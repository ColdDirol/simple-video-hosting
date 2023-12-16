package com.colddirol.services.minio

import com.colddirol.YamlConfiguration
import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient

/**
 * The MinioBucketService object contains the bucketName field, which is set in main().
 *
 * After setting the value through the setter, the createBucket() function is launched.
 *
 * @author Vladimir Kartashev
 */
object MinioBucketService {
    /**
     * The bucketName field contains the name of the future bucket in Minio S3
     */
    lateinit var bucketName: String

    fun initMinioBucketService() {
        val minioConfig = YamlConfiguration.getApplicationConfig("ktor.minio")

        bucketName = minioConfig.propertyOrNull("bucket")?.getString().toString()

        createBucket()

        println("--initMinioBucketService initialization has been completed")
    }


    /**
     * The createBucket() function is run after setting the value of bucketName via its setter.
     *
     * The function checks the bucket in Minio S3 for presence.
     * - If there is no such a bucket, the function simply creates it.
     *
     * @return void
     */
    private fun createBucket() {
        try {
            val minioClient = MinioClientService.createMinioClient()
            if (!minioClient.bucketExists(
                    BucketExistsArgs.builder()
                        .bucket(bucketName).build()
                )
            ) {
                newBucket(minioClient)
            }
        } catch (e: Exception) {
            throw IllegalStateException(e.message)
        }
    }

    /**
     * the newBucket() function creates a bucket with the name given by bucketName.
     *
     * @param minioClient Object of the MinioClient
     *
     * @return void
     */
    private fun newBucket(minioClient: MinioClient) {
        minioClient.makeBucket(
            MakeBucketArgs.builder().bucket(bucketName).build()
        )
    }
}