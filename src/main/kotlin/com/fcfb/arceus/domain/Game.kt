package com.fcfb.arceus.domain

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "game", schema = "arceus")
class Game {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "game_id")
    @JsonProperty("game_id")
    var gameId: Int? = 0

    @Basic
    @Column(name = "home_team")
    @JsonProperty("home_team")
    var homeTeam: String? = "home team"

    @Basic
    @Column(name = "away_team")
    @JsonProperty("away_team")
    var awayTeam: String? = "away team"

    @Basic
    @Column(name = "home_coach1")
    @JsonProperty("home_coach1")
    var homeCoach1: String? = "home coach"

    @Basic
    @Column(name = "home_coach2")
    @JsonProperty("home_coach2")
    var homeCoach2: String? = null

    @Basic
    @Column(name = "away_coach1")
    @JsonProperty("away_coach1")
    var awayCoach1: String? = "away coach"

    @Basic
    @Column(name = "away_coach2")
    @JsonProperty("away_coach2")
    var awayCoach2: String? = null

    @Basic
    @Column(name = "home_coach_discord_id1")
    @JsonProperty("home_coach_discord_id1")
    var homeCoachDiscordId1: String? = null

    @Basic
    @Column(name = "home_coach_discord_id2")
    @JsonProperty("home_coach_discord_id2")
    var homeCoachDiscordId2: String? = null

    @Basic
    @Column(name = "away_coach_discord_id1")
    @JsonProperty("away_coach_discord_id1")
    var awayCoachDiscordId1: String? = null

    @Basic
    @Column(name = "away_coach_discord_id2")
    @JsonProperty("away_coach_discord_id2")
    var awayCoachDiscordId2: String? = null

    @Basic
    @Column(name = "home_offensive_playbook")
    @JsonProperty("home_offensive_playbook")
    lateinit var homeOffensivePlaybook: OffensivePlaybook

    @Basic
    @Column(name = "away_offensive_playbook")
    @JsonProperty("away_offensive_playbook")
    lateinit var awayOffensivePlaybook: OffensivePlaybook

    @Basic
    @Column(name = "home_defensive_playbook")
    @JsonProperty("home_defensive_playbook")
    lateinit var homeDefensivePlaybook: DefensivePlaybook

    @Basic
    @Column(name = "away_defensive_playbook")
    @JsonProperty("away_defensive_playbook")
    lateinit var awayDefensivePlaybook: DefensivePlaybook

    @Basic
    @Column(name = "home_score")
    @JsonProperty("home_score")
    var homeScore: Int? = 0

    @Basic
    @Column(name = "away_score")
    @JsonProperty("away_score")
    var awayScore: Int? = 0

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "possession")
    @JsonProperty("possession")
    var possession: TeamSide? = null

    @Basic
    @Column(name = "quarter")
    @JsonProperty("quarter")
    var quarter: Int? = 1

    @Basic
    @Column(name = "clock")
    @JsonProperty("clock")
    var clock: String? = "7:00"

    @Basic
    @Column(name = "ball_location")
    @JsonProperty("ball_location")
    var ballLocation: Int? = null

    @Basic
    @Column(name = "down")
    @JsonProperty("down")
    var down: Int? = 1

    @Basic
    @Column(name = "yards_to_go")
    @JsonProperty("yards_to_go")
    var yardsToGo: Int? = 10

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "tv_channel")
    @JsonProperty("tv_channel")
    var tvChannel: TVChannel? = null

    @Basic
    @Column(name = "start_time")
    @JsonProperty("start_time")
    var startTime: String? = null

    @Basic
    @Column(name = "location")
    @JsonProperty("location")
    var location: String? = null

    @Basic
    @Column(name = "home_wins")
    @JsonProperty("home_wins")
    var homeWins: Int? = null

    @Basic
    @Column(name = "home_losses")
    @JsonProperty("home_losses")
    var homeLosses: Int? = null

    @Basic
    @Column(name = "away_wins")
    @JsonProperty("away_wins")
    var awayWins: Int? = null

    @Basic
    @Column(name = "away_losses")
    @JsonProperty("away_losses")
    var awayLosses: Int? = null

    @Basic
    @Column(name = "scorebug")
    @JsonProperty("scorebug")
    var scorebug: String? = null

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "subdivision")
    @JsonProperty("subdivision")
    var subdivision: Subdivision? = null

    @Basic
    @Column(name = "timestamp")
    @JsonProperty("timestamp")
    var timestamp: String? = null

    @Basic
    @Column(name = "win_probability")
    @JsonProperty("win_probability")
    var winProbability: Double? = null

    @Basic
    @Column(name = "season")
    @JsonProperty("season")
    var season: Int? = null

    @Basic
    @Column(name = "week")
    @JsonProperty("week")
    var week: Int? = null

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "waiting_on")
    @JsonProperty("waiting_on")
    var waitingOn: TeamSide? = TeamSide.AWAY

    @Basic
    @Column(name = "win_probability_plot")
    @JsonProperty("win_probability_plot")
    var winProbabilityPlot: String? = null

    @Basic
    @Column(name = "score_plot")
    @JsonProperty("score_plot")
    var scorePlot: String? = null

    @Basic
    @Column(name = "num_plays")
    @JsonProperty("num_plays")
    var numPlays: Int? = 0

    @Basic
    @Column(name = "home_timeouts")
    @JsonProperty("home_timeouts")
    var homeTimeouts: Int? = 3

    @Basic
    @Column(name = "away_timeouts")
    @JsonProperty("away_timeouts")
    var awayTimeouts: Int? = 3

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "coin_toss_winner")
    @JsonProperty("coin_toss_winner")
    var coinTossWinner: TeamSide? = null

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "coin_toss_choice")
    @JsonProperty("coin_toss_choice")
    var coinTossChoice: CoinTossChoice? = null

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "home_platform")
    @JsonProperty("home_platform")
    lateinit var homePlatform: Platform

    @Basic
    @Column(name = "home_platform_id")
    @JsonProperty("home_platform_id")
    var homePlatformId: String? = null

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "away_platform")
    @JsonProperty("away_platform")
    lateinit var awayPlatform: Platform

    @Basic
    @Column(name = "away_platform_id")
    @JsonProperty("away_platform_id")
    var awayPlatformId: String? = null

    @Basic
    @Column(name = "game_timer")
    @JsonProperty("game_timer")
    var gameTimer: String? = null

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "current_play_type")
    @JsonProperty("current_play_type")
    var currentPlayType: PlayType? = null

    @Basic
    @Column(name = "current_play_id")
    @JsonProperty("current_play_id")
    var currentPlayId: Int? = null

    @Basic
    @Column(name = "clock_stopped")
    @JsonProperty("clock_stopped")
    var clockStopped: Boolean? = null

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "game_status")
    @JsonProperty("game_status")
    var gameStatus: GameStatus? = null

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "game_type")
    @JsonProperty("game_type")
    var gameType: GameType? = null

    constructor(
        homeTeam: String,
        awayTeam: String,
        homeCoach1: String,
        homeCoach2: String?,
        awayCoach1: String,
        awayCoach2: String?,
        homeCoachDiscordId1: String,
        homeCoachDiscordId2: String?,
        awayCoachDiscordId1: String,
        awayCoachDiscordId2: String?,
        homeOffensivePlaybook: OffensivePlaybook,
        awayOffensivePlaybook: OffensivePlaybook,
        homeDefensivePlaybook: DefensivePlaybook,
        awayDefensivePlaybook: DefensivePlaybook,
        homeScore: Int,
        awayScore: Int,
        possession: TeamSide?,
        quarter: Int,
        clock: String,
        ballLocation: Int?,
        down: Int,
        yardsToGo: Int,
        tvChannel: TVChannel?,
        startTime: String?,
        location: String?,
        homeWins: Int?,
        homeLosses: Int?,
        awayWins: Int?,
        awayLosses: Int?,
        scorebug: String?,
        subdivision: Subdivision?,
        timestamp: String?,
        winProbability: Double?,
        season: Int?,
        week: Int?,
        waitingOn: TeamSide,
        winProbabilityPlot: String?,
        scorePlot: String?,
        numPlays: Int,
        homeTimeouts: Int,
        awayTimeouts: Int,
        coinTossWinner: TeamSide?,
        coinTossChoice: CoinTossChoice?,
        homePlatform: Platform,
        homePlatformId: String?,
        awayPlatform: Platform,
        awayPlatformId: String?,
        gameTimer: String?,
        currentPlayType: PlayType?,
        currentPlayId: Int?,
        clockStopped: Boolean?,
        gameStatus: GameStatus?,
        gameType: GameType?,
    ) {
        this.homeTeam = homeTeam
        this.awayTeam = awayTeam
        this.homeCoach1 = homeCoach1
        this.homeCoach2 = homeCoach2
        this.awayCoach1 = awayCoach1
        this.awayCoach2 = awayCoach2
        this.homeCoachDiscordId1 = homeCoachDiscordId1
        this.homeCoachDiscordId2 = homeCoachDiscordId2
        this.awayCoachDiscordId1 = awayCoachDiscordId1
        this.awayCoachDiscordId2 = awayCoachDiscordId2
        this.homeOffensivePlaybook = homeOffensivePlaybook
        this.awayOffensivePlaybook = awayOffensivePlaybook
        this.homeDefensivePlaybook = homeDefensivePlaybook
        this.awayDefensivePlaybook = awayDefensivePlaybook
        this.homeScore = homeScore
        this.awayScore = awayScore
        this.possession = possession
        this.quarter = quarter
        this.clock = clock
        this.ballLocation = ballLocation
        this.down = down
        this.yardsToGo = yardsToGo
        this.tvChannel = tvChannel
        this.startTime = startTime
        this.location = location
        this.homeWins = homeWins
        this.homeLosses = homeLosses
        this.awayWins = awayWins
        this.awayLosses = awayLosses
        this.scorebug = scorebug
        this.subdivision = subdivision
        this.timestamp = timestamp
        this.winProbability = winProbability
        this.season = season
        this.week = week
        this.waitingOn = waitingOn
        this.winProbabilityPlot = winProbabilityPlot
        this.scorePlot = scorePlot
        this.numPlays = numPlays
        this.homeTimeouts = homeTimeouts
        this.awayTimeouts = awayTimeouts
        this.coinTossWinner = coinTossWinner
        this.coinTossChoice = coinTossChoice
        this.homePlatform = homePlatform
        this.homePlatformId = homePlatformId
        this.awayPlatform = awayPlatform
        this.awayPlatformId = awayPlatformId
        this.gameTimer = gameTimer
        this.currentPlayType = currentPlayType
        this.currentPlayId = currentPlayId
        this.clockStopped = clockStopped
        this.gameStatus = gameStatus
        this.gameType = gameType
    }

    constructor()

    enum class GameStatus(val description: String) {
        PREGAME("PREGAME"),
        OPENING_KICKOFF("OPENING KICKOFF"),
        IN_PROGRESS("IN PROGRESS"),
        HALFTIME("HALFTIME"),
        FINAL("FINAL"),
        END_OF_REGULATION("END OF REGULATION"),
        OVERTIME("OVERTIME"),
    }

    enum class Subdivision(val description: String) {
        FCFB("FCFB"),
        FBS("FBS"),
        FCS("FCS"),
        ;

        companion object {
            fun fromString(description: String): Subdivision? {
                return entries.find { it.description == description }
            }
        }
    }

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

    enum class DefensivePlaybook(val description: String) {
        FOUR_THREE("4-3"),
        THREE_FOUR("3-4"),
        FIVE_TWO("5-2"),
        FOUR_FOUR("4-4"),
        THREE_THREE_FIVE("3-3-5"),
        ;

        companion object {
            fun fromString(description: String): DefensivePlaybook? {
                return entries.find { it.description == description }
            }
        }
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

    enum class PlayCall(val description: String) {
        RUN("RUN"),
        PASS("PASS"),
        SPIKE("SPIKE"),
        KNEEL("KNEEL"),
        FIELD_GOAL("FIELD_GOAL"),
        PAT("PAT"),
        TWO_POINT("TWO_POINT"),
        KICKOFF_NORMAL("KICKOFF_NORMAL"),
        KICKOFF_ONSIDE("KICKOFF_ONSIDE"),
        KICKOFF_SQUIB("KICKOFF_SQUIB"),
        PUNT("PUNT"),
    }

    enum class PlayType(val description: String) {
        NORMAL("NORMAL"),
        KICKOFF("KICKOFF"),
        PAT("PAT"),
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
        BLOCKED("BLOCKED"),
        KICK_SIX("KICK SIX"),
        DEFENSE_TWO_POINT("DEFENSE TWO POINT"),
        SUCCESS("SUCCESS"),
        FAILED("NO FAILED"),
        SPIKE("SPIKE"),
        KNEEL("KNEEL"),
        PUNT("PUNT"),
        PUNT_RETURN_TOUCHDOWN("PUNT RETURN TOUCHDOWN"),
        PUNT_TEAM_TOUCHDOWN("PUNT TEAM TOUCHDOWN"),
        MUFFED_PUNT("MUFFED PUNT"),
    }

    enum class RunoffType(val description: String) {
        CHEW("CHEW"),
        HURRY("HURRY"),
        NORMAL("NORMAL"),
    }

    enum class Scenario(val description: String) {
        GAME_START("GAME START"),
        PLAY_RESULT("PLAY RESULT"),
        COIN_TOSS("COIN_TOSS"),
        COIN_TOSS_CHOICE("COIN TOSS CHOICE"),
        KICKOFF_NUMBER_REQUEST("KICKOFF NUMBER REQUEST"),
        NORMAL_NUMBER_REQUEST("NORMAL NUMBER REQUEST"),
        DM_NUMBER_REQUEST("DM NUMBER REQUEST"),
        GOOD("GOOD"),
        NO_GOOD("NO GOOD"),
        SUCCESS("SUCCESS"),
        FAILED("FAILED"),
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
        TURNOVER_PLUS_20_YARDS("TO + 20 YARDS"),
        TURNOVER_PLUS_15_YARDS("TO + 15 YARDS"),
        TURNOVER_PLUS_10_YARDS("TO + 10 YARDS"),
        TURNOVER_PLUS_5_YARDS("TO + 5 YARDS"),
        TURNOVER("TO"),
        TURNOVER_MINUS_5_YARDS("TO - 5 YARDS"),
        TURNOVER_MINUS_10_YARDS("TO - 10 YARDS"),
        TURNOVER_MINUS_15_YARDS("TO - 15 YARDS"),
        TURNOVER_MINUS_20_YARDS("TO - 20 YARDS"),
        TURNOVER_ON_DOWNS("TURNOVER ON DOWNS"),
        TURNOVER_TOUCHDOWN("TURNOVER TOUCHDOWN"),
        TOUCHDOWN("TOUCHDOWN"),
        SAFETY("SAFETY"),
        FUMBLE("FUMBLE"),
        FIVE_YARD_RETURN("5 YARD RETURN"),
        TEN_YARD_RETURN("10 YARD RETURN"),
        TWENTY_YARD_RETURN("20 YARD RETURN"),
        THIRTY_YARD_RETURN("30 YARD RETURN"),
        THIRTY_FIVE_YARD_RETURN("35 YARD RETURN"),
        FORTY_YARD_RETURN("40 YARD RETURN"),
        FORTY_FIVE_YARD_RETURN("45 YARD RETURN"),
        FIFTY_YARD_RETURN("50 YARD RETURN"),
        SIXTY_FIVE_YARD_RETURN("65 YARD RETURN"),
        TOUCHBACK("TOUCHBACK"),
        RETURN_TOUCHDOWN("RETURN TOUCHDOWN"),
        RECOVERED("RECOVERED"),
        DEFENSE_TWO_POINT("DEFENSE TWO POINT"),
        SPIKE("SPIKE"),
        KNEEL("KNEEL"),
        BLOCKED_PUNT("BLOCKED PUNT"),
        PUNT_RETURN_TOUCHDOWN("PUNT RETURN TOUCHDOWN"),
        BLOCKED_FIELD_GOAL("BLOCKED FIELD GOAL"),
        KICK_SIX("KICK SIX"),
        FIVE_YARD_PUNT("5 YARD PUNT"),
        TEN_YARD_PUNT("10 YARD PUNT"),
        FIFTEEN_YARD_PUNT("15 YARD PUNT"),
        TWENTY_YARD_PUNT("20 YARD PUNT"),
        TWENTY_FIVE_YARD_PUNT("25 YARD PUNT"),
        THIRTY_YARD_PUNT("30 YARD PUNT"),
        THIRTY_FIVE_YARD_PUNT("35 YARD PUNT"),
        FORTY_YARD_PUNT("40 YARD PUNT"),
        FORTY_FIVE_YARD_PUNT("45 YARD PUNT"),
        FIFTY_YARD_PUNT("50 YARD PUNT"),
        FIFTY_FIVE_YARD_PUNT("55 YARD PUNT"),
        SIXTY_YARD_PUNT("60 YARD PUNT"),
        SIXTY_FIVE_YARD_PUNT("65 YARD PUNT"),
        SEVENTY_YARD_PUNT("70 YARD PUNT"),
        ;

        companion object {
            fun fromString(description: String): Scenario? {
                return Scenario.entries.find { it.description == description }
            }
        }
    }

    enum class TeamSide(val description: String) {
        HOME("home"),
        AWAY("away"),
    }

    enum class CoinTossChoice(val description: String) {
        RECEIVE("receive"),
        DEFER("defer"),
    }

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
            fun fromString(description: String): GameType? {
                return GameType.values().find { it.description == description }
            }
        }
    }
}
