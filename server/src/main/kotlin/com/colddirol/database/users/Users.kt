package com.colddirol.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * The Users object is used to manage queries against the *users* table in the database
 *
 * @author Vladimir Kartashev
 */
object Users: Table("Users") {
    private val id = Users.integer("id").autoIncrement()
    private val login = Users.varchar("login", 20)
    private val password = Users.varchar("password", 128)

    /**
     * The insert() method allows you to insert a new object into the *users* table
     *
     * @param userDTO Data Transfer Object of table *tokens* (Formed in the RegisterController class)
     * @return void
     */
    fun insert(userDTO: UserDTO) {
        // transaction scope
        transaction {
           Users.insert {
               it[login] = userDTO.login
               it[password] = userDTO.password
           }
        }
    }

    /**
     * The fetchUser() function allows you to
     * return the UserDTO of the user by his login (if there is one in the database)
     *
     * @param login Login of the user who is currently logging in / registering
     * @return UserDTO (can be null if user does not exist)
     */
    fun fetchUser(login: String): UserDTO? {
        return try {
            transaction {
                // single() - вернется первый результат
                val userModel = Users.select { Users.login.eq(login) }.single()

                UserDTO(
                    login = userModel[Users.login],
                    password = userModel[password]
                )
            }
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * The fetchId() function allows you to
     * return the User ID by his login
     *
     * @param login Login of the user who is currently logging in / registering
     * @return Int (can't be null)
     */
    fun fetchId(login: String): Int {
        return transaction {
            val userModel = Users.select { Users.login.eq(login) }.single()
            userModel[Users.id]
        }
    }
}