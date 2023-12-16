package com.colddirol.features.registration.models

import kotlinx.serialization.Serializable

/**
 * Date class RegisterReceiveRemote contains data coming from the user (login, password)
 *
 * @author Vladimir Kartashev
 */
@Serializable
data class RegisterReceiveRemote(
    val login: String,
    val password: String
)