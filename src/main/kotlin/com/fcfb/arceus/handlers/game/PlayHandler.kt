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
import com.fcfb.arceus.repositories.PlayRepository
import com.fcfb.arceus.service.fcfb.game.RangesService
import com.fcfb.arceus.utils.InvalidActualResultException
import com.fcfb.arceus.utils.InvalidPlayTypeException
import com.fcfb.arceus.utils.InvalidScenarioException
import com.fcfb.arceus.utils.ResultNotFoundException
import org.springframework.stereotype.Component

@Component
class PlayHandler(
    private var playRepository: PlayRepository,
    private var rangesService: RangesService,
    private var gameHandler: GameHandler,
    private var statsHandler: StatsHandler,
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
        allPlays: List<Play>,
        game: Game,
        playCall: PlayCall,
        runoffType: RunoffType,
        offensiveTimeoutCalled: Boolean,
        offensiveNumber: String,
        decryptedDefensiveNumber: String,
    ): Play {
        if (game.currentPlayType != PlayType.NORMAL) {
            throw InvalidPlayTypeException()
        }
        val difference = gameHandler.getDifference(offensiveNumber.toInt(), decryptedDefensiveNumber.toInt())
        var possession = gamePlay.possession
        val (offensivePlaybook, defensivePlaybook) = getPlaybooks(game, possession)
        val (timeoutUsed, homeTimeoutCalled, awayTimeoutCalled) = getTimeoutUsage(game, gamePlay, offensiveTimeoutCalled)

        val resultInformation = rangesService.getNormalResult(playCall, offensivePlaybook, defensivePlaybook, difference)
        val result = resultInformation.result ?: throw ResultNotFoundException()
        val playTime = resultInformation.playTime

        // Determine runoff time between plays
        val clockStopped = game.clockStopped
        val runoffTime = getRunoffTime(clockStopped, gamePlay.clock, timeoutUsed, playCall, runoffType, offensivePlaybook)

        var homeScore = game.homeScore
        var awayScore = game.awayScore
        var ballLocation = game.ballLocation
        var down = game.down
        var yardsToGo = game.yardsToGo
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
                yards = 100 - ballLocation
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
                ballLocation -= 3
                if (ballLocation <= 0) {
                    ballLocation = 1
                }
                down += 1
                if (down > 4) {
                    actualResult = ActualResult.TURNOVER_ON_DOWNS
                    ballLocation = 100 - ballLocation
                }
            }
            else -> {
                yards = result.description.toInt()
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
                        actualResult =
                            if (yards > 0) {
                                ActualResult.GAIN
                            } else if (yards == 0) {
                                ActualResult.NO_GAIN
                            } else {
                                ActualResult.LOSS
                            }
                    }
                }
            }
        }
        when (actualResult) {
            ActualResult.TURNOVER_ON_DOWNS, ActualResult.TURNOVER -> {
                possession =
                    if (possession == TeamSide.HOME) {
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
            ActualResult.LOSS -> {}
            else -> throw InvalidActualResultException()
        }

        return updatePlayValues(
            game,
            allPlays,
            gamePlay,
            playCall,
            result,
            actualResult,
            possession,
            homeScore,
            awayScore,
            runoffTime,
            playTime,
            ballLocation,
            down,
            yardsToGo,
            decryptedDefensiveNumber,
            offensiveNumber,
            difference,
            yards,
            timeoutUsed,
            homeTimeoutCalled,
            awayTimeoutCalled,
        )
    }

    /**
     * Runs the field goal play, returns the updated gamePlay
     *
     */
    fun runFieldGoalPlay(
        gamePlay: Play,
        allPlays: List<Play>,
        game: Game,
        playCall: PlayCall,
        runoffType: RunoffType,
        offensiveTimeoutCalled: Boolean,
        offensiveNumber: String,
        decryptedDefensiveNumber: String,
    ): Play {
        if (game.currentPlayType != PlayType.NORMAL) {
            throw InvalidPlayTypeException()
        }

        val difference = gameHandler.getDifference(offensiveNumber.toInt(), decryptedDefensiveNumber.toInt())
        var possession = gamePlay.possession
        var ballLocation = 100 - game.ballLocation
        val (offensivePlaybook, _) = getPlaybooks(game, possession)
        val (timeoutUsed, homeTimeoutCalled, awayTimeoutCalled) = getTimeoutUsage(game, gamePlay, offensiveTimeoutCalled)

        val resultInformation = rangesService.getFieldGoalResult(playCall, ballLocation + 17, difference)
        val result = resultInformation.result ?: throw ResultNotFoundException()
        val playTime = resultInformation.playTime
        ballLocation = game.ballLocation

        // Determine runoff time between plays
        val clockStopped = game.clockStopped
        val runoffTime = getRunoffTime(clockStopped, gamePlay.clock, timeoutUsed, playCall, runoffType, offensivePlaybook)

        var homeScore = game.homeScore
        var awayScore = game.awayScore
        val down: Int?
        val yardsToGo: Int?
        val actualResult: ActualResult
        when (result) {
            Scenario.KICK_SIX -> {
                actualResult = ActualResult.KICK_SIX
                ballLocation = 97
            }
            Scenario.BLOCKED_FIELD_GOAL -> {
                actualResult = ActualResult.BLOCKED
                ballLocation = 100 - ballLocation
            }
            Scenario.NO_GOOD -> {
                actualResult = ActualResult.NO_GOOD
                ballLocation = 100 - ballLocation
            }
            Scenario.GOOD -> {
                actualResult = ActualResult.GOOD
                ballLocation = 35
            }
            else -> throw InvalidScenarioException()
        }
        when (actualResult) {
            ActualResult.KICK_SIX -> {
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
                possession =
                    if (possession == TeamSide.HOME) {
                        TeamSide.AWAY
                    } else {
                        TeamSide.HOME
                    }
                down = 1
                yardsToGo = 10
            }
            ActualResult.NO_GOOD -> {
                possession =
                    if (possession == TeamSide.HOME) {
                        TeamSide.AWAY
                    } else {
                        TeamSide.HOME
                    }
                down = 1
                yardsToGo = 10
            }
            ActualResult.GOOD -> {
                if (possession == TeamSide.HOME) {
                    homeScore += 3
                } else {
                    awayScore += 3
                }
                down = 1
                yardsToGo = 10
            }
            else -> throw InvalidActualResultException()
        }

        return updatePlayValues(
            game,
            allPlays,
            gamePlay,
            playCall,
            result,
            actualResult,
            possession,
            homeScore,
            awayScore,
            runoffTime,
            playTime,
            ballLocation,
            down,
            yardsToGo,
            decryptedDefensiveNumber,
            offensiveNumber,
            difference,
            0,
            timeoutUsed,
            homeTimeoutCalled,
            awayTimeoutCalled,
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
        allPlays: List<Play>,
        game: Game,
        playCall: PlayCall,
        runoffType: RunoffType,
        offensiveTimeoutCalled: Boolean,
        offensiveNumber: String,
        decryptedDefensiveNumber: String,
    ): Play {
        if (game.currentPlayType != PlayType.NORMAL) {
            throw InvalidPlayTypeException()
        }

        val difference = gameHandler.getDifference(offensiveNumber.toInt(), decryptedDefensiveNumber.toInt())
        var possession = gamePlay.possession
        var ballLocation = game.ballLocation
        val (offensivePlaybook, _) = getPlaybooks(game, possession)
        val (timeoutUsed, homeTimeoutCalled, awayTimeoutCalled) = getTimeoutUsage(game, gamePlay, offensiveTimeoutCalled)

        val resultInformation = rangesService.getPuntResult(playCall, ballLocation, difference)
        val result = resultInformation.result ?: throw ResultNotFoundException()
        val playTime = resultInformation.playTime

        // Determine runoff time between plays
        val clockStopped = game.clockStopped
        val runoffTime = getRunoffTime(clockStopped, gamePlay.clock, timeoutUsed, playCall, runoffType, offensivePlaybook)

        var homeScore = game.homeScore
        var awayScore = game.awayScore
        var down = game.down
        var yardsToGo = game.yardsToGo
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
            Scenario.FIVE_YARD_PUNT, Scenario.TEN_YARD_PUNT, Scenario.FIFTEEN_YARD_PUNT, Scenario.TWENTY_YARD_PUNT,
            Scenario.TWENTY_FIVE_YARD_PUNT, Scenario.THIRTY_YARD_PUNT, Scenario.THIRTY_FIVE_YARD_PUNT,
            Scenario.FORTY_YARD_PUNT, Scenario.FORTY_FIVE_YARD_PUNT, Scenario.FIFTY_YARD_PUNT, Scenario.FIFTY_FIVE_YARD_PUNT,
            Scenario.SIXTY_YARD_PUNT, Scenario.SIXTY_FIVE_YARD_PUNT, Scenario.SEVENTY_YARD_PUNT,
            -> {
                actualResult = ActualResult.PUNT
                ballLocation = 100 - (ballLocation + result.description.substringBefore(" YARD PUNT").toInt())
            }
            Scenario.FUMBLE -> {
                actualResult = ActualResult.MUFFED_PUNT
                ballLocation += 40
                if (ballLocation >= 100) {
                    ballLocation = 99
                }
            }
            Scenario.TOUCHDOWN -> {
                actualResult = ActualResult.PUNT_TEAM_TOUCHDOWN
                ballLocation = 97
            }
            else -> throw InvalidScenarioException()
        }
        when (actualResult) {
            ActualResult.PUNT_RETURN_TOUCHDOWN -> {
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
                possession =
                    if (possession == TeamSide.HOME) {
                        TeamSide.AWAY
                    } else {
                        TeamSide.HOME
                    }
                down = 1
                yardsToGo = 10
            }
            ActualResult.PUNT -> {
                possession =
                    if (possession == TeamSide.HOME) {
                        TeamSide.AWAY
                    } else {
                        TeamSide.HOME
                    }
                down = 1
                yardsToGo = 10
            }
            ActualResult.MUFFED_PUNT -> {
                possession =
                    if (possession == TeamSide.HOME) {
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
            else -> throw InvalidActualResultException()
        }

        return updatePlayValues(
            game,
            allPlays,
            gamePlay,
            playCall,
            result,
            actualResult,
            possession,
            homeScore,
            awayScore,
            runoffTime,
            playTime,
            ballLocation,
            down,
            yardsToGo,
            decryptedDefensiveNumber,
            offensiveNumber,
            difference,
            0,
            timeoutUsed,
            homeTimeoutCalled,
            awayTimeoutCalled,
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
        allPlays: List<Play>,
        game: Game,
        playCall: PlayCall,
        offensiveNumber: String,
        decryptedDefensiveNumber: String,
    ): Play {
        if (game.currentPlayType != PlayType.KICKOFF) {
            throw InvalidPlayTypeException()
        }

        val difference = gameHandler.getDifference(offensiveNumber.toInt(), decryptedDefensiveNumber.toInt())
        var possession = gamePlay.possession
        val resultInformation = rangesService.getNonNormalResult(playCall, difference)
        val result = resultInformation.result ?: throw ResultNotFoundException()
        var homeScore = game.homeScore
        var awayScore = game.awayScore
        val ballLocation: Int
        val down = 1
        val yardsToGo = 10
        val playTime = resultInformation.playTime
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
            Scenario.THIRTY_FIVE_YARD_RETURN, Scenario.FORTY_YARD_RETURN, Scenario.FORTY_FIVE_YARD_RETURN,
            Scenario.FIFTY_YARD_RETURN, Scenario.SIXTY_FIVE_YARD_RETURN,
            -> {
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
            Scenario.FAILED_ONSIDE -> {
                actualResult = ActualResult.FAILED_ONSIDE
                ballLocation = 55
            }
            else -> throw InvalidScenarioException()
        }
        when (actualResult) {
            ActualResult.KICKING_TEAM_TOUCHDOWN ->
                if (possession == TeamSide.HOME) {
                    homeScore += 6
                } else {
                    awayScore += 6
                }
            ActualResult.KICKOFF, ActualResult.FAILED_ONSIDE ->
                possession =
                    if (possession == TeamSide.HOME) {
                        TeamSide.AWAY
                    } else {
                        TeamSide.HOME
                    }
            ActualResult.RETURN_TOUCHDOWN ->
                if (possession == TeamSide.HOME) {
                    possession = TeamSide.AWAY
                    awayScore += 6
                } else {
                    possession = TeamSide.HOME
                    homeScore += 6
                }
            ActualResult.SUCCESSFUL_ONSIDE, ActualResult.MUFFED_KICK ->
                possession =
                    if (possession == TeamSide.HOME) {
                        TeamSide.HOME
                    } else {
                        TeamSide.AWAY
                    }
            else -> throw InvalidActualResultException()
        }

        return updatePlayValues(
            game,
            allPlays,
            gamePlay,
            playCall,
            result,
            actualResult,
            possession,
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
            false,
            false,
            false,
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
        allPlays: List<Play>,
        game: Game,
        playCall: PlayCall,
        offensiveNumber: String,
        decryptedDefensiveNumber: String,
    ): Play {
        if (game.currentPlayType != PlayType.PAT) {
            throw InvalidPlayTypeException()
        }

        val difference = gameHandler.getDifference(offensiveNumber.toInt(), decryptedDefensiveNumber.toInt())
        val possession = gamePlay.possession
        val resultInformation = rangesService.getNonNormalResult(playCall, difference)
        val result = resultInformation.result ?: throw ResultNotFoundException()
        var homeScore = game.homeScore
        var awayScore = game.awayScore
        val ballLocation = 35
        val down = 1
        val yardsToGo = 10
        val actualResult =
            when (result) {
                Scenario.GOOD -> ActualResult.GOOD
                Scenario.NO_GOOD -> ActualResult.NO_GOOD
                Scenario.SUCCESS -> ActualResult.SUCCESS
                Scenario.FAILED -> ActualResult.FAILED
                Scenario.DEFENSE_TWO_POINT -> ActualResult.DEFENSE_TWO_POINT
                else -> throw InvalidScenarioException()
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
                throw InvalidActualResultException()
            }
        }

        return updatePlayValues(
            game,
            allPlays,
            gamePlay,
            playCall,
            result,
            actualResult,
            possession,
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
            false,
            false,
            false,
        )
    }

    private fun getRunoffTime(
        clockStopped: Boolean?,
        clock: Int,
        timeoutUsed: Boolean,
        playCall: PlayCall,
        runoffType: RunoffType,
        offensivePlaybook: OffensivePlaybook,
    ): Int {
        // Determine runoff time between plays
        return if (!clockStopped!! && !timeoutUsed) {
            when {
                playCall == PlayCall.SPIKE -> 3
                playCall == PlayCall.KNEEL -> 30
                runoffType == RunoffType.CHEW -> 30
                runoffType == RunoffType.HURRY -> 7
                runoffType == RunoffType.FINAL ->
                    if (clock <= 7) {
                        clock
                    } else if (clock > 30) {
                        30
                    } else {
                        clock - 1
                    }
                offensivePlaybook == OffensivePlaybook.PRO -> 15
                offensivePlaybook == OffensivePlaybook.AIR_RAID -> 10
                offensivePlaybook == OffensivePlaybook.FLEXBONE -> 20
                offensivePlaybook == OffensivePlaybook.SPREAD -> 13
                offensivePlaybook == OffensivePlaybook.WEST_COAST -> 17
                else -> 0
            }
        } else {
            0
        }
    }

    private fun getPlaybooks(
        game: Game,
        possession: TeamSide,
    ): Pair<OffensivePlaybook, DefensivePlaybook> {
        return if (possession == TeamSide.HOME) {
            game.homeOffensivePlaybook to game.awayDefensivePlaybook
        } else {
            game.awayOffensivePlaybook to game.homeDefensivePlaybook
        }
    }

    private fun handleEndOfQuarterNormalScenarios(
        game: Game,
        gamePlay: Play,
        actualResult: ActualResult,
        initialPossession: TeamSide,
        initialClock: Int,
        initialQuarter: Int,
        homeScore: Int,
        awayScore: Int,
        playTime: Int,
    ): Triple<TeamSide, Int, Int> {
        var possession = initialPossession
        var clock = initialClock
        var quarter = initialQuarter

        // If quarter is over but game is not over
        if (clock <= 0 && !isScoringPlay(actualResult) && quarter < 4) {
            quarter += 1
            clock = 420 - playTime
            if (quarter == 3) {
                possession = gameHandler.handleHalfTimePossessionChange(game)
                clock = 420
            }
        } else if (clock <= 0 && !isScoringPlay(actualResult) && gamePlay.quarter == 4) {
            // Check if game is over or needs to go to OT
            quarter =
                if (homeScore > awayScore || awayScore > homeScore) {
                    0
                } else {
                    5
                }
            clock = 0
        } else if (clock <= 0 && isScoringPlay(actualResult)
        ) {
            clock = 0
        } else if (clock > 0) {
            clock = initialClock - playTime
            if (clock <= 0 && !isScoringPlay(actualResult) && quarter < 4) {
                quarter += 1
                clock = 420
                if (quarter == 3) {
                    possession = gameHandler.handleHalfTimePossessionChange(game)
                }
            } else if (clock <= 0 && !isScoringPlay(actualResult) && gamePlay.quarter == 4) {
                // Check if game is over or needs to go to OT
                quarter =
                    if (homeScore > awayScore || awayScore > homeScore) {
                        0
                    } else {
                        5
                    }
                clock = 0
            } else if (clock <= 0 && isScoringPlay(actualResult)) {
                clock = 0
            }
        }
        return Triple(possession, clock, quarter)
    }

    private fun handleEndOfQuarterPATScenarios(
        game: Game,
        gamePlay: Play,
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
            quarter < 4
        ) {
            quarter += 1
            clock = 420
            if (quarter == 3) {
                possession = gameHandler.handleHalfTimePossessionChange(game)
            }
        } else if (
            clock <= 0 &&
            gamePlay.quarter == 4
        ) {
            // Check if game is over or needs to go to OT
            quarter =
                if (homeScore > awayScore || awayScore > homeScore) {
                    0
                } else {
                    5
                }
            clock = 0
        }
        return Triple(possession, clock, quarter)
    }

    private fun getTimeoutUsage(
        game: Game,
        gamePlay: Play,
        offensiveTimeoutCalled: Boolean,
    ): Triple<Boolean, Boolean, Boolean> {
        val clockStopped = game.clockStopped
        val defensiveTimeoutCalled = gamePlay.timeoutUsed
        var timeoutUsed = false
        var homeTimeoutCalled = false
        var awayTimeoutCalled = false
        if (defensiveTimeoutCalled && gamePlay.possession == TeamSide.HOME && game.awayTimeouts > 0 && !clockStopped) {
            timeoutUsed = true
            awayTimeoutCalled = true
        } else if (defensiveTimeoutCalled && gamePlay.possession == TeamSide.AWAY && game.homeTimeouts > 0 && !clockStopped) {
            timeoutUsed = true
            homeTimeoutCalled = true
        } else if (offensiveTimeoutCalled && gamePlay.possession == TeamSide.HOME && game.homeTimeouts > 0 && !clockStopped) {
            timeoutUsed = true
            homeTimeoutCalled = true
        } else if (offensiveTimeoutCalled && gamePlay.possession == TeamSide.AWAY && game.awayTimeouts > 0 && !clockStopped) {
            timeoutUsed = true
            awayTimeoutCalled = true
        } else if (offensiveTimeoutCalled && gamePlay.possession == TeamSide.HOME) {
            homeTimeoutCalled = true
        } else if (offensiveTimeoutCalled && gamePlay.possession == TeamSide.AWAY) {
            awayTimeoutCalled = true
        } else if (defensiveTimeoutCalled && gamePlay.possession == TeamSide.HOME) {
            awayTimeoutCalled = true
        } else if (defensiveTimeoutCalled && gamePlay.possession == TeamSide.AWAY) {
            homeTimeoutCalled = true
        }
        return Triple(timeoutUsed, homeTimeoutCalled, awayTimeoutCalled)
    }

    private fun updatePlayValues(
        game: Game,
        allPlays: List<Play>,
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
        timeoutUsed: Boolean,
        homeTimeoutCalled: Boolean,
        awayTimeoutcalled: Boolean,
    ): Play {
        var clock = gamePlay.clock.minus(runoffTime)
        var quarter = gamePlay.quarter
        var possession = initialPossession

        if (playCall != PlayCall.PAT && playCall != PlayCall.TWO_POINT) {
            val (updatedPossession, updatedClock, updatedQuarter) =
                handleEndOfQuarterNormalScenarios(
                    game,
                    gamePlay,
                    actualResult,
                    possession,
                    clock,
                    quarter,
                    homeScore,
                    awayScore,
                    playTime,
                )
            possession = updatedPossession
            clock = updatedClock
            quarter = updatedQuarter
        } else {
            // Handle end of quarter PAT scenarios
            val (updatedPossession, updatedClock, updatedQuarter) =
                handleEndOfQuarterPATScenarios(
                    game,
                    gamePlay,
                    possession,
                    clock,
                    quarter,
                    homeScore,
                    awayScore,
                )
            possession = updatedPossession
            clock = updatedClock
            quarter = updatedQuarter
        }

        // Update gamePlay values
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
        gamePlay.difference = difference
        gamePlay.timeoutUsed = timeoutUsed
        gamePlay.playFinished = true

        if (initialPossession == TeamSide.HOME) {
            gamePlay.offensiveTimeoutCalled = homeTimeoutCalled
            gamePlay.defensiveTimeoutCalled = awayTimeoutcalled
        } else {
            gamePlay.offensiveTimeoutCalled = awayTimeoutcalled
            gamePlay.defensiveTimeoutCalled = homeTimeoutCalled
        }

        // Save the play and then update the game and stats
        playRepository.save(gamePlay)

        gameHandler.updateGameInformation(
            game,
            gamePlay,
            homeScore,
            awayScore,
            possession,
            quarter,
            clock,
            ballLocation,
            down,
            yardsToGo,
            homeTimeoutCalled,
            awayTimeoutcalled,
            timeoutUsed,
        )

        statsHandler.updateGameStats(
            game,
            allPlays,
            gamePlay,
        )

        return gamePlay
    }

    private fun isScoringPlay(actualResult: ActualResult): Boolean {
        return actualResult == ActualResult.TOUCHDOWN ||
            actualResult == ActualResult.TURNOVER_TOUCHDOWN ||
            actualResult == ActualResult.RETURN_TOUCHDOWN ||
            actualResult == ActualResult.KICKING_TEAM_TOUCHDOWN ||
            actualResult == ActualResult.PUNT_RETURN_TOUCHDOWN ||
            actualResult == ActualResult.PUNT_TEAM_TOUCHDOWN ||
            actualResult == ActualResult.KICK_SIX
    }
}
