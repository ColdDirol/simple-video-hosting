package com.colddirol.features.login.controllers

import com.colddirol.database.tokens.Tokens
import com.colddirol.database.users.Users
import com.colddirol.features.login.models.LoginReceiveRemote
import com.colddirol.features.login.models.LoginResponseRemote
import com.colddirol.services.authentication.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

/**
 * The LoginController class is called from LoginRoutes to handle the /login request from the client
 *
 * Request - login, password
 *
 * Response - token
 *
 * @param call ApplicationCall
 *
 * @author Vladimir Kartashev
 */
class LoginController(private val call: ApplicationCall) {

    /**
     * The handleLogin() function handles the /login request
     *
     * @return void
     */
    suspend fun handleLogin() {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUser(receive.login)

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
        } else {
            if(userDTO.password == HashingService.sha512(receive.password)) {
                val token = UUID.randomUUID().toString()
                Tokens.update(Users.fetchId(receive.login), token)

                call.respond(LoginResponseRemote(token = token))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }
}