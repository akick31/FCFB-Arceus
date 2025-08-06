package com.fcfb.arceus.domain.enums

import com.fasterxml.jackson.annotation.JsonCreator

enum class Warning(val description: String) {
    NONE("NONE"),
    FIRST_WARNING("FIRST_WARNING"),
    SECOND_WARNING("SECOND_WARNING"),
    ;

    companion object {
        @JsonCreator
        fun fromDescription(description: String): Warning =
            entries.find { it.description.equals(description, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown warning: $description")
    }
} 