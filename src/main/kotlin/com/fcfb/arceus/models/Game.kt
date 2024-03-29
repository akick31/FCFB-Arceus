package com.fcfb.arceus.models

class Game {

    data class StartRequest (
        val homePlatform: Platform?,
        val homePlatformId: String?,
        val awayPlatform: Platform?,
        val awayPlatformId: String?,
        val season: String,
        val week: String,
        val subdivision: Subdivision?,
        val homeTeam: String,
        val awayTeam: String,
        val tvChannel: TVChannel?,
        val startTime: String?,
        val location: String?,
        val isScrimmage: Boolean?
    )
    enum class Subdivision(subdivision: String) {
        FBS("FBS"),
        FCS("FCS"),
    }

    enum class OffensivePlaybooks(offensivePlaybook: String) {
        FLEXBONE("Flexbone"),
        AIR_RAID("Air Raid"),
        PRO("Pro"),
        SPREAD("Spread"),
        WEST_COAST("West Coast"),
    }

    enum class DefensivePlaybooks(defensivePlaybook: String) {
        FOUR_THREE("4-3"),
        THREE_FOUR("3-4"),
        FIVE_TWO("5-2"),
        FOUR_FOUR("4-4"),
        THREE_THREE_FIVE("3-3-5"),
    }

    enum class TVChannel(channel: String) {
        ABC("ABC"),
        CBS("CBS"),
        ESPN("ESPN"),
        ESPN2("ESPN2"),
        FOX("FOX"),
        FS1("FS1"),
        FS2("FS2"),
        NBC("NBC"),
    }

    enum class Platform(platform: String) {
        DISCORD("Discord"),
        REDDIT("Reddit"),
    }

    enum class Scenario(scenario: String) {
        GAME_START("GAME_START"),
        COIN_TOSS("COIN_TOSS"),
        TURNOVER_TOUCHDOWN("TURNOVER_TOUCHDOWN"),
        TURNOVER_PLUS_20("TURNOVER_PLUS_20"),
        TURNOVER_PLUS_15("TURNOVER_PLUS_15"),
        TURNOVER_PLUS_10("TURNOVER_PLUS_10"),
        TURNOVER_PLUS_5("TURNOVER_PLUS_5"),
        TURNOVER("TURNOVER"),
        TURNOVER_MINUS_5("TURNOVER_MINUS_5"),
        TURNOVER_MINUS_10("TURNOVER_MINUS_10"),
        TURNOVER_MINUS_15("TURNOVER_MINUS_15"),
        TURNOVER_MINUS_20("TURNOVER_MINUS_20"),
        LOSS_OF_10("LOSS_OF_10"),
        LOSS_OF_5("LOSS_OF_5"),
        LOSS_OF_3("LOSS_OF_3"),
        LOSS_OF_1("LOSS_OF_1"),
        NO_GAIN("NO_GAIN"),
        INCOMPLETE("INCOMPLETE"),
        GAIN_OF_1("GAIN_OF_1"),
        GAIN_OF_2("GAIN_OF_2"),
        GAIN_OF_3("GAIN_OF_3"),
        GAIN_OF_4("GAIN_OF_4"),
        GAIN_OF_5("GAIN_OF_5"),
        GAIN_OF_6("GAIN_OF_6"),
        GAIN_OF_7("GAIN_OF_7"),
        GAIN_OF_8("GAIN_OF_8"),
        GAIN_OF_9("GAIN_OF_9"),
        GAIN_OF_10("GAIN_OF_10"),
        GAIN_OF_11("GAIN_OF_11"),
        GAIN_OF_12("GAIN_OF_12"),
        GAIN_OF_13("GAIN_OF_13"),
        GAIN_OF_14("GAIN_OF_14"),
        GAIN_OF_15("GAIN_OF_15"),
        GAIN_OF_16("GAIN_OF_16"),
        GAIN_OF_17("GAIN_OF_17"),
        GAIN_OF_18("GAIN_OF_18"),
        GAIN_OF_19("GAIN_OF_19"),
        GAIN_OF_20("GAIN_OF_20"),
        GAIN_OF_25("GAIN_OF_25"),
        GAIN_OF_30("GAIN_OF_30"),
        GAIN_OF_35("GAIN_OF_35"),
        GAIN_OF_40("GAIN_OF_40"),
        GAIN_OF_45("GAIN_OF_45"),
        GAIN_OF_50("GAIN_OF_50"),
        GAIN_OF_55("GAIN_OF_55"),
        GAIN_OF_60("GAIN_OF_60"),
        GAIN_OF_65("GAIN_OF_65"),
        GAIN_OF_70("GAIN_OF_70"),
        GAIN_OF_75("GAIN_OF_75"),
        GAIN_OF_80("GAIN_OF_80"),
        GAIN_OF_85("GAIN_OF_85"),
        GAIN_OF_90("GAIN_OF_90"),
        GAIN_OF_95("GAIN_OF_95"),
        TOUCHDOWN("TOUCHDOWN"),
        //TODO: Add kickoffs, punts, field goals, etc
    }

}