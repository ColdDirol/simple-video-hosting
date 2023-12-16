package com.colddirol.services.authentication

import java.math.BigInteger
import java.security.MessageDigest

/**
 * The AuthenticationService object allows to encode user password
 *
 * @author Vladimir Kartashev
 */
object HashingService {
    /**
     * The function *sha512()* allows to encrypt user password
     *
     * @param input (Password)
     *
     * @return String (Hashcode)
     *
     */
    fun sha512(input: String): String {
        val md = MessageDigest.getInstance("SHA-512")
        val messageDigest = md.digest(input.toByteArray())
        val no = BigInteger(1, messageDigest)
        var hashText = no.toString(16)

        while (hashText.length < 32) {
            hashText = "0$hashText"
        }

        return hashText
    }
}