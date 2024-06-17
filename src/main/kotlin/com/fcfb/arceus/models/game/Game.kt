package com.fcfb.arceus.models.game

class Game {
    enum class Subdivision(val description: String) {
        FBS("FBS"),
        FCS("FCS"),
    }

    enum class OffensivePlaybook(val description: String) {
        FLEXBONE("Flexbone"),
        AIR_RAID("Air Raid"),
        PRO("Pro"),
        SPREAD("Spread"),
        WEST_COAST("West Coast"),
    }

    enum class DefensivePlaybook(val description: String) {
        FOUR_THREE("4-3"),
        THREE_FOUR("3-4"),
        FIVE_TWO("5-2"),
        FOUR_FOUR("4-4"),
        THREE_THREE_FIVE("3-3-5"),
    }

    enum class TVChannel(val description: String) {
        ABC("ABC"),
        CBS("CBS"),
        ESPN("ESPN"),
        ESPN2("ESPN2"),
        FOX("FOX"),
        FS1("FS1"),
        FS2("FS2"),
        NBC("NBC"),
    }

    enum class Platform(val description: String) {
        DISCORD("Discord"),
        REDDIT("Reddit"),
    }

    enum class Play(val description: String) {
        RUN("RUN"),
        PASS("PASS"),
        SPIKE("SPIKE"),
        KNEEL("KNEEL"),
        FIELD_GOAL("FIELD GOAL"),
        PAT("PAT"),
        TWO_POINT("TWO POINT"),
        KICKOFF_NORMAL("KICKOFF NORMAL"),
        KICKOFF_ONSIDE("KICKOFF ONSIDE"),
        KICKOFF_SQUIB("KICKOFF SQUIB"),
        PUNT("PUNT"),
    }

    enum class PlayType(val description: String) {
        NORMAL("NORMAL"),
        KICKOFF("KICKOFF"),
        PAT("PAT"),
    }

    enum class Result(val description: String) {
        GOOD("GOOD"),
        NO_GOOD("NO GOOD"),
        NO_GAIN("NO GAIN"),
        INCOMPLETE("INCOMPLETE"),
        LOSS_OF_10_YARDS("-10"),
        LOSS_OF_5_YARDS("-5"),
        LOSS_OF_3_YARDS("-3"),
        LOSS_OF_1_YARD("-1"),
        GAIN_OF_1_YARD("1"),
        GAIN_OF_2_YARDS("2"),
        GAIN_OF_3_YARDS("3"),
        GAIN_OF_4_YARDS("4"),
        GAIN_OF_5_YARDS("5"),
        GAIN_OF_6_YARDS("6"),
        GAIN_OF_7_YARDS("7"),
        GAIN_OF_8_YARDS("8"),
        GAIN_OF_9_YARDS("9"),
        GAIN_OF_10_YARDS("10"),
        GAIN_OF_11_YARDS("11"),
        GAIN_OF_12_YARDS("12"),
        GAIN_OF_13_YARDS("13"),
        GAIN_OF_14_YARDS("14"),
        GAIN_OF_15_YARDS("15"),
        GAIN_OF_16_YARDS("16"),
        GAIN_OF_17_YARDS("17"),
        GAIN_OF_18_YARDS("18"),
        GAIN_OF_19_YARDS("19"),
        GAIN_OF_20_YARDS("20"),
        GAIN_OF_25_YARDS("25"),
        GAIN_OF_30_YARDS("30"),
        GAIN_OF_35_YARDS("35"),
        GAIN_OF_40_YARDS("40"),
        GAIN_OF_45_YARDS("45"),
        GAIN_OF_50_YARDS("50"),
        GAIN_OF_55_YARDS("55"),
        GAIN_OF_60_YARDS("60"),
        GAIN_OF_65_YARDS("65"),
        GAIN_OF_70_YARDS("70"),
        GAIN_OF_75_YARDS("75"),
        GAIN_OF_80_YARDS("80"),
        GAIN_OF_85_YARDS("85"),
        GAIN_OF_90_YARDS("90"),
        GAIN_OF_95_YARDS("95"),
        TURNOVER_PLUS_20_YARDS("TURNOVER + 20 YARDS"),
        TURNOVER_PLUS_15_YARDS("TURNOVER + 15 YARDS"),
        TURNOVER_PLUS_10_YARDS("TURNOVER + 10 YARDS"),
        TURNOVER_PLUS_5_YARDS("TURNOVER + 5 YARDS"),
        TURNOVER("TURNOVER"),
        TURNOVER_MINUS_5_YARDS("TURNOVER - 5 YARDS"),
        TURNOVER_MINUS_10_YARDS("TURNOVER - 10 YARDS"),
        TURNOVER_MINUS_15_YARDS("TURNOVER - 15 YARDS"),
        TURNOVER_MINUS_20_YARDS("TURNOVER - 20 YARDS"),
        TURNOVER_ON_DOWNS("TURNOVER ON DOWNS"),
        TURNOVER_TOUCHDOWN("PICK/FUMBLE SIX"),
        TOUCHDOWN("TOUCHDOWN"),
        SAFETY("SAFETY"),
        FUMBLE("FUMBLE"),
        FIVE_YARD_LINE("5"),
        TEN_YARD_LINE("10"),
        TWENTY_YARD_LINE("20"),
        THIRTY_YARD_LINE("30"),
        THIRTY_FIVE_YARD_LINE("35"),
        FOURTY_YARD_LINE("40"),
        FOURTY_FIVE_YARD_LINE("45"),
        FIFTY_YARD_LINE("50"),
        SIXTY_FIVE_YARD_LINE("65"),
        TOUCHBACK("TOUCHBACK"),
        RETURN_TOUCHDOWN("RETURN TOUCHDOWN"),
        RECOVERED("RECOVERED"),
        DEFENSE_TWO_POINT("DEFENSE TWO POINT")
    }

    enum class ActualResult(val description: String) {
        FIRST_DOWN("FIRST DOWN"),
        GAIN("GAIN"),
        NO_GAIN("NO GAIN"),
        TURNOVER_ON_DOWNS("TURNOVER ON DOWNS"),
        TOUCHDOWN("TOUCHDOWN"),
        SAFETY("SAFETY"),
        TURNOVER("TURNOVER"),
        TURNOVER_TOUCHDOWN("TURNOVER TOUCHDOWN"),
        KICKING_TEAM_TOUCHDOWN("KICKING TEAM TOUCHDOWN"),
        RETURN_TOUCHDOWN("KICKING TEAM TOUCHDOWN"),
        MUFFED_KICK("MUFFED KICK"),
        KICKOFF("KICKOFF"),
        SUCCESSFUL_ONSIDE("SUCCESSFUL ONSIDE"),
        FAILED_ONSIDE("FAILED ONSIDE"),
        GOOD("GOOD"),
        NO_GOOD("NO GOOD"),
        DEFENSE_TWO_POINT("DEFENSE TWO POINT")
    }

    enum class RunoffType(val description: String) {
        CHEW("CHEW"),
        HURRY("HURRY"),
        NORMAL("NORMAL"),
    }

    enum class Scenario(val description: String) {
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

    enum class Possession(val description: String) {
        HOME("home"),
        AWAY("away")
    }

    enum class CoinTossChoice(val description: String) {
        RECEIVE("receive"),
        DEFER("defer")
    }

    enum class CoinTossWinner(val description: String) {
        HOME("home"),
        AWAY("away")
    }

}