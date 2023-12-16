package com.colddirol.services.authentication

import io.ktor.server.application.*
import io.ktor.server.auth.*

/**
 * The AuthenticationService object allows to get user data (credentials, tokens)
 *
 * @author Vladimir Kartashev
 *
 */
object AuthenticationService {
    /**
     * The function *getTokenOrNull()* allows to get token
     * or null (if the user did not send the token header)
     *
     * @param call ApplicationCall
     *
     * @return String (incoming token) or null
     *
     */
    fun getTokenOrNull(call: ApplicationCall): String? {
        return call.principal<BearerTokenPrincipal>()?.token
    }
}