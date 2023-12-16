package com.colddirol.features.registration.routes

import com.colddirol.features.registration.controllers.RegisterController
import io.ktor.server.application.*
import io.ktor.server.routing.*

/**
 * The function that accepts /register routing.
 * After accepting the request, calls the RegisterController to handle it.
 *
 * Request - login, password
 *
 * Response - token
 *
 * @return void
 *
 * @author Vladimir Kartashev
 */
fun Application.configureRegisterRoutes() {
    routing {
        post("/register") {
            val registerController = RegisterController(call)
            registerController.handleRegistration()
        }
    }
}