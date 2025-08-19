package com.fcfb.arceus.enums.user

enum class CoachPosition(val description: String) {
    HEAD_COACH("Head Coach"),
    OFFENSIVE_COORDINATOR("Offensive Coordinator"),
    DEFENSIVE_COORDINATOR("Defensive Coordinator"),
    RETIRED("Retired"),
    ;

    companion object {
        fun fromString(description: String): CoachPosition? {
            return entries.find { it.description == description }
        }
    }
}
