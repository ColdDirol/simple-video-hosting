package com.colddirol.features.stream.controllers

import com.colddirol.services.minio.MinioBucketService
import com.colddirol.services.minio.MinioClientService
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import io.minio.GetObjectArgs
import io.minio.StatObjectArgs
import io.minio.errors.MinioException
import kotlinx.coroutines.*
import java.io.IOException

/**
 * The StreamController class is called from StreamRoutes to handle the...
 *
 * /stream/{token}/{id}
 *
 * ...request from the client
 *
 * {token} - User TOKEN (sent by URL request)
 *
 * {id} - Video ID (sent by URL request)
 *
 * @param call ApplicationCall
 *
 * @author Vladimir Kartashev
 */
class StreamController(private var call: ApplicationCall) {

    /**
     * The handleStreamRequest() function handles the /stream/{token}/{id} request.
     *
     * To disable deletion - you need to comment out or remove the FINALLY { } block.
     *
     * @return void
     *
     * @throws MinioException
     */
    suspend fun handleStreamRequest() {
        try {
            val id = call.parameters["id"]
            if (id == null) {
                call.respondText("Video not found", status = HttpStatusCode.NotFound)
            } else {
                val objectName = "video_$id.mp4"

                // Вызов заголовка, содержащего диапазон Range
                val rangeHeader = call.request.header(HttpHeaders.Range)
                val range = rangeHeader?.let {
                    parseRange(it)
                }

                withContext(Dispatchers.IO) {
                    try {
                        val minioClient = MinioClientService.createMinioClient()

                        // Получаем информацию об объекте
                        val statObject = minioClient.statObject(
                            StatObjectArgs.builder()
                                .bucket(MinioBucketService.bucketName)
                                .`object`(objectName)
                                .build()
                        )

                        // Стандартные значения
                        var start: Long = 0
                        val fileSize = statObject.size()
                        var end = fileSize - 1

                        // Если диапазон задан - установка значений
                        if (range != null) {
                            start = range.first
                            end = range.second ?: end
                        }

                        val length = end - start + 1

                        // Получаем частичный объект с указанным диапазоном
                        val objectReadStream = minioClient.getObject(
                            GetObjectArgs.builder()
                                .bucket(MinioBucketService.bucketName)
                                .`object`(objectName)
                                .offset(start)
                                .length(length)
                                .build()
                        )

                        val headers = Headers.build {
                            // Установка Content-Type
                            append(HttpHeaders.ContentType, ContentType.Video.MP4.toString())

                            // Установка Accept-Ranges
                            append(HttpHeaders.AcceptRanges, "bytes")

                            // Установка Content-Range
                            if (range != null) {
                                append(
                                    HttpHeaders.ContentRange,
                                    "bytes $start-$end/${statObject.size()}"
                                )
                            }

                            // Установка Content-Length
                            append(HttpHeaders.ContentLength, length.toString())
                        }

                        val status = if (range != null) HttpStatusCode.PartialContent else HttpStatusCode.OK

                        // В ответ отправляем стрим с указанным Range
                        call.respond(
                            status = status,
                            message = object : OutgoingContent.WriteChannelContent() {
                                override val contentType: ContentType = ContentType.Video.MP4
                                override val headers: Headers = headers

                                override suspend fun writeTo(channel: ByteWriteChannel) {
                                    objectReadStream.use {
                                        it.copyTo(channel.toOutputStream())
                                    }
                                }
                            }
                        )
                    } catch (e: MinioException) {
                        e.printStackTrace()
                        call.respond(HttpStatusCode.NotFound, "Video not found")
                    }
                }
            }
        } catch (e: IOException) {
            // some message for time switching
        }
    }

    /**
     * Helper function to parse range from the header
     *
     * @param rangeHeader
     * @return Pair<Long, Long?>?
     */
    private fun parseRange(rangeHeader: String): Pair<Long, Long?>? {
        val regex = "bytes=(\\d+)-(\\d*)".toRegex()
        val matchResult = regex.find(rangeHeader)

        return matchResult?.let {
            val values = it.destructured
            val start = values.component1().toLong()
            val end = values.component2().toLongOrNull()

            start to end
        }
    }
}