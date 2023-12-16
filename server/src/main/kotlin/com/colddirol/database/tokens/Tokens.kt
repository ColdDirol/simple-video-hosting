package com.colddirol.database.tokens

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

/**
 * The Tokens object is used to manage queries against the *tokens* table in the database
 *
 * @author Vladimir Kartashev
 */
object Tokens: Table("Tokens") {
    private val user_id = Tokens.integer("user_id")
    private val token = Tokens.varchar("token", 50)

    /**
     * The insert() function allows you to insert a new object into the *tokens* table
     *
     * @param tokenDTO Data Transfer Object of table *tokens* (Formed in the RegisterController class)
     * @return void
     */
    fun insert(tokenDTO: TokenDTO) {
        // transaction scope
        transaction {
            Tokens.insert {
                it[user_id] = tokenDTO.user_id
                it[token] = tokenDTO.token
            }
        }
    }

    /**
     * The update() function allows you to update the token field in an existing *tokens* table object
     *
     * @param user_id User ID whose token needs to be refreshed
     * @param newToken New token
     * @return void
     */
    fun update(user_id: Int, newToken: String) {
        transaction {
            Tokens.update({ Tokens.user_id eq user_id }) {
                it[token] = newToken
            }
        }
    }

    /**
     * The *fetchUserId()* function allows you to
     * return the User ID (who owns this token)
     *
     * @param token Token of the user who is currently using it
     * @return Int (can't be null)
     */
    fun fetchUserId(token: String): Int {
        return transaction {
            val tokenModel = Tokens.select { Tokens.token.eq(token) }.single()
            tokenModel[user_id]
        }
    }

    /**
     * The isTrueToken() function lets you know the existence of the token sent from the website.
     * Used to authorize the user.
     *
     * @param token Token sent by the website (Received in LoginController)
     * @return Boolean
     */
    fun isTrueToken(token: String): Boolean {
        var isTokenValid = false

        transaction {
            val result = Tokens.select { Tokens.token.eq(token) }.singleOrNull()

            if(result != null) {
                val storedToken = result[Tokens.token]
                isTokenValid = token == storedToken
            }
        }

        return isTokenValid
    }
}