package com.fcfb.arceus.domain.enums

import com.fasterxml.jackson.annotation.JsonCreator

enum class Platform(val description: String) {
    DISCORD("DISCORD"),
    ;

    companion object {
        @JsonCreator
        fun fromDescription(description: String): Platform =
            Platform.entries.find { it.description.equals(description, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown platform: $description")
    }
}
