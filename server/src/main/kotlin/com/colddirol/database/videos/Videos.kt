package com.colddirol.database.videos

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Videos: Table("Videos") {
    private val user_id = Videos.integer("user_id")
    private val name = Videos.varchar("name", 50)

    /**
     * The insert() function allows you to insert a new object into the *videos* table
     *
     * @param videoDTO Data Transfer Object of table *videos* (Formed in the UploadController class)
     * @return void
     */
    fun insert(videoDTO: VideoDTO) {
        // transaction scope
        transaction {
            Videos.insert {
                it[user_id] = videoDTO.user_id
                it[name] = videoDTO.name
            }
        }
    }
}