package com.colddirol.features.login.models

import kotlinx.serialization.Serializable

/**
 * Date class LoginResponseRemote contains the data sent to the user (his token)
 *
 * @author Vladimir Kartashev
 */
@Serializable
data class LoginResponseRemote(
    val token: String
)