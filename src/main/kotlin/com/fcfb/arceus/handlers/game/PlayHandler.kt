package com.fcfb.arceus.handlers.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.ActualResult
import com.fcfb.arceus.domain.Game.DefensivePlaybook
import com.fcfb.arceus.domain.Game.OffensivePlaybook
import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.PlayType
import com.fcfb.arceus.domain.Game.RunoffType
import com.fcfb.arceus.domain.Game.Scenario
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.domain.Ranges
import com.fcfb.arceus.models.ExceptionType
import com.fcfb.arceus.models.handleException
import com.fcfb.arceus.repositories.RangesRepository
import io.ktor.util.reflect.instanceOf
import org.springframework.stereotype.Component

@Component
class PlayHandler(
    private var rangesRepository: RangesRepository,
    private var gameHandler: GameHandler
) {
    /**
     * Runs the play, returns the updated gamePlay
     * @param gamePlay
     * @param game
     * @param playCall
     * @param offensiveNumber
     * @param decryptedDefensiveNumber
     * @return
     */
    fun runNormalPlay(
        gamePlay: Play,
        clockStopped: Boolean?,
        game: Game,
        playCall: PlayCall,
        runoffType: RunoffType,
        timeoutCalled: Boolean,
        offensiveNumber: String,
        decryptedDefensiveNumber: String
    ): Play? {
        if (game.currentPlayType != PlayType.NORMAL) {
            return null
        }
        val difference = gameHandler.getDifference(offensiveNumber.toInt(), decryptedDefensiveNumber.toInt())
        var possession = gamePlay.possession
        val offensivePlaybook: OffensivePlaybook
        val defensivePlaybook: DefensivePlaybook
        if (possession == TeamSide.HOME) {
            offensivePlaybook = game.homeOffensivePlaybook
            defensivePlaybook = game.awayDefensivePlaybook
        } else {
            offensivePlaybook = game.awayOffensivePlaybook
            defensivePlaybook = game.homeDefensivePlaybook
        }
        println(defensivePlaybook.toString())
        val resultInformation: Ranges = rangesRepository.findNormalResult(
            playCall.description,
            offensivePlaybook.description,
            defensivePlaybook.description,
            difference.toString()
        ) ?: handleException(ExceptionType.RESULT_NOT_FOUND)

        val result = resultInformation.result
        val playTime = resultInformation.playTime

        // Determine runoff time between plays
        var runoffTime = 0
        if (!clockStopped!! && !timeoutCalled) {
            when {
                playCall == PlayCall.SPIKE -> runoffTime = 3
                playCall == PlayCall.KNEEL -> runoffTime = 30
                runoffType == RunoffType.CHEW -> runoffTime = 30
                runoffType == RunoffType.HURRY -> runoffTime = 7
                offensivePlaybook == OffensivePlaybook.PRO -> runoffTime = 15
                offensivePlaybook == OffensivePlaybook.AIR_RAID -> runoffTime = 10
                offensivePlaybook == OffensivePlaybook.FLEXBONE -> runoffTime = 20
                offensivePlaybook == OffensivePlaybook.SPREAD -> runoffTime = 13
                offensivePlaybook == OffensivePlaybook.WEST_COAST -> runoffTime = 17
            }
        }
        var homeScore = game.homeScore ?: handleException(ExceptionType.INVALID_HOME_SCORE)
        var awayScore = game.awayScore ?: handleException(ExceptionType.INVALID_AWAY_SCORE)
        var ballLocation = game.ballLocation ?: handleException(ExceptionType.INVALID_BALL_LOCATION)
        var down = game.down ?: handleException(ExceptionType.INVALID_DOWN)
        var yardsToGo = game.yardsToGo ?: handleException(ExceptionType.INVALID_YARDS_TO_GO)
        var yards = 0
        var actualResult: ActualResult
        when (result) {
            Scenario.TURNOVER_TOUCHDOWN -> actualResult = ActualResult.TURNOVER_TOUCHDOWN
            Scenario.TURNOVER_PLUS_20_YARDS -> {
                actualResult = ActualResult.TURNOVER
                ballLocation = 100 - ballLocation + 20
                if (ballLocation > 100) {
                    actualResult = ActualResult.TURNOVER_TOUCHDOWN
                }
            }
            Scenario.TURNOVER_PLUS_15_YARDS -> {
                actualResult = ActualResult.TURNOVER
                ballLocation = 100 - ballLocation + 15
                if (ballLocation > 100) {
                    actualResult = ActualResult.TURNOVER_TOUCHDOWN
                }
            }
            Scenario.TURNOVER_PLUS_10_YARDS -> {
                actualResult = ActualResult.TURNOVER
                ballLocation = 100 - ballLocation + 10
                if (ballLocation > 100) {
                    actualResult = ActualResult.TURNOVER_TOUCHDOWN
                }
            }
            Scenario.TURNOVER_PLUS_5_YARDS -> {
                actualResult = ActualResult.TURNOVER
                ballLocation = 100 - ballLocation + 5
                if (ballLocation > 100) {
                    actualResult = ActualResult.TURNOVER_TOUCHDOWN
                }
            }
            Scenario.TURNOVER -> {
                actualResult = ActualResult.TURNOVER
                ballLocation = 100 - ballLocation
            }
            Scenario.TURNOVER_MINUS_5_YARDS -> {
                actualResult = ActualResult.TURNOVER
                ballLocation = 100 - ballLocation - 5
                if (ballLocation <= 0) {
                    ballLocation = 20
                }
            }
            Scenario.TURNOVER_MINUS_10_YARDS -> {
                actualResult = ActualResult.TURNOVER
                ballLocation = 100 - ballLocation - 10
                if (ballLocation <= 0) {
                    ballLocation = 20
                }
            }
            Scenario.TURNOVER_MINUS_15_YARDS -> {
                actualResult = ActualResult.TURNOVER
                ballLocation = 100 - ballLocation - 15
                if (ballLocation <= 0) {
                    ballLocation = 20
                }
            }
            Scenario.TURNOVER_MINUS_20_YARDS -> {
                actualResult = ActualResult.TURNOVER
                ballLocation = 100 - ballLocation - 20
                if (ballLocation <= 0) {
                    ballLocation = 20
                }
            }
            Scenario.NO_GAIN, Scenario.INCOMPLETE -> {
                actualResult = ActualResult.NO_GAIN
                down += 1
                if (down > 4) {
                    actualResult = ActualResult.TURNOVER_ON_DOWNS
                    ballLocation = 100 - ballLocation
                }
            }
            else -> {
                yards = result?.description?.toInt()!!
                ballLocation += yards
                if (ballLocation >= 100) {
                    actualResult = ActualResult.TOUCHDOWN
                } else if (ballLocation <= 0) {
                    actualResult = ActualResult.SAFETY
                } else if (yards >= yardsToGo) {
                    down = 1
                    yardsToGo = 10
                    actualResult = ActualResult.FIRST_DOWN
                } else {
                    down += 1
                    if (down > 4) {
                        actualResult = ActualResult.TURNOVER_ON_DOWNS
                        ballLocation = 100 - ballLocation
                    } else {
                        yardsToGo -= yards
                        actualResult = if (yards > 0) {
                            ActualResult.GAIN
                        } else {
                            ActualResult.NO_GAIN
                        }
                    }
                }
            }
        }
        when (actualResult) {
            ActualResult.TURNOVER_ON_DOWNS, ActualResult.TURNOVER -> {
                possession = if (possession == TeamSide.HOME) {
                    TeamSide.AWAY
                } else {
                    TeamSide.HOME
                }
                down = 1
                yardsToGo = 10
            }
            ActualResult.SAFETY -> {
                ballLocation = 20
                if (possession == TeamSide.HOME) {
                    awayScore += 2
                } else {
                    homeScore += 2
                }
            }
            ActualResult.TOUCHDOWN -> {
                ballLocation = 97
                if (possession == TeamSide.HOME) {
                    homeScore += 6
                } else {
                    awayScore += 6
                }
            }
            ActualResult.TURNOVER_TOUCHDOWN -> {
                ballLocation = 97
                if (possession == TeamSide.HOME) {
                    awayScore += 6
                    possession = TeamSide.AWAY
                } else {
                    homeScore += 6
                    possession = TeamSide.HOME
                }
            }
            ActualResult.FIRST_DOWN -> {}
            ActualResult.GAIN -> {}
            ActualResult.NO_GAIN -> {}
            else -> handleException(ExceptionType.INVALID_ACTUAL_RESULT)
        }

        // Handle clock logic
        var clock = (gamePlay.clock?.minus(runoffTime) ?: handleException(ExceptionType.INVALID_CLOCK)) - (playTime ?: handleException(
            ExceptionType.INVALID_PLAY_TIME))
        var quarter = gamePlay.gameQuarter ?: handleException(ExceptionType.INVALID_QUARTER)
        if (clock <= 0 &&
            quarter < 4 &&
            actualResult != ActualResult.TOUCHDOWN &&
            actualResult != ActualResult.TURNOVER_TOUCHDOWN
        ) {
            quarter += 1
            clock = 420
            if (quarter == 3) {
                ballLocation = 35
                possession = gameHandler.handleHalfTimePossessionChange(game)
                game.currentPlayType = PlayType.KICKOFF
            }
        } else if (clock <= 0 &&
            actualResult != ActualResult.TOUCHDOWN &&
            actualResult != ActualResult.TURNOVER_TOUCHDOWN &&
            gamePlay.gameQuarter == 4
        ) {
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
        gamePlay.playCall = playCall
        gamePlay.result = result
        gamePlay.actualResult = actualResult
        gamePlay.yards = yards
        gamePlay.playTime = playTime
        gamePlay.runoffTime = runoffTime
        gamePlay.winProbability = gamePlay.winProbability
        gamePlay.difference = difference
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
        gamePlay: Play,
        game: Game,
        playCall: PlayCall,
        offensiveNumber: String,
        decryptedDefensiveNumber: String
    ): Play? {
        if (game.currentPlayType != PlayType.KICKOFF) {
            return null
        }

        val difference = gameHandler.getDifference(offensiveNumber.toInt(), decryptedDefensiveNumber.toInt())
        var possession = gamePlay.possession
        val resultInformation = rangesRepository.findNonNormalResult(
            playCall.description,
            difference.toString()
        ) ?: handleException(ExceptionType.RESULT_NOT_FOUND)
        val result = resultInformation.result
        var homeScore = game.homeScore ?: handleException(ExceptionType.INVALID_HOME_SCORE)
        var awayScore = game.awayScore ?: handleException(ExceptionType.INVALID_AWAY_SCORE)
        var ballLocation = 35
        val down = 1
        val yardsToGo = 10
        val playTime = resultInformation.playTime ?: handleException(ExceptionType.INVALID_PLAY_TIME)
        val actualResult: ActualResult?
        when (result) {
            Scenario.TOUCHDOWN -> {
                actualResult = ActualResult.KICKING_TEAM_TOUCHDOWN
                ballLocation = 97
            }
            Scenario.FUMBLE -> {
                actualResult = ActualResult.MUFFED_KICK
                ballLocation = 75
            }
            Scenario.FIVE_YARD_LINE, Scenario.TEN_YARD_LINE, Scenario.TWENTY_YARD_LINE, Scenario.THIRTY_YARD_LINE,
            Scenario.THIRTY_FIVE_YARD_LINE, Scenario.FOURTY_YARD_LINE, Scenario.FOURTY_FIVE_YARD_LINE,
            Scenario.FIFTY_YARD_LINE, Scenario.SIXTY_FIVE_YARD_LINE -> {
                actualResult = ActualResult.KICKOFF
                ballLocation = result.description.substringBefore(" YARD LINE").toInt()
            }
            Scenario.TOUCHBACK -> {
                actualResult = ActualResult.KICKOFF
                ballLocation = 25
            }
            Scenario.RETURN_TOUCHDOWN -> {
                actualResult = ActualResult.RETURN_TOUCHDOWN
                ballLocation = 97
            }
            Scenario.RECOVERED -> {
                actualResult = ActualResult.SUCCESSFUL_ONSIDE
                ballLocation = 45
            }
            Scenario.NO_GOOD -> {
                actualResult = ActualResult.FAILED_ONSIDE
                ballLocation = 55
            }
            else -> handleException(ExceptionType.INVALID_RESULT)
        }
        when (actualResult) {
            ActualResult.KICKING_TEAM_TOUCHDOWN -> if (possession == TeamSide.HOME) {
                homeScore += 6
            } else {
                awayScore += 6
            }
            ActualResult.KICKOFF, ActualResult.FAILED_ONSIDE -> possession = if (possession == TeamSide.HOME) {
                TeamSide.AWAY
            } else {
                TeamSide.HOME
            }
            ActualResult.RETURN_TOUCHDOWN -> if (possession == TeamSide.HOME) {
                possession = TeamSide.AWAY
                awayScore += 6
            } else {
                possession = TeamSide.HOME
                homeScore += 6
            }
            ActualResult.SUCCESSFUL_ONSIDE, ActualResult.MUFFED_KICK -> possession = if (possession == TeamSide.HOME) {
                TeamSide.HOME
            } else {
                TeamSide.AWAY
            }
            else -> handleException(ExceptionType.INVALID_ACTUAL_RESULT)
        }

        // Handle clock logic
        var clock = gamePlay.clock?.minus(playTime) ?: handleException(ExceptionType.INVALID_CLOCK)
        var quarter = gamePlay.gameQuarter ?: handleException(ExceptionType.INVALID_QUARTER)
        if (clock <= 0 &&
            quarter < 4 &&
            actualResult != ActualResult.RETURN_TOUCHDOWN &&
            actualResult != ActualResult.KICKING_TEAM_TOUCHDOWN
        ) {
            quarter += 1
            clock = 420
            if (quarter == 3) {
                possession = gameHandler.handleHalfTimePossessionChange(game)
                game.currentPlayType = PlayType.KICKOFF
            }
        } else if (clock <= 0 &&
            actualResult != ActualResult.RETURN_TOUCHDOWN &&
            actualResult != ActualResult.KICKING_TEAM_TOUCHDOWN &&
            gamePlay.gameQuarter == 4
        ) {
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
        gamePlay.playCall = playCall
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
        gamePlay: Play,
        game: Game,
        playCall: PlayCall,
        offensiveNumber: String,
        decryptedDefensiveNumber: String
    ): Play? {
        if (game.currentPlayType != PlayType.PAT) {
            return null
        }

        val difference = gameHandler.getDifference(offensiveNumber.toInt(), decryptedDefensiveNumber.toInt())
        var possession: TeamSide? = gamePlay.possession
        val resultInformation: Ranges = rangesRepository.findNonNormalResult(
            playCall.description,
            difference.toString()
        ) ?: handleException(ExceptionType.RESULT_NOT_FOUND)
        val result = resultInformation.result
        var homeScore = game.homeScore ?: handleException(ExceptionType.INVALID_HOME_SCORE)
        var awayScore = game.awayScore ?: handleException(ExceptionType.INVALID_AWAY_SCORE)
        val ballLocation = 25
        val down = 1
        val yardsToGo = 10
        val actualResult = when (result) {
            Scenario.GOOD -> ActualResult.GOOD
            Scenario.NO_GOOD -> ActualResult.NO_GOOD
            Scenario.DEFENSE_TWO_POINT -> ActualResult.DEFENSE_TWO_POINT
            else -> handleException(ExceptionType.INVALID_RESULT)
        }
        when (actualResult) {
            ActualResult.GOOD -> {
                if (playCall == PlayCall.PAT) {
                    if (possession == TeamSide.HOME) {
                        homeScore += 1
                    } else {
                        awayScore += 1
                    }
                } else {
                    if (possession == TeamSide.HOME) {
                        homeScore += 2
                    } else {
                        awayScore += 2
                    }
                }
            }
            ActualResult.NO_GOOD -> {}
            ActualResult.DEFENSE_TWO_POINT -> {
                if (possession == TeamSide.HOME) {
                    awayScore += 2
                } else {
                    homeScore += 2
                }
            }
            else -> {
                handleException(ExceptionType.INVALID_ACTUAL_RESULT)
            }
        }
        val clock = (gamePlay.clock ?: handleException(ExceptionType.INVALID_CLOCK)) - (resultInformation.playTime ?: handleException(
            ExceptionType.INVALID_PLAY_TIME))
        val quarter = gamePlay.gameQuarter ?: handleException(ExceptionType.INVALID_QUARTER)
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
        gamePlay.playCall = playCall
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