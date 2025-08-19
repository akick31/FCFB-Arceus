package com.fcfb.arceus.enums.team

enum class OffensivePlaybook(val description: String) {
    FLEXBONE("FLEXBONE"),
    AIR_RAID("AIR RAID"),
    PRO("PRO"),
    SPREAD("SPREAD"),
    WEST_COAST("WEST COAST"),
    ;

    companion object {
        fun fromString(description: String): OffensivePlaybook? {
            return entries.find { it.description == description }
        }
    }
}
