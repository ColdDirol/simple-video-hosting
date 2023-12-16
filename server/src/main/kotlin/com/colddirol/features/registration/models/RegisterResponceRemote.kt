package com.colddirol.features.registration.models

import kotlinx.serialization.Serializable

/**
 * Date class RegisterResponseRemote contains the data sent to the user (his token)
 *
 * @author Vladimir Kartashev
 */
@Serializable
data class RegisterResponseRemote(
    val token: String
)