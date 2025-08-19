package com.fcfb.arceus.enums.user

enum class TransactionType(val description: String) {
    HIRED("Hired"),
    HIRED_INTERIM("Hired Interim"),
    FIRED("Fired"),
    ;

    companion object {
        fun fromString(description: String): TransactionType? {
            return TransactionType.entries.find { it.description == description }
        }
    }
}
