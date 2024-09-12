package com.fcfb.arceus.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Objects
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
    @Column(name = "home_coach")
    @JsonProperty("home_coach")
    var homeCoach: String? = "home coach"

    @Basic
    @Column(name = "away_coach")
    @JsonProperty("away_coach")
    var awayCoach: String? = "away coach"

    @Basic
    @Column(name = "home_coach_discord_id")
    @JsonProperty("home_coach_discord_id")
    var homeCoachDiscordId: String? = null

    @Basic
    @Column(name = "away_coach_discord_id")
    @JsonProperty("away_coach_discord_id")
    var awayCoachDiscordId: String? = null

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "home_offensive_playbook")
    @JsonProperty("home_offensive_playbook")
    lateinit var homeOffensivePlaybook: OffensivePlaybook

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "away_offensive_playbook")
    @JsonProperty("away_offensive_playbook")
    lateinit var awayOffensivePlaybook: OffensivePlaybook

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "home_defensive_playbook")
    @JsonProperty("home_defensive_playbook")
    lateinit var homeDefensivePlaybook: DefensivePlaybook

    @Enumerated(EnumType.STRING)
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
    lateinit var subdivision: Subdivision

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
    @Column(name = "scrimmage")
    @JsonProperty("scrimmage")
    var scrimmage: Boolean? = null

    @Basic
    @Column(name = "clock_stopped")
    @JsonProperty("clock_stopped")
    var clockStopped: Boolean? = null

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "game_status")
    @JsonProperty("game_status")
    var gameStatus: GameStatus? = null

    constructor(
        homeTeam: String,
        awayTeam: String,
        homeCoach: String,
        awayCoach: String,
        homeCoachDiscordId: String,
        awayCoachDiscordId: String,
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
        subdivision: Subdivision,
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
        scrimmage: Boolean?,
        clockStopped: Boolean?,
        gameStatus: GameStatus?
    ) {
        this.homeTeam = homeTeam
        this.awayTeam = awayTeam
        this.homeCoach = homeCoach
        this.awayCoach = awayCoach
        this.homeCoachDiscordId = homeCoachDiscordId
        this.awayCoachDiscordId = awayCoachDiscordId
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
        this.scrimmage = scrimmage
        this.clockStopped = clockStopped
        this.gameStatus = gameStatus
    }

    constructor()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as Game
        return gameId == that.gameId && homeTeam == that.homeTeam && awayTeam == that.awayTeam && homeCoach == that.homeCoach && awayCoach == that.awayCoach && homeCoachDiscordId == that.homeCoachDiscordId && awayCoachDiscordId == that.awayCoachDiscordId && homeOffensivePlaybook == that.homeOffensivePlaybook && awayOffensivePlaybook == that.awayOffensivePlaybook && homeDefensivePlaybook == that.homeDefensivePlaybook && awayDefensivePlaybook == that.awayDefensivePlaybook && homeScore == that.homeScore && awayScore == that.awayScore && possession == that.possession && quarter == that.quarter && clock == that.clock && ballLocation == that.ballLocation && down == that.down && yardsToGo == that.yardsToGo && tvChannel == that.tvChannel && startTime == that.startTime && location == that.location && homeWins == that.homeWins && homeLosses == that.homeLosses && awayWins == that.awayWins && awayLosses == that.awayLosses && scorebug == that.scorebug && subdivision == that.subdivision && timestamp == that.timestamp && winProbability == that.winProbability && season == that.season && week == that.week && waitingOn == that.waitingOn && winProbabilityPlot == that.winProbabilityPlot && scorePlot == that.scorePlot && numPlays == that.numPlays && homeTimeouts == that.homeTimeouts && awayTimeouts == that.awayTimeouts && coinTossWinner == that.coinTossWinner && coinTossChoice == that.coinTossChoice && homePlatform == that.homePlatform && homePlatformId == that.homePlatformId && awayPlatform == that.awayPlatform && awayPlatformId == that.awayPlatformId && gameTimer == that.gameTimer && currentPlayType == that.currentPlayType && currentPlayId == that.currentPlayId && scrimmage == that.scrimmage && clockStopped == that.clockStopped && gameStatus == that.gameStatus
    }

    override fun hashCode(): Int {
        return Objects.hash(
            gameId,
            homeTeam,
            awayTeam,
            homeCoach,
            awayCoach,
            homeCoachDiscordId,
            awayCoachDiscordId,
            homeOffensivePlaybook,
            awayOffensivePlaybook,
            homeDefensivePlaybook,
            awayDefensivePlaybook,
            homeScore,
            awayScore,
            possession,
            quarter,
            clock,
            ballLocation,
            down,
            yardsToGo,
            tvChannel,
            startTime,
            location,
            homeWins,
            homeLosses,
            awayWins,
            awayLosses,
            scorebug,
            subdivision,
            timestamp,
            winProbability,
            season,
            week,
            waitingOn,
            winProbabilityPlot,
            scorePlot,
            numPlays,
            homeTimeouts,
            awayTimeouts,
            coinTossWinner,
            coinTossChoice,
            homePlatform,
            homePlatformId,
            awayPlatform,
            awayPlatformId,
            gameTimer,
            currentPlayType,
            currentPlayId,
            scrimmage,
            clockStopped,
            gameStatus
        )
    }

    enum class GameStatus(val description: String) {
        PREGAME("Pregame"),
        OPENING_KICKOFF("Opening Kickoff"),
        IN_PROGRESS("In Progress"),
        HALFTIME("Halftime"),
        FINAL("Final"),
        END_OF_REGULATION("End of Regulation"),
        OVERTIME("Overtime")
    }

    enum class Subdivision(val description: String) {
        FBS("FBS"),
        FCS("FCS");

        companion object {
            fun fromString(description: String): Subdivision? {
                return entries.find { it.description == description }
            }
        }
    }

    enum class OffensivePlaybook(val description: String) {
        FLEXBONE("Flexbone"),
        AIR_RAID("Air Raid"),
        PRO("Pro"),
        SPREAD("Spread"),
        WEST_COAST("West Coast");

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
        THREE_THREE_FIVE("3-3-5");

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
        DEFENSE_TWO_POINT("DEFENSE TWO POINT");

        companion object {
            fun fromString(description: String): Result? {
                return entries.find { it.description == description }
            }
        }
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
        // TODO: Add kickoffs, punts, field goals, etc
    }

    enum class TeamSide(val description: String) {
        HOME("home"),
        AWAY("away")
    }

    enum class CoinTossChoice(val description: String) {
        RECEIVE("receive"),
        DEFER("defer")
    }

    enum class CoinTossCall(val description: String) {
        HEADS("heads"),
        TAILS("tails");

        companion object {
            fun fromString(description: String): CoinTossCall? {
                return entries.find { it.description == description }
            }
        }
    }
}
