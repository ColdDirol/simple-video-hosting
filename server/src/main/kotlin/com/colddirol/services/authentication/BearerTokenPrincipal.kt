package com.colddirol.services.authentication

import io.ktor.server.auth.*

/**
 * Date class BearerTokenPrincipal contains data coming from the user (token)
 * to use Bearer Authentication.
 *
 * @author Vladimir Kartashev
 */
data class BearerTokenPrincipal(val token: String) : Principal
