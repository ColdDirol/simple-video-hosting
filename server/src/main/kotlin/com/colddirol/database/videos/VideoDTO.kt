package com.colddirol.database.videos

/**
 * The VideoDTODTO data class is used to
 * configure receiving/sending data to the
 * *users* table in the Database
 *
 * @author Vladimir Kartashev
 */
data class VideoDTO (
    //data transfer object
    val user_id: Int,
    val name: String
)