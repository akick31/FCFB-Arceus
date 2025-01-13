package com.fcfb.arceus.handlers.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.ActualResult
import com.fcfb.arceus.domain.Game.CoinTossChoice
import com.fcfb.arceus.domain.Game.GameStatus
import com.fcfb.arceus.domain.Game.GameType
import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.PlayType
import com.fcfb.arceus.domain.Game.Scenario
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.repositories.GameRepository
import com.fcfb.arceus.service.fcfb.SeasonService
import com.fcfb.arceus.service.fcfb.TeamService
import com.fcfb.arceus.service.fcfb.UserService
import com.fcfb.arceus.service.fcfb.game.GameService
import com.fcfb.arceus.service.fcfb.game.ScorebugService
import com.fcfb.arceus.utils.InvalidHalfTimePossessionChangeException
import org.springframework.stereotype.Component
import kotlin.math.abs

@Component
class GameHandler(
    private val gameRepository: GameRepository,
    private val scorebugService: ScorebugService,
    private val gameService: GameService,
    private val teamService: TeamService,
    private val userService: UserService,
    private val seasonService: SeasonService,
) {
    fun updateGameInformation(
        game: Game,
        play: Play,
        homeScore: Int,
        awayScore: Int,
        possession: TeamSide,
        quarter: Int,
        clock: Int,
        ballLocation: Int,
        down: Int,
        yardsToGo: Int,
        homeTimeoutCalled: Boolean,
        awayTimeoutCalled: Boolean,
        timeoutUsed: Boolean,
    ): Game {
        // Update if the clock is stopped
        game.clockStopped = play.playCall == PlayCall.SPIKE || play.result == Scenario.INCOMPLETE ||
            play.actualResult == ActualResult.TURNOVER_ON_DOWNS ||
            play.actualResult == ActualResult.TOUCHDOWN || play.playCall == PlayCall.FIELD_GOAL ||
            play.playCall == PlayCall.PAT || play.playCall == PlayCall.KICKOFF_NORMAL ||
            play.playCall == PlayCall.KICKOFF_ONSIDE || play.playCall == PlayCall.KICKOFF_SQUIB ||
            play.playCall == PlayCall.PUNT || play.actualResult == ActualResult.TURNOVER ||
            play.actualResult == ActualResult.TURNOVER_TOUCHDOWN || play.actualResult == ActualResult.SAFETY ||
            game.gameStatus == GameStatus.OVERTIME || game.gameStatus == GameStatus.HALFTIME

        // Update timeouts
        if (homeTimeoutCalled && timeoutUsed) {
            game.homeTimeouts -= 1
        } else if (awayTimeoutCalled && timeoutUsed) {
            game.awayTimeouts -= 1
        }

        // Update waiting on
        val waitingOn =
            if (possession == TeamSide.HOME) {
                TeamSide.AWAY
            } else {
                TeamSide.HOME
            }

        // Update the play type
        if (play.actualResult == ActualResult.TOUCHDOWN ||
            play.actualResult == ActualResult.TURNOVER_TOUCHDOWN ||
            play.actualResult == ActualResult.RETURN_TOUCHDOWN ||
            play.actualResult == ActualResult.KICKING_TEAM_TOUCHDOWN ||
            play.actualResult == ActualResult.PUNT_RETURN_TOUCHDOWN ||
            play.actualResult == ActualResult.PUNT_TEAM_TOUCHDOWN ||
            play.actualResult == ActualResult.KICK_SIX
        ) {
            game.currentPlayType = PlayType.PAT
        } else if (play.actualResult == ActualResult.SAFETY ||
            (play.playCall == PlayCall.FIELD_GOAL && play.result == Scenario.GOOD) ||
            play.playCall == PlayCall.PAT ||
            play.playCall == PlayCall.TWO_POINT
        ) {
            if (game.gameStatus == GameStatus.OVERTIME) {
                game.currentPlayType = PlayType.NORMAL
            } else {
                game.currentPlayType = PlayType.KICKOFF
            }
        } else {
            game.currentPlayType = PlayType.NORMAL
        }

        // Update the quarter/overtime stuff
        if (quarter == 0) {
            // End of game
            game.quarter = 4
            game.clock = "0:00"
            game.clockStopped = true
            game.gameStatus = GameStatus.FINAL
            endGame(game)
        } else if (game.gameStatus == GameStatus.OVERTIME) {
            // Overtime
            game.clock = "0:00"
            if (play.actualResult == ActualResult.GOOD ||
                play.actualResult == ActualResult.NO_GOOD ||
                play.actualResult == ActualResult.SUCCESS ||
                play.actualResult == ActualResult.FAILED ||
                play.actualResult == ActualResult.DEFENSE_TWO_POINT ||
                play.actualResult == ActualResult.TURNOVER_ON_DOWNS ||
                play.actualResult == ActualResult.TURNOVER
            ) {
                // Handle the end of each half of overtime
                if (game.overtimeHalf == 1) {
                    game.overtimeHalf = 2
                    game.possession =
                        if (game.possession == TeamSide.HOME) {
                            TeamSide.AWAY
                        } else {
                            TeamSide.HOME
                        }
                    game.ballLocation = 75
                    game.down = 1
                    game.yardsToGo = 10
                    game.waitingOn = if (game.possession == TeamSide.HOME) TeamSide.AWAY else TeamSide.HOME
                } else {
                    if (homeScore != awayScore) {
                        // End of game, one team has won
                        game.gameStatus = GameStatus.FINAL
                        endGame(game)
                    } else {
                        game.overtimeHalf = 1
                        game.possession =
                            if (game.possession == TeamSide.HOME) {
                                TeamSide.HOME
                            } else {
                                TeamSide.AWAY
                            }
                        game.ballLocation = 75
                        game.down = 1
                        game.yardsToGo = 10
                        game.quarter += 1
                        game.homeTimeouts = 1
                        game.awayTimeouts = 1
                        game.waitingOn = if (possession == TeamSide.HOME) TeamSide.AWAY else TeamSide.HOME
                    }
                }
            } else {
                game.possession = possession
                game.waitingOn = waitingOn
                game.ballLocation = ballLocation
                game.down = down
                game.yardsToGo = yardsToGo
            }
        } else if (game.gameStatus == GameStatus.OPENING_KICKOFF) {
            // Start of game
            game.gameStatus = GameStatus.IN_PROGRESS
            game.clock = convertClockToString(clock)
            game.possession = possession
            game.quarter = quarter
            game.ballLocation = ballLocation
            game.down = down
            game.yardsToGo = yardsToGo
            game.waitingOn = waitingOn
        } else if (quarter == 3 && clock == 420 && game.gameStatus != GameStatus.HALFTIME) {
            // Halftime
            game.homeTimeouts = 3
            game.awayTimeouts = 3
            game.gameStatus = GameStatus.HALFTIME
            game.currentPlayType = PlayType.KICKOFF
            game.ballLocation = 35
            game.clock = convertClockToString(clock)
            game.possession = possession
            game.quarter = 3
            game.down = 1
            game.yardsToGo = 10
            game.waitingOn = waitingOn
        } else if (game.gameStatus == GameStatus.HALFTIME) {
            // Start of second half
            game.gameStatus = GameStatus.IN_PROGRESS
            game.clock = convertClockToString(clock)
            game.possession = possession
            game.quarter = quarter
            game.ballLocation = ballLocation
            game.down = down
            game.yardsToGo = yardsToGo
            game.waitingOn = waitingOn
        } else if (quarter >= 5 && game.gameStatus == GameStatus.IN_PROGRESS) {
            // Start of Overtime
            game.clock = "0:00"
            game.quarter = quarter
            game.gameStatus = GameStatus.END_OF_REGULATION
            game.ballLocation = 75
            game.down = 1
            game.yardsToGo = 10
            game.overtimeHalf = 1
            game.homeTimeouts = 1
            game.awayTimeouts = 1
        } else {
            // Normal
            game.clock = convertClockToString(clock)
            game.possession = possession
            game.quarter = quarter
            game.ballLocation = ballLocation
            game.down = down
            game.yardsToGo = yardsToGo
            game.waitingOn = waitingOn
        }

        // Update everything else
        game.homeScore = homeScore
        game.awayScore = awayScore
        game.winProbability = play.winProbability
        game.numPlays = play.playNumber
        game.gameTimer = gameService.calculateDelayOfGameTimer()
        game.gameWarned = false

        gameRepository.save(game)
        scorebugService.generateScorebug(game)

        return game
    }

    /**
     * Returns the difference between the offensive and defensive numbers.
     * @param offensiveNumber
     * @param defesiveNumber
     * @return
     */
    fun getDifference(
        offensiveNumber: Int,
        defesiveNumber: Int,
    ): Int {
        var difference = abs(defesiveNumber - offensiveNumber)
        if (difference > 750) {
            difference = 1500 - difference
        }
        return difference
    }

    /**
     * Returns the number of seconds from the clock.
     * @param clock
     * @return
     */
    fun convertClockToSeconds(clock: String): Int {
        val clockArray = clock.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val minutes = clockArray[0].toInt()
        val seconds = clockArray[1].toInt()
        return minutes * 60 + seconds
    }

    /**
     * Returns the clock from the number of seconds.
     * @param seconds
     * @return
     */
    fun convertClockToString(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%d:%02d", minutes, remainingSeconds)
    }

    fun handleHalfTimePossessionChange(game: Game): TeamSide {
        return if (game.coinTossWinner == TeamSide.HOME && game.coinTossChoice == CoinTossChoice.DEFER) {
            TeamSide.AWAY
        } else if (game.coinTossWinner == TeamSide.HOME && game.coinTossChoice == CoinTossChoice.RECEIVE) {
            TeamSide.HOME
        } else if (game.coinTossWinner == TeamSide.AWAY && game.coinTossChoice == CoinTossChoice.DEFER) {
            TeamSide.HOME
        } else if (game.coinTossWinner == TeamSide.AWAY && game.coinTossChoice == CoinTossChoice.RECEIVE) {
            TeamSide.AWAY
        } else {
            throw InvalidHalfTimePossessionChangeException()
        }
    } // TODO Win probability and ELO rating methods

    /**
     * Run through end of game tasks
     */
    private fun endGame(game: Game) {
        if (game.gameType != GameType.SCRIMMAGE) {
            teamService.updateTeamWinsAndLosses(game)
            userService.updateUserWinsAndLosses(game)
        }
        if (game.gameType == GameType.NATIONAL_CHAMPIONSHIP) {
            seasonService.endSeason(game)
        }
    }
}
