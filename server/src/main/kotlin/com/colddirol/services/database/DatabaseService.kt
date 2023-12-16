package com.colddirol.services.database

import com.colddirol.YamlConfiguration
import com.colddirol.database.tokens.Tokens
import com.colddirol.database.users.Users
import com.colddirol.database.videos.Videos
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * The DatabaseService object initializes the *InMemory* database with the *initDatabase()* function.
 *
 * @author Vladimir Kartashev
 */
object DatabaseService {
    fun initDatabase() {
        try {
            val databaseConfig = YamlConfiguration.getApplicationConfig("ktor.database")

            val url = databaseConfig.propertyOrNull("url")?.getString()
            val driver = databaseConfig.propertyOrNull("driver")?.getString()
            val user = databaseConfig.propertyOrNull("user")?.getString()
            val password = databaseConfig.propertyOrNull("password")?.getString()

            // Connect to the database without specifying username or password
            if (url != null && driver != null && user != null && password != null) {
                Database.connect(
                    url,
                    driver = driver,
                    user = user,
                    password = password
                )
            }

            // Create tables using Exposed
            transaction {
                SchemaUtils.create(Users, Tokens, Videos)
            }

            println("--Database tables has been created.")
        } catch (e: Exception) {
            throw IllegalStateException(e.message)
        }
    }
}