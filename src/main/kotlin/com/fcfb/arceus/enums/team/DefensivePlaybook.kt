package com.fcfb.arceus.enums.team

enum class DefensivePlaybook(val description: String) {
    FOUR_THREE("4-3"),
    THREE_FOUR("3-4"),
    FIVE_TWO("5-2"),
    FOUR_FOUR("4-4"),
    THREE_THREE_FIVE("3-3-5"),
    ;

    companion object {
        fun fromString(description: String): DefensivePlaybook {
            return entries.find { it.description == description }
                ?: throw IllegalArgumentException("Unknown DefensivePlaybook: $description")
        }
    }
}
