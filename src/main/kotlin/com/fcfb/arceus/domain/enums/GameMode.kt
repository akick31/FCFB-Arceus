package com.fcfb.arceus.domain.enums

import com.fasterxml.jackson.annotation.JsonCreator

enum class GameMode(val description: String) {
    NORMAL("Normal"),
    CHEW("Chew"),
    ;

    companion object {
        @JsonCreator
        fun fromDescription(description: String): GameMode =
            entries.find { it.description.equals(description, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown game mode: $description")
    }
} 