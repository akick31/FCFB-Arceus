package com.fcfb.arceus.enums.system

import com.fasterxml.jackson.annotation.JsonCreator

enum class Platform(val description: String) {
    DISCORD("DISCORD"),
    ;

    companion object {
        @JsonCreator
        fun fromDescription(description: String): com.fcfb.arceus.enums.system.Platform =
            com.fcfb.arceus.enums.system.Platform.entries.find { it.description.equals(description, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown platform: $description")
    }
}
