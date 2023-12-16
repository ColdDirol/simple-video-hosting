package com.colddirol.features.registration.controllers

import com.colddirol.database.tokens.TokenDTO
import com.colddirol.database.tokens.Tokens
import com.colddirol.database.users.UserDTO
import com.colddirol.database.users.Users
import com.colddirol.features.registration.models.RegisterReceiveRemote
import com.colddirol.features.registration.models.RegisterResponseRemote
import com.colddirol.services.authentication.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import java.util.*

/**
 * The RegisterController class is called from RegisterRoutes to handle the /register request from the client
 *
 * @param call ApplicationCall
 *
 * @author Vladimir Kartashev
 */
class RegisterController(private var call: ApplicationCall) {

    /**
     * The handleRegistration() function handles the /register request
     *
     * @return void
     *
     * @throws ExposedSQLException
     */
    suspend fun handleRegistration() {
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()

        val userDTO = Users.fetchUser(registerReceiveRemote.login)

        if(userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            val token = UUID.randomUUID().toString()

            try {
                Users.insert(
                    UserDTO(
                        login = registerReceiveRemote.login,
                        password = HashingService.sha512(registerReceiveRemote.password)
                    )
                )
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, "User already exists") // получаем
            }
            Tokens.insert(
                TokenDTO(
                    user_id = Users.fetchId(registerReceiveRemote.login),
                    token = token
                )
            )

            call.respond(RegisterResponseRemote(token = token))
        }
    }
}