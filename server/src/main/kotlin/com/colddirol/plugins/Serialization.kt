package com.colddirol.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

/**
 * Standard implementation of Kotlinx Serialization JSON
 *
 * @return void
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
