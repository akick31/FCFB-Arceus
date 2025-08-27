package com.fcfb.arceus.enums.team

enum class Conference(val description: String) {
    ACC("ACC"),
    AMERICAN("American"),
    BIG_12("Big 12"),
    BIG_TEN("Big Ten"),
    FAKE_TEAM("Fake Team"),
    FBS_INDEPENDENT("FBS Independent"),
    MAC("MAC"),
    MOUNTAIN_WEST("Mountain West"),
    PAC_12("Pac-12"),
    SEC("SEC"),
    SUN_BELT("Sun Belt"),
    MISSOURI_VALLEY("Missouri Valley"),
    COLONIAL("Colonial"),
    NEC("NEC"),
    ;

    companion object {
        fun fromString(description: String): Conference? {
            return Conference.values().find { it.description == description }
        }
    }
}
