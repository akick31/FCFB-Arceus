package com.fcfb.arceus.service.gameLogic

import com.fcfb.arceus.domain.GamePlaysEntity
import com.fcfb.arceus.domain.OngoingGamesEntity
import com.fcfb.arceus.domain.RangesEntity
import com.fcfb.arceus.domain.UsersEntity
import com.fcfb.arceus.api.repositories.RangesRepository
import com.fcfb.arceus.api.repositories.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class PlayLogic(private val gameUtils: GameUtils) {
    @Autowired
    lateinit var rangesRepository: RangesRepository

    @Autowired
    lateinit var usersRepository: UsersRepository

    /**
     * Runs the play, returns the updated gamePlay
     * @param gamePlay
     * @param game
     * @param play
     * @param offensiveNumber
     * @param decryptedDefensiveNumber
     * @return
     */
    fun runNormalPlay(
        gamePlay: GamePlaysEntity,
        clockStopped: Boolean?,
        game: OngoingGamesEntity,
        playCall: String, runoffType: String,
        timeoutCalled: Boolean, offensiveNumber: String,
        decryptedDefensiveNumber: String
    ): GamePlaysEntity {
        val difference = gameUtils.getDifference(offensiveNumber.toInt(), decryptedDefensiveNumber.toInt())
        var possession: String? = gamePlay.possession
        var offensivePlaybook: String? = ""
        var defensivePlaybook: String? = ""
        if (possession == "home") {
            offensivePlaybook = game.homeOffensivePlaybook
            defensivePlaybook = game.awayDefensivePlaybook
        } else {
            offensivePlaybook = game.awayOffensivePlaybook
            defensivePlaybook = game.homeDefensivePlaybook
        }
        val resultInformation: RangesEntity = rangesRepository.findNormalResult(
            playCall.lowercase(),
            offensivePlaybook,
            defensivePlaybook,
            difference
        ) ?: throw Exception("Result not found in the ranges")
        
        var result: String? = resultInformation.result?.uppercase()
        val playCall = playCall.uppercase()
        val runoffType = runoffType.uppercase()
        val playTime: Int? = resultInformation.playTime

        // Determine runoff time between plays
        var runoffTime = 0
        if (!clockStopped!! && !timeoutCalled) {
            when {
                playCall == "SPIKE" -> runoffTime = 3
                runoffType == "KNEEL" -> runoffTime = 30
                runoffType == "CHEW" -> runoffTime = 30
                runoffType == "HURRY" -> runoffTime = 7
                offensivePlaybook == "pro" -> runoffTime = 15
                offensivePlaybook == "air raid" -> runoffTime = 10
                offensivePlaybook == "flexbone" -> runoffTime = 20
                offensivePlaybook == "spread" -> runoffTime = 13
                offensivePlaybook == "west coast" -> runoffTime = 17
            }
        }
        var homeScore: Int = game.homeScore
        var awayScore: Int = game.awayScore
        var ballLocation: Int = game.ballLocation ?: throw Exception("Ball location not found")
        var down: Int = game.down
        var yardsToGo: Int = game.yardsToGo
        var yards = 0
        var actualResult: String
        when (result) {
            "PICK/FUMBLE 6" -> actualResult = "TURNOVER TOUCHDOWN"
            "TO + 20 YARDS", "TO + 15 YARDS", "TO + 10 YARDS", "TO + 5 YARDS" -> {
                actualResult = "TURNOVER"
                ballLocation = 100 - ballLocation + result.substringAfter('+').trim().toInt()
                if (ballLocation > 100) {
                    actualResult = "TURNOVER TOUCHDOWN"
                }
            }
            "TURNOVER" -> {
                actualResult = "TURNOVER"
                ballLocation = 100 - ballLocation
            }
            "TO - 5 YARDS", "TO - 10 YARDS", "TO - 15 YARDS", "TO - 20 YARDS" -> {
                actualResult = "TURNOVER"
                ballLocation = 100 - ballLocation - result.substringAfter('-').trim().toInt()
                if (ballLocation <= 0) {
                    ballLocation = 20
                }
            }
            "NO GAIN", "INCOMPLETE" -> {
                actualResult = "NO GAIN"
                if (down > 4) {
                    actualResult = "TURNOVER ON DOWNS"
                    ballLocation = 100 - ballLocation
                }
            }
            else -> {
                yards = result?.toInt()!!
                result = "$result YARDS"
                ballLocation += yards
                if (ballLocation >= 100) {
                    actualResult = "TOUCHDOWN"
                } else if (ballLocation <= 0) {
                    actualResult = "SAFETY"
                } else if (yards > yardsToGo) {
                    down = 1
                    yardsToGo = 10
                    actualResult = "FIRST DOWN"
                } else {
                    down += 1
                    if (down > 4) {
                        actualResult = "TURNOVER ON DOWNS"
                        ballLocation = 100 - ballLocation
                    } else {
                        yardsToGo -= yards
                        actualResult = if (yards > 0) {
                            "GAIN"
                        } else {
                            "LOSS"
                        }
                    }
                }
            }
        }
        when (actualResult) {
            "TURNOVER ON DOWNS", "TURNOVER" -> {
                possession = if (possession == "home") {
                    "away"
                } else {
                    "home"
                }
                down = 1
                yardsToGo = 10
            }
            "SAFETY" -> {
                ballLocation = 20
                if (possession == "home") {
                    awayScore += 2
                } else {
                    homeScore += 2
                }
            }
            "TOUCHDOWN" -> {
                ballLocation = 97
                if (possession == "home") {
                    homeScore += 6
                } else {
                    awayScore += 6
                }
            }
            "TURNOVER TOUCHDOWN" -> {
                ballLocation = 97
                if (possession == "home") {
                    awayScore += 6
                    possession = "away"
                } else {
                    homeScore += 6
                    possession = "home"
                }
            }
        }

        // Handle clock logic
        var clock: Int = (gamePlay.clock?.minus(runoffTime) ?: throw Exception("Clock not found")) - playTime!!
        var quarter: Int = gamePlay.gameQuarter ?: throw Exception("Quarter not found")
        if (clock <= 0 && quarter < 4 && !actualResult.contains("TOUCHDOWN")) {
            quarter += 1
            clock = 420
            if (quarter == 3) {
                ballLocation = 35
                possession = gameUtils.handleHalfTimePossessionChange(game)
                game.currentPlayType = "KICKOFF"
            }
        } else if (clock <= 0 && !actualResult.contains("TOUCHDOWN") && gamePlay.gameQuarter == 4) {
            // Check if game is over or needs to go to OT
            quarter = if (homeScore > awayScore || awayScore > homeScore) {
                0
            } else {
                5
            }
        }
        gamePlay.homeScore = homeScore
        gamePlay.awayScore = awayScore
        gamePlay.possession = possession
        gamePlay.gameQuarter = quarter
        gamePlay.clock = clock
        gamePlay.ballLocation = ballLocation
        gamePlay.down = down
        gamePlay.yardsToGo = yardsToGo
        gamePlay.defensiveNumber = decryptedDefensiveNumber
        gamePlay.offensiveNumber = offensiveNumber
        gamePlay.offensiveSubmitter = gamePlay.offensiveSubmitter
        gamePlay.defensiveSubmitter = gamePlay.defensiveSubmitter
        gamePlay.play = playCall
        gamePlay.result = result
        gamePlay.actualResult = actualResult
        gamePlay.yards = yards
        gamePlay.playTime = playTime
        gamePlay.runoffTime = runoffTime
        gamePlay.winProbability = gamePlay.winProbability
        gamePlay.difference = difference
        val homeUser: UsersEntity = usersRepository.findEntityByTeam(game.homeTeam) ?: throw Exception("Home user entity not found")
        val awayUser: UsersEntity = usersRepository.findEntityByTeam(game.awayTeam) ?: throw Exception("Away user entity not found")
        val homeUsername: String = homeUser.username ?: throw Exception("Home username not found")
        val awayUsername: String = awayUser.username ?: throw Exception("Away username not found")
        if (possession == "home") {
            game.waitingOn = awayUsername
        } else {
            game.waitingOn = homeUsername
        }
        gamePlay.timeoutUsed = !clockStopped && timeoutCalled
        return gamePlay
    }

    /**
     * Runs the kickoff play, returns the updated gamePlay
     * @param gamePlay
     * @param game
     * @param playCall
     * @param offensiveNumber
     * @param decryptedDefensiveNumber
     */
    fun runKickoffPlay(
        gamePlay: GamePlaysEntity,
        game: OngoingGamesEntity, playCall: String,
        offensiveNumber: String,
        decryptedDefensiveNumber: String
    ): GamePlaysEntity {
        val difference = gameUtils.getDifference(offensiveNumber.toInt(), decryptedDefensiveNumber.toInt())
        var possession: String? = gamePlay.possession
        val resultInformation: RangesEntity = rangesRepository.findNonNormalResult(playCall.lowercase(), difference) ?: throw Exception("Result not found in the ranges")
        val result: String? = resultInformation.result?.uppercase()
        val playCall = playCall.uppercase()
        var homeScore: Int = game.homeScore
        var awayScore: Int = game.awayScore
        var ballLocation = 35
        val down = 1
        val yardsToGo = 10
        val playTime: Int = resultInformation.playTime ?: throw Exception("Play time not found")
        var actualResult = ""
        when (result) {
            "TOUCHDOWN" -> {
                actualResult = "KICKING TEAM TOUCHDOWN"
                ballLocation = 97
            }
            "FUMBLE" -> {
                actualResult = "MUFFED KICK"
                ballLocation = 75
            }
            "5", "10", "20", "30", "35", "40", "45", "50", "65" -> {
                actualResult = "KICKOFF"
                ballLocation = result.toInt()
            }
            "TOUCHBACK" -> {
                actualResult = "KICKOFF"
                ballLocation = 25
            }
            "RETURN TOUCHDOWN" -> {
                actualResult = "RETURN TOUCHDOWN"
                ballLocation = 97
            }
            "RECOVERED" -> {
                actualResult = "ONSIDE KICK RECOVERED"
                ballLocation = 45
            }
            "NO GOOD" -> {
                actualResult = "ONSIDE KICK FAILED"
                ballLocation = 55
            }
        }
        when (actualResult) {
            "KICKING TEAM TOUCHDOWN" -> if (possession == "home") {
                homeScore += 6
            } else {
                awayScore += 6
            }
            "KICKOFF", "ONSIDE KICK FAILED" -> possession = if (possession == "home") {
                "away"
            } else {
                "home"
            }
            "RETURN TOUCHDOWN" -> if (possession == "home") {
                possession = "away"
                awayScore += 6
            } else {
                possession = "home"
                homeScore += 6
            }
            "ONSIDE KICK RECOVERED", "MUFFED KICK" -> possession = if (possession == "home") {
                "home"
            } else {
                "away"
            }
        }

        // Handle clock logic
        var clock: Int = gamePlay.clock?.minus(playTime) ?: throw Exception("Clock not found")
        var quarter: Int = gamePlay.gameQuarter ?: throw Exception("Quarter not found")
        if (clock <= 0 && quarter < 4 && !actualResult.contains("TOUCHDOWN")) {
            quarter += 1
            clock = 420
            if (quarter == 3) {
                possession = gameUtils.handleHalfTimePossessionChange(game)
                game.currentPlayType = "KICKOFF"
            }
        } else if (clock <= 0 && !actualResult.contains("TOUCHDOWN") && gamePlay.gameQuarter == 4) {
            // Check if game is over or needs to go to OT
            quarter = if (homeScore > awayScore || awayScore > homeScore) {
                0
            } else {
                5
            }
        }
        val homeUser: UsersEntity = usersRepository.findEntityByTeam(game.homeTeam) ?: throw Exception("Home user entity not found")
        val awayUser: UsersEntity = usersRepository.findEntityByTeam(game.awayTeam) ?: throw Exception("Away user entity not found")
        val homeUsername: String = homeUser.username ?: throw Exception("Home username not found")
        val awayUsername: String = awayUser.username ?: throw Exception("Away username not found")
        if (possession == "home") {
            game.waitingOn = awayUsername
        } else {
            game.waitingOn = homeUsername
        }
        gamePlay.homeScore = homeScore
        gamePlay.awayScore = awayScore
        gamePlay.possession = possession
        gamePlay.gameQuarter = quarter
        gamePlay.clock = clock
        gamePlay.ballLocation = ballLocation
        gamePlay.down = down
        gamePlay.yardsToGo = yardsToGo
        gamePlay.defensiveNumber = decryptedDefensiveNumber
        gamePlay.offensiveNumber = offensiveNumber
        gamePlay.offensiveSubmitter = gamePlay.offensiveSubmitter
        gamePlay.defensiveSubmitter = gamePlay.defensiveSubmitter
        gamePlay.play = playCall
        gamePlay.result = result
        gamePlay.actualResult = actualResult
        gamePlay.yards = 0
        gamePlay.playTime = playTime
        gamePlay.runoffTime = 0
        gamePlay.winProbability = gamePlay.winProbability
        gamePlay.difference = difference
        gamePlay.timeoutUsed = false
        return gamePlay
    }

    /**
     * Runs the point after play, returns the updated gamePlay
     * @param gamePlay
     * @param game
     * @param playCall
     * @param offensiveNumber
     * @param decryptedDefensiveNumber
     */
    fun runPointAfterPlay(
        gamePlay: GamePlaysEntity,
        game: OngoingGamesEntity,
        playCall: String,
        offensiveNumber: String,
        decryptedDefensiveNumber: String
    ): GamePlaysEntity {
        val difference = gameUtils.getDifference(offensiveNumber.toInt(), decryptedDefensiveNumber.toInt())
        var possession: String? = gamePlay.possession
        val resultInformation: RangesEntity = rangesRepository.findNonNormalResult(playCall.lowercase(), difference) ?: throw Exception("Result not found in the ranges")
        val result: String? = resultInformation.result?.uppercase()
        var playCall = playCall.uppercase()
        var homeScore: Int = game.homeScore
        var awayScore: Int = game.awayScore
        val ballLocation = 25
        val down = 1
        val yardsToGo = 10
        var actualResult = ""
        when (result) {
            "GOOD" -> actualResult = "GOOD"
            "NO GOOD" -> actualResult = "NO GOOD"
            "DEFENSE TWO POINT" -> actualResult = "DEFENSE TWO POINT"
        }
        when (actualResult) {
            "GOOD" -> {
                if (playCall == "PAT") {
                    if (possession == "home") {
                        homeScore += 1
                    } else {
                        awayScore += 1
                    }
                } else {
                    if (possession == "home") {
                        homeScore += 2
                    } else {
                        awayScore += 2
                    }
                }
            }
        }
        possession = if (possession == "home") {
            "away"
        } else {
            "home"
        }
        val clock: Int = (gamePlay.clock ?: throw Exception("Clock not found")) - (resultInformation.playTime ?: throw Exception("Play time not found"))
        val quarter: Int = gamePlay.gameQuarter ?: throw Exception("Game quarter not found")
        val homeUser: UsersEntity = usersRepository.findEntityByTeam(game.homeTeam) ?: throw Exception("Home user entity not found")
        val awayUser: UsersEntity = usersRepository.findEntityByTeam(game.awayTeam) ?: throw Exception("Away user entity not found")
        val homeUsername: String = homeUser.username ?: throw Exception("Home username not found")
        val awayUsername: String = awayUser.username ?: throw Exception("Away username not found")
        if (possession == "home") {
            game.waitingOn = awayUsername
        } else {
            game.waitingOn = homeUsername
        }
        gamePlay.homeScore = homeScore
        gamePlay.awayScore = awayScore
        gamePlay.possession = possession
        gamePlay.gameQuarter = quarter
        gamePlay.clock = clock
        gamePlay.ballLocation = ballLocation
        gamePlay.down = down
        gamePlay.yardsToGo = yardsToGo
        gamePlay.defensiveNumber = decryptedDefensiveNumber
        gamePlay.offensiveNumber = offensiveNumber
        gamePlay.offensiveSubmitter = gamePlay.offensiveSubmitter
        gamePlay.defensiveSubmitter = gamePlay.defensiveSubmitter
        gamePlay.play = playCall
        gamePlay.result = result
        gamePlay.actualResult = actualResult
        gamePlay.yards = 0
        gamePlay.playTime = resultInformation.playTime
        gamePlay.runoffTime = 0
        gamePlay.winProbability = gamePlay.winProbability
        gamePlay.difference = difference
        gamePlay.timeoutUsed = false
        return gamePlay
    }
}
