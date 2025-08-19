package com.fcfb.arceus.enums.gameflow

enum class CoinTossCall(val description: String) {
    HEADS("heads"),
    TAILS("tails"),
    ;

    companion object {
        fun fromString(description: String): CoinTossCall? {
            return entries.find { it.description == description }
        }
    }
}
