package com.fcfb.arceus.enums.team

enum class Subdivision(val description: String) {
    FCFB("FCFB"),
    FBS("FBS"),
    FCS("FCS"),
    FAKE("FAKE"),
    ;

    companion object {
        fun fromString(description: String): Subdivision? {
            return entries.find { it.description == description }
        }
    }
}
