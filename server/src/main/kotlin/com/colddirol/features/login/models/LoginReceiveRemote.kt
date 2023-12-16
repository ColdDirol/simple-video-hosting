package com.colddirol.features.login.models

import kotlinx.serialization.Serializable

/**
 * Date class LoginReceiveRemote contains data coming from the user (login, password)\
 *
 * @author Vladimir Kartashev
 */
@Serializable
data class LoginReceiveRemote(
    val login: String,
    val password: String
)