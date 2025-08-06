package com.fcfb.arceus.domain.enums

import com.fasterxml.jackson.annotation.JsonCreator

enum class GameType(val description: String) {
    OUT_OF_CONFERENCE("Out of Conference"),
    CONFERENCE_GAME("Conference Game"),
    CONFERENCE_CHAMPIONSHIP("Conference Championship"),
    PLAYOFFS("Playoffs"),
    NATIONAL_CHAMPIONSHIP("National Championship"),
    BOWL("Bowl"),
    SCRIMMAGE("Scrimmage"),
    ;

    companion object {
        @JsonCreator
        fun fromDescription(description: String): GameType =
            entries.find { it.description.equals(description, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown game type: $description")
    }
}
