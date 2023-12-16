package com.colddirol.services.minio

import com.colddirol.YamlConfiguration
import io.minio.MinioClient

/**
 * The MinioClientService object controls the issuance of the MinioClient object.
 *
 * @author Vladimir Kartashev
 */
object MinioClientService {
    private lateinit var endpoint: String
    private lateinit var accessKey: String
    private lateinit var secretKey: String


    fun initMinioClientService() {
        val minioConfig = YamlConfiguration.getApplicationConfig("ktor.minio")

        endpoint = minioConfig.propertyOrNull("endpoint")?.getString().toString()
        accessKey = minioConfig.propertyOrNull("accessKey")?.getString().toString()
        secretKey = minioConfig.propertyOrNull("secretKey")?.getString().toString()

        println("--endpoint = $endpoint, accessKey = $accessKey, secretKey = $secretKey")

        println("--initMinioClientService initialization has been completed")
    }

    /**
     * The createMinioClient() function creates and returns a MinioClient.
     *
     * - username and password are in the resources/credentialsForDatabase file
     * (used for database and Minio S3);
     *
     * - username and password are scanned using the CredentialsService object.
     *
     * @return MinioClient
     */
    fun createMinioClient(): MinioClient {
        return try {
            MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build()
        } catch (e: Exception) {
            throw IllegalStateException(e.message)
        }
    }
}