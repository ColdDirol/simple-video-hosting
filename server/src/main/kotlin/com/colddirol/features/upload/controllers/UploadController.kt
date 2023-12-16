package com.colddirol.features.upload.controllers

import com.colddirol.database.tokens.Tokens
import com.colddirol.database.videos.VideoDTO
import com.colddirol.database.videos.Videos
import com.colddirol.services.minio.MinioBucketService
import com.colddirol.services.minio.MinioClientService
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.minio.PutObjectArgs
import io.minio.errors.MinioException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.lang.IllegalStateException
import java.util.*

/**
 * The UploadController class is called from UploadRoutes to handle the...
 *
 * /upload/{token}
 *
 * ...request from the client
 *
 * {token} - User TOKEN (sent by URL request)
 *
 * @param call ApplicationCall
 *
 * @author Vladimir Kartashev
 */
class UploadController(private var call: ApplicationCall) {
    /**
     * The handleUploadRequest() function handles the /upload/{token} request.
     *
     * @return void
     *
     * @throws IllegalStateException No video file in the request
     * @throws IllegalArgumentException Not a mp4 file
     */
    suspend fun handleUploadRequest(token: String) {
        try {
            val multipart = call.receiveMultipart()

            // fast variant
            val id = UUID.randomUUID()

            // for DB table *videos*
            val userId = Tokens.fetchUserId(token)

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        val ext = when (part.contentType?.contentSubtype) {
                            "mp4" -> ".mp4"
                            else -> throw IllegalArgumentException("Only .mp4 files are allowed")
                        }

                        // Загрузка видео на Minio S3 используя поток
                        withContext(Dispatchers.IO) {
                            try {
                                val minioClient = MinioClientService.createMinioClient()
                                val fileName = "video_${id}${ext}"

                                // получение потока
                                val input = part.streamProvider()

                                // закидываем поток сразу в putObject
                                minioClient.putObject(
                                    PutObjectArgs.builder()
                                        .bucket(MinioBucketService.bucketName)
                                        .`object`(fileName)
                                        .stream(input, -1, 5 * 1024 * 1024)
                                        .contentType("video/mp4")
                                        .build()
                                )

                                // вставка в таблицу *videos* в БД
                                try {
                                    Videos.insert(
                                        VideoDTO(
                                            user_id = userId,
                                            name = fileName
                                        )
                                    )
                                } catch (e: ExposedSQLException) {
                                    call.respond(HttpStatusCode.Conflict, "User already exists")
                                } finally {
                                    input.close()
                                }

                            } catch (e: MinioException) {
                                e.printStackTrace()
                                call.respondText("Upload error", status = HttpStatusCode.BadRequest)
                            } finally {
                                part.dispose()
                            }
                        }
                    }

                    else -> {
                        // Проигнорировать другие части
                    }
                }
                part.dispose()
            }

            call.respondText("$id")
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            call.respondText("No video file in the request", status = HttpStatusCode.BadRequest)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            call.respondText("You have to send mp4 file", status = HttpStatusCode.BadRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}