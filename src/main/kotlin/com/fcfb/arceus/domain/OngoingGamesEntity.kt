package com.fcfb.arceus.domain

import java.time.LocalDateTime
import java.util.*
import jakarta.persistence.*

@Entity
@Table(name = "ongoing_games", schema = "arceus")
class OngoingGamesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "game_id")
    var gameId = 0

    @Basic
    @Column(name = "home_team")
    var homeTeam: String = "home team"

    @Basic
    @Column(name = "away_team")
    var awayTeam: String = "away team"

    @Basic
    @Column(name = "home_coach")
    var homeCoach: String = "home coach"

    @Basic
    @Column(name = "away_coach")
    var awayCoach: String = "away coach"

    @Basic
    @Column(name = "home_offensive_playbook")
    var homeOffensivePlaybook: String = "home offensive playbook"

    @Basic
    @Column(name = "away_offensive_playbook")
    var awayOffensivePlaybook: String = "away offensive playbook"

    @Basic
    @Column(name = "home_defensive_playbook")
    var homeDefensivePlaybook: String = "home defensive playbook"

    @Basic
    @Column(name = "away_defensive_playbook")
    var awayDefensivePlaybook: String = "away defensive playbook"

    @Basic
    @Column(name = "home_score")
    var homeScore: Int = 0

    @Basic
    @Column(name = "away_score")
    var awayScore: Int = 0

    @Basic
    @Column(name = "possession")
    var possession: String? = null

    @Basic
    @Column(name = "quarter")
    var quarter: Int = 1

    @Basic
    @Column(name = "clock")
    var clock: String = "7:00"

    @Basic
    @Column(name = "ball_location")
    var ballLocation: Int? = null

    @Basic
    @Column(name = "down")
    var down: Int = 1

    @Basic
    @Column(name = "yards_to_go")
    var yardsToGo: Int = 10

    @Basic
    @Column(name = "tv_channel")
    var tvChannel: String? = null

    @Basic
    @Column(name = "start_time")
    var startTime: String? = null

    @Basic
    @Column(name = "location")
    var location: String? = null

    @Basic
    @Column(name = "home_wins")
    var homeWins: Int? = null

    @Basic
    @Column(name = "home_losses")
    var homeLosses: Int? = null

    @Basic
    @Column(name = "away_wins")
    var awayWins: Int? = null

    @Basic
    @Column(name = "away_losses")
    var awayLosses: Int? = null

    @Basic
    @Column(name = "scorebug")
    var scorebug: String? = null

    @Basic
    @Column(name = "subdivision")
    var subdivision: String? = null

    @Basic
    @Column(name = "timestamp")
    var timestamp: LocalDateTime? = null

    @Basic
    @Column(name = "win_probability")
    var winProbability: Double? = null

    @Basic
    @Column(name = "is_final")
    var isFinal: Boolean = false

    @Basic
    @Column(name = "is_ot")
    var isOT: Boolean = false

    @Basic
    @Column(name = "season")
    var season: Int? = null

    @Basic
    @Column(name = "week")
    var week: Int? = null

    @Basic
    @Column(name = "waiting_on")
    var waitingOn: String = "away"

    @Basic
    @Column(name = "win_probability_plot")
    var winProbabilityPlot: String? = null

    @Basic
    @Column(name = "score_plot")
    var scorePlot: String? = null

    @Basic
    @Column(name = "num_plays")
    var numPlays: Int = 0

    @Basic
    @Column(name = "home_timeouts")
    var homeTimeouts: Int = 3

    @Basic
    @Column(name = "away_timeouts")
    var awayTimeouts: Int = 3

    @Basic
    @Column(name = "coin_toss_winner")
    var coinTossWinner: String? = null

    @Basic
    @Column(name = "coin_toss_choice")
    var coinTossChoice: String? = null

    @Basic
    @Column(name = "home_platform")
    var homePlatform: String? = null

    @Basic
    @Column(name = "home_platform_id")
    var homePlatformId: String? = null

    @Basic
    @Column(name = "away_platform")
    var awayPlatform: String? = null

    @Basic
    @Column(name = "away_platform_id")
    private var awayPlatformId: String? = null

    @Basic
    @Column(name = "game_timer")
    var gameTimer: String? = null

    @Basic
    @Column(name = "current_play_type")
    var currentPlayType: String? = null

    @Basic
    @Column(name = "current_play_id")
    var currentPlayId: Int? = null

    @Basic
    @Column(name = "is_scrimmage")
    var isScrimmage: Boolean? = null

    @Basic
    @Column(name = "clock_stopped")
    var clockStopped: Boolean? = null

    constructor(
        homeTeam: String, awayTeam: String, homeCoach: String, awayCoach: String,
        homeOffensivePlaybook: String, awayOffensivePlaybook: String, homeDefensivePlaybook: String,
        awayDefensivePlaybook: String, homeScore: Int, awayScore: Int, possession: String?,
        quarter: Int, clock: String, ballLocation: Int?, down: Int, yardsToGo: Int,
        tvChannel: String?, startTime: String?, location: String?, homeWins: Int?, homeLosses: Int?,
        awayWins: Int?, awayLosses: Int?, scorebug: String?, subdivision: String?,
        timestamp: LocalDateTime?, winProbability: Double?, isFinal: Boolean, isOT: Boolean,
        season: Int?, week: Int?, waitingOn: String, winProbabilityPlot: String?, scorePlot: String?,
        numPlays: Int, homeTimeouts: Int, awayTimeouts: Int, coinTossWinner: String?,
        coinTossChoice: String?, homePlatform: String?, homePlatformId: String?, awayPlatform: String?,
        awayPlatformId: String?, gameTimer: String?, currentPlayType: String?, currentPlayId: Int?,
        isScrimmage: Boolean?, clockStopped: Boolean?
    ) {
        this.homeTeam = homeTeam
        this.awayTeam = awayTeam
        this.homeCoach = homeCoach
        this.awayCoach = awayCoach
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
        this.isFinal = isFinal
        this.isOT = isOT
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
        this.isScrimmage = isScrimmage
        this.clockStopped = clockStopped
    }

    constructor()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as OngoingGamesEntity
        return gameId == that.gameId && homeTeam == that.homeTeam && awayTeam == that.awayTeam && homeCoach == that.homeCoach && awayCoach == that.awayCoach && homeOffensivePlaybook == that.homeOffensivePlaybook && awayOffensivePlaybook == that.awayOffensivePlaybook && homeDefensivePlaybook == that.homeDefensivePlaybook && awayDefensivePlaybook == that.awayDefensivePlaybook && homeScore == that.homeScore && awayScore == that.awayScore && possession == that.possession && quarter == that.quarter && clock == that.clock && ballLocation == that.ballLocation && down == that.down && yardsToGo == that.yardsToGo && tvChannel == that.tvChannel && startTime == that.startTime && location == that.location && homeWins == that.homeWins && homeLosses == that.homeLosses && awayWins == that.awayWins && awayLosses == that.awayLosses && scorebug == that.scorebug && subdivision == that.subdivision && timestamp == that.timestamp && winProbability == that.winProbability && isFinal == that.isFinal && isOT == that.isOT && season == that.season && week == that.week && waitingOn == that.waitingOn && winProbabilityPlot == that.winProbabilityPlot && scorePlot == that.scorePlot && numPlays == that.numPlays && homeTimeouts == that.homeTimeouts && awayTimeouts == that.awayTimeouts && coinTossWinner == that.coinTossWinner && coinTossChoice == that.coinTossChoice && homePlatform == that.homePlatform && homePlatformId == that.homePlatformId && awayPlatform == that.awayPlatform && awayPlatformId == that.awayPlatformId && gameTimer == that.gameTimer && currentPlayType == that.currentPlayType && currentPlayId == that.currentPlayId && isScrimmage == that.isScrimmage && clockStopped == that.clockStopped
    }

    override fun hashCode(): Int {
        return Objects.hash(
            gameId,
            homeTeam,
            awayTeam,
            homeCoach,
            awayCoach,
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
            isFinal,
            isOT,
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
            isScrimmage,
            clockStopped
        )
    }

    override fun toString(): String {
        return """{
          "gameId": "$gameId",
          "homeTeam": "$homeTeam",
          "awayTeam": "$awayTeam",
          "homeCoach": "$homeCoach",
          "awayCoach": "$awayCoach",
          "homeOffensivePlaybook": "$homeOffensivePlaybook",
          "awayOffensivePlaybook": "$awayOffensivePlaybook",
          "homeDefensivePlaybook": "$homeDefensivePlaybook",
          "awayDefensivePlaybook": "$awayDefensivePlaybook",
          "homeScore": "$homeScore",
          "awayScore": "$awayScore",
          "possession": "$possession",
          "quarter": "$quarter",
          "clock": "$clock",
          "ballLocation": "$ballLocation",
          "down": "$down",
          "yardsToGo": "$yardsToGo",
          "tvChannel": "$tvChannel",
          "startTime": "$startTime",
          "location": "$location",
          "homeWins": "$homeWins",
          "homeLosses": "$homeLosses",
          "awayWins": "$awayWins",
          "awayLosses": "$awayLosses",
          "scorebug": "$scorebug",
          "subdivision": "$subdivision",
          "timestamp": "$timestamp",
          "winProbability": "$winProbability",
          "isFinal": "$isFinal",
          "isOT": "$isOT",
          "season": "$season",
          "week": "$week",
          "waitingOn": "$waitingOn",
          "winProbabilityPlot": "$winProbabilityPlot",
          "scorePlot": "$scorePlot",
          "numPlays": "$numPlays",
          "homeTimeouts": "$homeTimeouts",
          "awayTimeouts": "$awayTimeouts",
          "coinTossWinner": "$coinTossWinner",
          "coinTossChoice": "$coinTossChoice",
          "homePlatform": "$homePlatform",
          "homePlatformId": "$homePlatformId",
          "awayPlatform": "$awayPlatform",
          "awayPlatformId": "$awayPlatformId",
          "gameTimer": "$gameTimer",
          "currentPlayType": "$currentPlayType",
          "currentPlayId": "$currentPlayId",
          "isScrimmage": "$isScrimmage",
          "clockStopped": "$clockStopped"
        }"""
    }
}
