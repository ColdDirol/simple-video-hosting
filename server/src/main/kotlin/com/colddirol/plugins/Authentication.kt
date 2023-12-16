package com.colddirol.plugins

import com.colddirol.database.tokens.Tokens
import com.colddirol.services.authentication.BearerTokenPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*

/**
 * Standard implementation of Authentication
 *
 * @return void
 *
 * @author Vladimir Kartashev
 */
fun Application.configureAuthentication() {
    install(Authentication) {
        /**
         * Implementation of Bearer Authentication
         */
        bearer("auth-bearer") {
            authenticate { tokenCredential ->
                if (Tokens.isTrueToken(tokenCredential.token)) {
                    BearerTokenPrincipal(tokenCredential.token)
                } else {
                    null
                }
            }
        }
    }
}