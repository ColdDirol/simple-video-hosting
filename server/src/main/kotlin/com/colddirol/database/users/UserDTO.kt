package com.colddirol.database.users

/**
 * The UserDTO data class is used to
 * configure receiving/sending data to the
 * *users* table in the Database
 *
 * @author Vladimir Kartashev
 */
data class UserDTO (
    //data transfer object
    val login: String,
    val password: String
)