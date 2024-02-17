package com.fcfb.arceus.utils

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SessionUtils {
    fun generateSessionToken(): String {
        // Generate a UUID (Universally Unique Identifier)
        val uuid = UUID.randomUUID()

        // Convert UUID to a string and return it as the session token
        return uuid.toString()
    }
}