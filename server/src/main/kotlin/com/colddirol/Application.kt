package com.colddirol

import com.colddirol.features.login.routes.configureLoginRoutes
import com.colddirol.features.registration.routes.configureRegisterRoutes
import com.colddirol.features.stream.routes.configureStreamRoutes
import com.colddirol.features.upload.routes.configureUploadRoutes
import com.colddirol.plugins.configureAuthentication
import com.colddirol.plugins.configureSerialization
import com.colddirol.services.database.DatabaseService
import com.colddirol.services.minio.MinioBucketService
import com.colddirol.services.minio.MinioClientService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.cors.routing.*

/**
 * The main function is used to:
 * - Reading credentialsForDatabase from the credentialsForDatabase file in the `resources` folder,
 * - Database connection,
 * - MinioBucketManager Initializations,
 * - Server startup.
 */
fun main() {
    try {
        DatabaseService.initDatabase()
        MinioClientService.initMinioClientService()
        MinioBucketService.initMinioBucketService()

        val serverConfig = YamlConfiguration.getApplicationConfig("ktor.deployment")

        val port = serverConfig.propertyOrNull("port")?.getString()?.toInt() ?: 8080
        val host = serverConfig.propertyOrNull("host")?.getString() ?: "0.0.0.0"

        embeddedServer(
            CIO,
            port = port,
            host = host,
            module = Application::module
        ).start(wait = true)

    } catch (e: Exception) {
        e.printStackTrace()
        throw RuntimeException(e.message)
    }
}

/**
 * Application.module() function is used to:
 * - Set CORS settings,
 * - Routing configuration,
 * - Actions after stopping the server
 */
fun Application.module() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.Accept)
        allowHeader(HttpHeaders.ContentLanguage)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowCredentials = true
        //for localhost
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    configureSerialization()
    configureAuthentication()

    configureLoginRoutes()
    configureRegisterRoutes()
    configureUploadRoutes()
    configureStreamRoutes()

    environment.monitor.subscribe(ApplicationStopPreparing) {
        println("--Server has been stopped!")
    }
}