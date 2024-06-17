package com.fcfb.arceus.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fcfb.arceus.models.game.Game.CoinTossWinner
import com.fcfb.arceus.models.game.Game.CoinTossChoice
import com.fcfb.arceus.models.game.Game.OffensivePlaybook
import com.fcfb.arceus.models.game.Game.DefensivePlaybook
import com.fcfb.arceus.models.game.Game.Possession
import com.fcfb.arceus.models.game.Game.PlayType
import com.fcfb.arceus.models.game.Game.TVChannel
import com.fcfb.arceus.models.game.Game.Platform
import com.fcfb.arceus.models.game.Game.Subdivision
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "ongoing_games", schema = "arceus")
class OngoingGamesEntity {
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

    @Basic
    @Column(name = "possession")
    @JsonProperty("possession")
    var possession: Possession? = null

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

    @Basic
    @Column(name = "subdivision")
    @JsonProperty("subdivision")
    lateinit var subdivision: Subdivision

    @Basic
    @Column(name = "timestamp")
    @JsonProperty("timestamp")
    var timestamp: LocalDateTime? = null

    @Basic
    @Column(name = "win_probability")
    @JsonProperty("win_probability")
    var winProbability: Double? = null

    @Basic
    @Column(name = "final")
    @JsonProperty("final")
    var final: Boolean? = false

    @Basic
    @Column(name = "ot")
    @JsonProperty("ot")
    var ot: Boolean? = false

    @Basic
    @Column(name = "season")
    @JsonProperty("season")
    var season: Int? = null

    @Basic
    @Column(name = "week")
    @JsonProperty("week")
    var week: Int? = null

    @Basic
    @Column(name = "waiting_on")
    @JsonProperty("waiting_on")
    var waitingOn: String? = "away"

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

    @Basic
    @Column(name = "coin_toss_winner")
    @JsonProperty("coin_toss_winner")
    var coinTossWinner: CoinTossWinner? = null

    @Basic
    @Column(name = "coin_toss_choice")
    @JsonProperty("coin_toss_choice")
    var coinTossChoice: CoinTossChoice? = null

    @Basic
    @Column(name = "home_platform")
    @JsonProperty("home_platform")
    lateinit var homePlatform: Platform

    @Basic
    @Column(name = "home_platform_id")
    @JsonProperty("home_platform_id")
    var homePlatformId: String? = null

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

    constructor(
        homeTeam: String, awayTeam: String, homeCoach: String, awayCoach: String, homeCoachDiscordId: String,
        awayCoachDiscordId: String, homeOffensivePlaybook: OffensivePlaybook, awayOffensivePlaybook: OffensivePlaybook,
        homeDefensivePlaybook: DefensivePlaybook, awayDefensivePlaybook: DefensivePlaybook, homeScore: Int, awayScore: Int,
        possession: Possession?, quarter: Int, clock: String, ballLocation: Int?, down: Int, yardsToGo: Int,
        tvChannel: TVChannel?, startTime: String?, location: String?, homeWins: Int?, homeLosses: Int?,
        awayWins: Int?, awayLosses: Int?, scorebug: String?, subdivision: Subdivision,
        timestamp: LocalDateTime?, winProbability: Double?, final: Boolean, ot: Boolean,
        season: Int?, week: Int?, waitingOn: String, winProbabilityPlot: String?, scorePlot: String?,
        numPlays: Int, homeTimeouts: Int, awayTimeouts: Int, coinTossWinner: CoinTossWinner?,
        coinTossChoice: CoinTossChoice?, homePlatform: Platform, homePlatformId: String?, awayPlatform: Platform,
        awayPlatformId: String?, gameTimer: String?, currentPlayType: PlayType?, currentPlayId: Int?,
        scrimmage: Boolean?, clockStopped: Boolean?
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
        this.final = final
        this.ot = ot
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
    }

    constructor()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as OngoingGamesEntity
        return gameId == that.gameId && homeTeam == that.homeTeam && awayTeam == that.awayTeam && homeCoach == that.homeCoach && awayCoach == that.awayCoach && homeCoachDiscordId == that.homeCoachDiscordId && awayCoachDiscordId == that.awayCoachDiscordId && homeOffensivePlaybook == that.homeOffensivePlaybook && awayOffensivePlaybook == that.awayOffensivePlaybook && homeDefensivePlaybook == that.homeDefensivePlaybook && awayDefensivePlaybook == that.awayDefensivePlaybook && homeScore == that.homeScore && awayScore == that.awayScore && possession == that.possession && quarter == that.quarter && clock == that.clock && ballLocation == that.ballLocation && down == that.down && yardsToGo == that.yardsToGo && tvChannel == that.tvChannel && startTime == that.startTime && location == that.location && homeWins == that.homeWins && homeLosses == that.homeLosses && awayWins == that.awayWins && awayLosses == that.awayLosses && scorebug == that.scorebug && subdivision == that.subdivision && timestamp == that.timestamp && winProbability == that.winProbability && final == that.final && ot == that.ot && season == that.season && week == that.week && waitingOn == that.waitingOn && winProbabilityPlot == that.winProbabilityPlot && scorePlot == that.scorePlot && numPlays == that.numPlays && homeTimeouts == that.homeTimeouts && awayTimeouts == that.awayTimeouts && coinTossWinner == that.coinTossWinner && coinTossChoice == that.coinTossChoice && homePlatform == that.homePlatform && homePlatformId == that.homePlatformId && awayPlatform == that.awayPlatform && awayPlatformId == that.awayPlatformId && gameTimer == that.gameTimer && currentPlayType == that.currentPlayType && currentPlayId == that.currentPlayId && scrimmage == that.scrimmage && clockStopped == that.clockStopped
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
            final,
            ot,
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
            clockStopped
        )
    }
}
