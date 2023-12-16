package com.colddirol.database.tokens

/**
 * The TokenDTO data class is used to
 * configure receiving/sending data to the
 * *tokens* table in the Database
 *
 * @author Vladimir Kartashev
 */
data class TokenDTO (
    //data transfer object
    val user_id: Int,
    val token: String
)