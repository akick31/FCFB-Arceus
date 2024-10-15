package com.fcfb.arceus.domain

import com.fcfb.arceus.domain.Game.ActualResult
import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.Scenario
import com.fcfb.arceus.domain.Game.TeamSide
import java.math.BigInteger
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
@Table(name = "play", schema = "arceus")
class Play {
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
    @Column(name = "quarter")
    var quarter: Int? = null

    @Basic
    @Column(name = "clock")
    var clock: Int? = null

    @Basic
    @Column(name = "ball_location")
    var ballLocation: Int? = null

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "possession")
    var possession: TeamSide? = null

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

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "play_call")
    var playCall: PlayCall? = null

    @Basic
    @Column(name = "result")
    var result: Scenario? = null

    @Basic
    @Column(name = "difference")
    var difference: Int? = null

    @Enumerated(EnumType.STRING)
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
    @Column(name = "win_probability_added")
    var winProbabilityAdded: Double? = null

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
    @Column(name = "offensive_timeout_called")
    var offensiveTimeoutCalled: Boolean? = null

    @Basic
    @Column(name = "defensive_timeout_called")
    var defensiveTimeoutCalled: Boolean? = null

    @Basic
    @Column(name = "home_timeouts")
    var homeTimeouts: Int? = null

    @Basic
    @Column(name = "away_timeouts")
    var awayTimeouts: Int? = null

    @Basic
    @Column(name = "play_finished")
    var playFinished: Boolean? = null

    @Basic
    @Column(name = "offensive_response_speed")
    var offensiveResponseSpeed: BigInteger? = null

    @Basic
    @Column(name = "defensive_response_speed")
    var defensiveResponseSpeed: BigInteger? = null

    constructor(
        gameId: Int,
        playNumber: Int,
        homeScore: Int?,
        awayScore: Int?,
        quarter: Int?,
        clock: Int?,
        ballLocation: Int?,
        possession: TeamSide?,
        down: Int?,
        yardsToGo: Int?,
        defensiveNumber: String?,
        offensiveNumber: String?,
        offensiveSubmitter: String?,
        defensiveSubmitter: String?,
        playCall: PlayCall?,
        result: Scenario?,
        actualResult: ActualResult?,
        yards: Int,
        playTime: Int,
        runoffTime: Int,
        winProbability: Double?,
        winProbabilityAdded: Double,
        homeTeam: String?,
        awayTeam: String?,
        difference: Int,
        timeoutUsed: Boolean?,
        offensiveTimeoutCalled: Boolean?,
        defensiveTimeoutCalled: Boolean?,
        homeTimeouts: Int,
        awayTimeouts: Int,
        playFinished: Boolean?,
        offensiveResponseSpeed: BigInteger?,
        defensiveResponseSpeed: BigInteger?,
    ) {
        this.gameId = gameId
        this.playNumber = playNumber
        this.homeScore = homeScore
        this.awayScore = awayScore
        this.quarter = quarter
        this.clock = clock
        this.ballLocation = ballLocation
        this.possession = possession
        this.down = down
        this.yardsToGo = yardsToGo
        this.defensiveNumber = defensiveNumber
        this.offensiveNumber = offensiveNumber
        this.offensiveSubmitter = offensiveSubmitter
        this.defensiveSubmitter = defensiveSubmitter
        this.playCall = playCall
        this.result = result
        this.actualResult = actualResult
        this.yards = yards
        this.playTime = playTime
        this.runoffTime = runoffTime
        this.winProbability = winProbability
        this.winProbabilityAdded = winProbabilityAdded
        this.homeTeam = homeTeam
        this.awayTeam = awayTeam
        this.difference = difference
        this.timeoutUsed = timeoutUsed
        this.offensiveTimeoutCalled = offensiveTimeoutCalled
        this.defensiveTimeoutCalled = defensiveTimeoutCalled
        this.homeTimeouts = homeTimeouts
        this.awayTimeouts = awayTimeouts
        this.playFinished = playFinished
        this.offensiveResponseSpeed = offensiveResponseSpeed
        this.defensiveResponseSpeed = defensiveResponseSpeed
    }

    constructor()
}
