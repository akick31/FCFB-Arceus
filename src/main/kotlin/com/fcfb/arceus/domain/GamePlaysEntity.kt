package com.fcfb.arceus.domain

import java.util.*
import javax.persistence.*
import com.fcfb.arceus.models.game.Game.Play
import com.fcfb.arceus.models.game.Game.Possession
import com.fcfb.arceus.models.game.Game.Result
import com.fcfb.arceus.models.game.Game.ActualResult

@Entity
@Table(name = "game_plays", schema = "arceus")
class GamePlaysEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "play_id")
    var playId: Int? = 0

    @Basic
    @Column(name = "game_id")
    var gameId: Int? = 0

    @Basic
    @Column(name = "play_number")
    var playNumber: Int? = 0

    @Basic
    @Column(name = "home_score")
    var homeScore: Int? = null

    @Basic
    @Column(name = "away_score")
    var awayScore: Int? = null

    @Basic
    @Column(name = "game_quarter")
    var gameQuarter: Int? = null

    @Basic
    @Column(name = "clock")
    var clock: Int? = null

    @Basic
    @Column(name = "ball_location")
    var ballLocation: Int? = null

    @Basic
    @Column(name = "possession")
    var possession: Possession? = null

    @Basic
    @Column(name = "down")
    var down: Int? = null

    @Basic
    @Column(name = "yards_to_go")
    var yardsToGo: Int? = null

    @Basic
    @Column(name = "defensive_number")
    var defensiveNumber: String? = null

    @Basic
    @Column(name = "offensive_number")
    var offensiveNumber: String? = null

    @Basic
    @Column(name = "defensive_submitter")
    var defensiveSubmitter: String? = null

    @Basic
    @Column(name = "offensive_submitter")
    var offensiveSubmitter: String? = null

    @Basic
    @Column(name = "play")
    var play: Play? = null

    @Basic
    @Column(name = "result")
    var result: Result? = null

    @Basic
    @Column(name = "difference")
    var difference: Int? = null

    @Basic
    @Column(name = "actual_result")
    var actualResult: ActualResult? = null

    @Basic
    @Column(name = "yards")
    var yards: Int? = null

    @Basic
    @Column(name = "play_time")
    var playTime: Int? = null

    @Basic
    @Column(name = "runoff_time")
    var runoffTime: Int? = null

    @Basic
    @Column(name = "win_probability")
    var winProbability: Double? = null

    @Basic
    @Column(name = "home_team")
    var homeTeam: String? = null

    @Basic
    @Column(name = "away_team")
    var awayTeam: String? = null

    @Basic
    @Column(name = "timeout_used")
    var timeoutUsed: Boolean? = null

    @Basic
    @Column(name = "home_timeouts")
    var homeTimeouts: Int? = null

    @Basic
    @Column(name = "away_timeouts")
    var awayTimeouts: Int? = null

    @Basic
    @Column(name = "play_finished")
    var playFinished: Boolean? = null

    constructor(
        gameId: Int, playNumber: Int, homeScore: Int?, awayScore: Int?, quarter: Int?,
        clock: Int?, ballLocation: Int?, possession: Possession?, down: Int?, yardsToGo: Int?,
        defensiveNumber: String?, offensiveNumber: String?, offensiveSubmitter: String?,
        defensiveSubmitter: String?, play: Play?, result: Result?, actualResult: ActualResult?, yards: Int,
        playTime: Int, runoffTime: Int, winProbability: Double?, homeTeam: String?, awayTeam: String?,
        difference: Int, timeoutUsed: Boolean?, homeTimeouts: Int, awayTimeouts: Int,
        playFinished: Boolean?
    ) {
        this.gameId = gameId
        this.playNumber = playNumber
        this.homeScore = homeScore
        this.awayScore = awayScore
        gameQuarter = quarter
        this.clock = clock
        this.ballLocation = ballLocation
        this.possession = possession
        this.down = down
        this.yardsToGo = yardsToGo
        this.defensiveNumber = defensiveNumber
        this.offensiveNumber = offensiveNumber
        this.offensiveSubmitter = offensiveSubmitter
        this.defensiveSubmitter = defensiveSubmitter
        this.play = play
        this.result = result
        this.actualResult = actualResult
        this.yards = yards
        this.playTime = playTime
        this.runoffTime = runoffTime
        this.winProbability = winProbability
        this.homeTeam = homeTeam
        this.awayTeam = awayTeam
        this.difference = difference
        this.timeoutUsed = timeoutUsed
        this.homeTimeouts = homeTimeouts
        this.awayTimeouts = awayTimeouts
        this.playFinished = playFinished
    }

    constructor()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as GamePlaysEntity
        return playNumber == that.playNumber && gameId == that.gameId && homeScore == that.homeScore && awayScore == that.awayScore && gameQuarter == that.gameQuarter && clock == that.clock && ballLocation == that.ballLocation && possession == that.possession && down == that.down && yardsToGo == that.yardsToGo && defensiveNumber == that.defensiveNumber && offensiveNumber == that.offensiveNumber && defensiveSubmitter == that.defensiveSubmitter && offensiveSubmitter == that.offensiveSubmitter && play == that.play && result == that.result && difference == that.difference && actualResult == that.actualResult && yards == that.yards && playTime == that.playTime && runoffTime == that.runoffTime && winProbability == that.winProbability && homeTeam == that.homeTeam && awayTeam == that.awayTeam && playId == that.playId && timeoutUsed == that.timeoutUsed && homeTimeouts == that.homeTimeouts && awayTimeouts == that.awayTimeouts && playFinished == that.playFinished
    }

    override fun hashCode(): Int {
        return Objects.hash(
            gameId,
            playNumber,
            homeScore,
            awayScore,
            gameQuarter,
            clock,
            ballLocation,
            possession,
            down,
            yardsToGo,
            defensiveNumber,
            offensiveNumber,
            defensiveSubmitter,
            offensiveSubmitter,
            play,
            result,
            difference,
            actualResult,
            yards,
            playTime,
            runoffTime,
            winProbability,
            homeTeam,
            awayTeam,
            playId,
            timeoutUsed,
            homeTimeouts,
            awayTimeouts,
            playFinished
        )
    }

    override fun toString(): String {
        return """{
  "gameId": "$gameId",
  "playNumber": "$playNumber",
  "homeScore": "$homeScore",
  "awayScore": "$awayScore",
  "quarter": "$gameQuarter",
  "clock": "$clock",
  "ballLocation": "$ballLocation",
  "possession": "$possession",
  "down": "$down",
  "yardsToGo": "$yardsToGo",
  "defensiveNumber": "$defensiveNumber",
  "offensiveNumber": "$offensiveNumber",
  "offensiveSubmitter": "$offensiveSubmitter",
  "defensiveSubmitter": "$defensiveSubmitter",
  "play": "$play",
  "result": "$result",
  "actualResult": "$actualResult",
  "yards": "$yards",
  "playTime": "$playTime",
  "runoffTime": "$runoffTime",
  "winProbability": "$winProbability",
  "homeTeam": "$homeTeam",
  "awayTeam": "$awayTeam",
  "difference": "$difference",
  "timeoutUsed": "$timeoutUsed",
  "homeTimeouts": "$homeTimeouts",
  "awayTimeouts": "$awayTimeouts",
  "playFinished": "$playFinished"
}"""
    }
}
