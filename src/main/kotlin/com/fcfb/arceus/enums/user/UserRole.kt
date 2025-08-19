package com.fcfb.arceus.enums.user

enum class UserRole(val description: String) {
    USER("User"),
    CONFERENCE_COMMISSIONER("Conference Commissioner"),
    ADMIN("Admin"),
    ;

    companion object {
        fun fromString(description: String): UserRole? {
            return entries.find { it.description == description }
        }
    }
}
