package com.colddirol.features.login.routes

import com.colddirol.features.login.controllers.LoginController
import io.ktor.server.application.*
import io.ktor.server.routing.*

/**
 * The function that accepts /login routing.
 * After accepting the request, calls the LoginController to handle it.
 *
 * @return void
 *
 * @author Vladimir Kartashev
 */
fun Application.configureLoginRoutes() {
    routing {
        post("/login") {
            val loginController = LoginController(call)
            loginController.handleLogin()
        }
    }
}