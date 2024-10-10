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
        timeoutUsed: Boolean,
        offensiveNumber: String,
        decryptedDefensiveNumber: String
    ): Play? {
        if (game.currentPlayType != PlayType.NORMAL) {
            return null
        }
        val difference = gameHandler.getDifference(offensiveNumber.toInt(), decryptedDefensiveNumber.toInt())
        var possession = gamePlay.possession
        val (offensivePlaybook, defensivePlaybook) = getPlaybooks(game, possession ?: handleException(ExceptionType.INVALID_POSSESSION))

        val resultInformation = if (playCall == PlayCall.SPIKE) {
            Ranges(
                PlayType.NORMAL.description,
                offensivePlaybook.description,
                defensivePlaybook.description,
                Scenario.SPIKE,
                0,
                0,
                0
            )
        }
        else if (playCall == PlayCall.KNEEL) {
            Ranges(
                PlayType.NORMAL.description,
                offensivePlaybook.description,
                defensivePlaybook.description,
                Scenario.KNEEL,
                0,
                0,
                0
            )
        }
        else {
            rangesRepository.findNormalResult(
                playCall.description,
                offensivePlaybook.description,
                defensivePlaybook.description,
                difference.toString()
            ) ?: handleException(ExceptionType.RESULT_NOT_FOUND)
        }

        val result = resultInformation.result
        val playTime = resultInformation.playTime

        // Determine runoff time between plays
        val runoffTime = getRunoffTime(clockStopped, timeoutUsed, playCall, runoffType, offensivePlaybook, defensivePlaybook,)

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
            Scenario.TOUCHDOWN -> {
                actualResult = ActualResult.TOUCHDOWN
            }
            Scenario.SPIKE -> {
                actualResult = ActualResult.SPIKE
                down += 1
                if (down > 4) {
                    actualResult = ActualResult.TURNOVER_ON_DOWNS
                    ballLocation = 100 - ballLocation
                }
            }
            Scenario.KNEEL -> {
                yards = -3
                actualResult = ActualResult.KNEEL
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
            ActualResult.SPIKE -> {}
            ActualResult.KNEEL -> {}
            else -> handleException(ExceptionType.INVALID_ACTUAL_RESULT)
        }

        return updatePlayValues(
            game,
            gamePlay,
            playCall,
            result,
            actualResult,
            possession ?: handleException(ExceptionType.INVALID_POSSESSION),
            homeScore,
            awayScore,
            runoffTime,
            playTime ?: handleException(ExceptionType.INVALID_PLAY_TIME),
            ballLocation,
            down,
            yardsToGo,
            decryptedDefensiveNumber,
            offensiveNumber,
            difference,
            yards,
            timeoutUsed
        )
    }

    /**
     * Runs the punt play, returns the updated gamePlay
     * @param gamePlay
     * @param game
     * @param playCall
     * @param offensiveNumber
     * @param decryptedDefensiveNumber
     */
    fun runPuntPlay(
        gamePlay: Play,
        clockStopped: Boolean?,
        game: Game,
        playCall: PlayCall,
        runoffType: RunoffType,
        timeoutUsed: Boolean,
        offensiveNumber: String,
        decryptedDefensiveNumber: String
    ): Play? {
        if (game.currentPlayType != PlayType.NORMAL) {
            return null
        }

        val difference = gameHandler.getDifference(offensiveNumber.toInt(), decryptedDefensiveNumber.toInt())
        var possession = gamePlay.possession
        var ballLocation = game.ballLocation ?: handleException(ExceptionType.INVALID_BALL_LOCATION)
        val (offensivePlaybook, defensivePlaybook) = getPlaybooks(game, possession ?: handleException(ExceptionType.INVALID_POSSESSION))

        val resultInformation = rangesRepository.findPuntResult(
            playCall.description,
            ballLocation.toString(),
            difference.toString()
        ) ?: handleException(ExceptionType.RESULT_NOT_FOUND)

        val result = resultInformation.result
        val playTime = resultInformation.playTime

        // Determine runoff time between plays
        val runoffTime = getRunoffTime(clockStopped, timeoutUsed, playCall, runoffType, offensivePlaybook, defensivePlaybook,)

        var homeScore = game.homeScore ?: handleException(ExceptionType.INVALID_HOME_SCORE)
        var awayScore = game.awayScore ?: handleException(ExceptionType.INVALID_AWAY_SCORE)
        var down = game.down ?: handleException(ExceptionType.INVALID_DOWN)
        var yardsToGo = game.yardsToGo ?: handleException(ExceptionType.INVALID_YARDS_TO_GO)
        val yards = 0
        val actualResult: ActualResult
        when (result) {
            Scenario.PUNT_RETURN_TOUCHDOWN -> {
                actualResult = ActualResult.PUNT_RETURN_TOUCHDOWN
                ballLocation = 97
            }
            Scenario.BLOCKED_PUNT -> {
                actualResult = ActualResult.BLOCKED
                ballLocation = 100 - ballLocation
            }
            Scenario.TOUCHBACK -> {
                actualResult = ActualResult.PUNT
                ballLocation = 20
            }
            Scenario.PUNT_TO_THE_FIVE, Scenario.PUNT_TO_THE_TEN, Scenario.PUNT_TO_THE_FIFTEEN, Scenario.PUNT_TO_THE_TWENTY,
            Scenario.PUNT_TO_THE_TWENTY_FIVE, Scenario.PUNT_TO_THE_THIRTY, Scenario.PUNT_TO_THE_THIRTY_FIVE,
            Scenario.PUNT_TO_THE_FORTY, Scenario.PUNT_TO_THE_FORTY_FIVE, Scenario.PUNT_TO_THE_FIFTY,
            Scenario.PUNT_TO_THE_FIFTY_FIVE, Scenario.PUNT_TO_THE_SIXTY, Scenario.PUNT_TO_THE_SIXTY_FIVE,
            Scenario.PUNT_TO_THE_SEVENTY -> {
                actualResult = ActualResult.PUNT
                ballLocation = result.description.substringAfter("PUNT TO THE ").substringBefore(" YARD").toInt()
            }
            Scenario.FUMBLE -> {
                actualResult = ActualResult.MUFFED_PUNT
                ballLocation = 100 - (ballLocation + 40)
            }
            Scenario.TOUCHDOWN -> {
                actualResult = ActualResult.PUNT_TEAM_TOUCHDOWN
                ballLocation = 97
            }
            else -> handleException(ExceptionType.INVALID_RESULT)
        }
        when (actualResult) {
            ActualResult.PUNT_RETURN_TOUCHDOWN  -> {
                if (possession == TeamSide.HOME) {
                    possession = TeamSide.AWAY
                    awayScore += 6
                } else {
                    possession = TeamSide.HOME
                    homeScore += 6
                }
                down = 1
                yardsToGo = 10
            }
            ActualResult.BLOCKED -> {
                possession = if (possession == TeamSide.HOME) {
                    TeamSide.AWAY
                } else {
                    TeamSide.HOME
                }
                down = 1
                yardsToGo = 10
            }
            ActualResult.PUNT -> {
                possession = if (possession == TeamSide.HOME) {
                    TeamSide.AWAY
                } else {
                    TeamSide.HOME
                }
                down = 1
                yardsToGo = 10
            }
            ActualResult.MUFFED_PUNT -> {
                possession = if (possession == TeamSide.HOME) {
                    TeamSide.HOME
                } else {
                    TeamSide.AWAY
                }
                down = 1
                yardsToGo = 10
            }
            ActualResult.PUNT_TEAM_TOUCHDOWN -> {
                if (possession == TeamSide.HOME) {
                    homeScore += 6
                    possession = TeamSide.HOME
                } else {
                    awayScore += 6
                    possession = TeamSide.AWAY
                }
            }
            else -> handleException(ExceptionType.INVALID_ACTUAL_RESULT)
        }

        return updatePlayValues(
            game,
            gamePlay,
            playCall,
            result,
            actualResult,
            possession ?: handleException(ExceptionType.INVALID_POSSESSION),
            homeScore,
            awayScore,
            runoffTime,
            playTime ?: handleException(ExceptionType.INVALID_PLAY_TIME),
            ballLocation,
            down,
            yardsToGo,
            decryptedDefensiveNumber,
            offensiveNumber,
            difference,
            0,
            timeoutUsed
        )
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
            Scenario.FIVE_YARD_RETURN, Scenario.TEN_YARD_RETURN, Scenario.TWENTY_YARD_RETURN, Scenario.THIRTY_YARD_RETURN,
            Scenario.THIRTY_FIVE_YARD_RETURN, Scenario.FOURTY_YARD_RETURN, Scenario.FOURTY_FIVE_YARD_RETURN,
            Scenario.FIFTY_YARD_RETURN, Scenario.SIXTY_FIVE_YARD_RETURN -> {
                actualResult = ActualResult.KICKOFF
                ballLocation = result.description.substringBefore(" YARD RETURN").toInt()
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

        return updatePlayValues(
            game,
            gamePlay,
            playCall,
            result,
            actualResult,
            possession ?: handleException(ExceptionType.INVALID_POSSESSION),
            homeScore,
            awayScore,
            0,
            playTime,
            ballLocation,
            down,
            yardsToGo,
            decryptedDefensiveNumber,
            offensiveNumber,
            difference,
            0,
            false
        )
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
        val ballLocation = 35
        val down = 1
        val yardsToGo = 10
        val actualResult = when (result) {
            Scenario.GOOD -> ActualResult.GOOD
            Scenario.NO_GOOD -> ActualResult.NO_GOOD
            Scenario.SUCCESS -> ActualResult.SUCCESS
            Scenario.FAILED -> ActualResult.FAILED
            Scenario.DEFENSE_TWO_POINT -> ActualResult.DEFENSE_TWO_POINT
            else -> handleException(ExceptionType.INVALID_RESULT)
        }
        when (actualResult) {
            ActualResult.GOOD -> {
                if (possession == TeamSide.HOME) {
                    homeScore += 1
                } else {
                    awayScore += 1
                }
            }
            ActualResult.NO_GOOD -> {}
            ActualResult.SUCCESS ->
                if (possession == TeamSide.HOME) {
                    homeScore += 2
                } else {
                    awayScore += 2
                }
            ActualResult.FAILED -> {}
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

        return updatePlayValues(
            game,
            gamePlay,
            playCall,
            result,
            actualResult,
            possession ?: handleException(ExceptionType.INVALID_POSSESSION),
            homeScore,
            awayScore,
            0,
            0,
            ballLocation,
            down,
            yardsToGo,
            decryptedDefensiveNumber,
            offensiveNumber,
            difference,
            0,
            false
        )
    }

    private fun getRunoffTime(
        clockStopped: Boolean?,
        timeoutUsed: Boolean,
        playCall: PlayCall,
        runoffType: RunoffType,
        offensivePlaybook: OffensivePlaybook,
        defensivePlaybook: DefensivePlaybook
    ): Int {
        // Determine runoff time between plays
        return if (!clockStopped!! && !timeoutUsed) {
            when {
                playCall == PlayCall.SPIKE -> 3
                playCall == PlayCall.KNEEL ->  30
                runoffType == RunoffType.CHEW ->  30
                runoffType == RunoffType.HURRY ->  7
                offensivePlaybook == OffensivePlaybook.PRO ->  15
                offensivePlaybook == OffensivePlaybook.AIR_RAID ->  10
                offensivePlaybook == OffensivePlaybook.FLEXBONE ->  20
                offensivePlaybook == OffensivePlaybook.SPREAD ->  13
                offensivePlaybook == OffensivePlaybook.WEST_COAST ->  17
                else -> 0
            }
        } else {
            0
        }
    }

    private fun getPlaybooks(
        game: Game,
        possession: TeamSide
    ): Pair<OffensivePlaybook, DefensivePlaybook> {
        return if (possession == TeamSide.HOME) {
            game.homeOffensivePlaybook to game.awayDefensivePlaybook
        } else {
            game.awayOffensivePlaybook to game.homeDefensivePlaybook
        }
    }

    private fun handleEndOfQuarterScenarios(
        game: Game,
        gamePlay: Play,
        actualResult: ActualResult,
        initialPossession: TeamSide,
        initialClock: Int,
        initialQuarter: Int,
        homeScore: Int,
        awayScore: Int,
    ): Triple<TeamSide, Int, Int> {
        var possession = initialPossession
        var clock = initialClock
        var quarter = initialQuarter

        // If quarter is over but game is not over
        if (clock <= 0 &&
            quarter < 4 &&
            actualResult != ActualResult.TOUCHDOWN &&
            actualResult != ActualResult.TURNOVER_TOUCHDOWN &&
            actualResult != ActualResult.RETURN_TOUCHDOWN &&
            actualResult != ActualResult.KICKING_TEAM_TOUCHDOWN &&
            actualResult != ActualResult.PUNT_RETURN_TOUCHDOWN &&
            actualResult != ActualResult.PUNT_TEAM_TOUCHDOWN &&
            actualResult != ActualResult.BLOCKED_FIELD_GOAL_TOUCHDOWN
        ) {
            quarter += 1
            clock = 420
            if (quarter == 3) {
                possession = gameHandler.handleHalfTimePossessionChange(game) ?: handleException(ExceptionType.INVALID_POSSESSION)
            }
        // If quarter is over AND regulation is over
        } else if (clock <= 0 &&
            actualResult != ActualResult.TOUCHDOWN &&
            actualResult != ActualResult.TURNOVER_TOUCHDOWN &&
            actualResult != ActualResult.RETURN_TOUCHDOWN &&
            actualResult != ActualResult.KICKING_TEAM_TOUCHDOWN &&
            actualResult != ActualResult.PUNT_RETURN_TOUCHDOWN &&
            actualResult != ActualResult.PUNT_TEAM_TOUCHDOWN &&
            actualResult != ActualResult.BLOCKED_FIELD_GOAL_TOUCHDOWN &&
            gamePlay.gameQuarter == 4
        ) {
            // Check if game is over or needs to go to OT
            quarter = if (homeScore > awayScore || awayScore > homeScore) {
                0
            } else {
                5
            }
            clock = 0
        }
        return Triple(possession, clock, quarter)
    }

    private fun updatePlayValues(
        game: Game,
        gamePlay: Play,
        playCall: PlayCall,
        result: Scenario,
        actualResult: ActualResult,
        initialPossession: TeamSide,
        homeScore: Int,
        awayScore: Int,
        runoffTime: Int,
        playTime: Int,
        ballLocation: Int,
        down: Int,
        yardsToGo: Int,
        decryptedDefensiveNumber: String,
        offensiveNumber: String,
        difference: Int,
        yards: Int,
        timeoutUsed: Boolean
    ) : Play {
        var clock = (gamePlay.clock?.minus(runoffTime) ?: handleException(ExceptionType.INVALID_CLOCK)) - (playTime)
        var quarter = gamePlay.gameQuarter ?: handleException(ExceptionType.INVALID_QUARTER)
        var possession = initialPossession

        if(playCall != PlayCall.PUNT && playCall == PlayCall.TWO_POINT) {
            val (updatedPossession, updatedClock, updatedQuarter) = handleEndOfQuarterScenarios(
                game,
                gamePlay,
                actualResult,
                possession,
                clock,
                quarter,
                homeScore,
                awayScore
            )
            possession = updatedPossession
            clock = updatedClock
            quarter = updatedQuarter
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
        gamePlay.timeoutUsed = timeoutUsed
        return gamePlay
    }
}