package com.colddirol.features.stream.routes

import com.colddirol.features.stream.controllers.StreamController
import io.ktor.server.application.*
import io.ktor.server.routing.*

/**
 * The function that accepts /stream/{token}/{id} routing.
 * After accepting the request, calls the StreamController to handle it.
 *
 * {token} - User TOKEN (sent by URL request)
 *
 * {id} - Video ID (sent by URL request)
 *
 * @return void
 *
 * @author Vladimir Kartashev
 */
fun Application.configureStreamRoutes() {
    routing {
        get("/stream/{id}") {
            val streamController = StreamController(call)
            streamController.handleStreamRequest()
        }
    }
}