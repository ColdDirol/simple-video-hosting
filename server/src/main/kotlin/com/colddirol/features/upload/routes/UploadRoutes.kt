package com.colddirol.features.upload.routes

import com.colddirol.features.upload.controllers.UploadController
import com.colddirol.services.authentication.AuthenticationService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * The function that accepts /upload/{token} routing.
 * After accepting the request, calls the UploadController to handle it.
 *
 * {token} - User TOKEN (sent by request header)
 *
 * @return void
 *
 * @author Vladimir Kartashev
 */
fun Application.configureUploadRoutes() {
    routing {
        authenticate("auth-bearer") {
            post("/upload") {
                val token = AuthenticationService.getTokenOrNull(call)

                if (token != null) {
                    val uploadController = UploadController(call)
                    uploadController.handleUploadRequest(token)
                } else {
                    call.respondText("Invalid token", status = HttpStatusCode.Unauthorized)
                }
            }
        }
    }
}