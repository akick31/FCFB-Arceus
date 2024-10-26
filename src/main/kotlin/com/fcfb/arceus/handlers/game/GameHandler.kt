package com.fcfb.arceus.handlers.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.ActualResult
import com.fcfb.arceus.domain.Game.CoinTossChoice
import com.fcfb.arceus.domain.Game.GameStatus
import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.PlayType
import com.fcfb.arceus.domain.Game.Scenario
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.repositories.GameRepository
import com.fcfb.arceus.service.fcfb.game.ScorebugService
import org.springframework.stereotype.Component

@Component
class GameHandler(
    private val gameRepository: GameRepository,
    private val scorebugService: ScorebugService,
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
            play.actualResult == ActualResult.TURNOVER_TOUCHDOWN || play.actualResult == ActualResult.SAFETY

        // Update timeouts
        if (homeTimeoutCalled && timeoutUsed) {
            game.homeTimeouts = game.homeTimeouts!! - 1
        } else if (awayTimeoutCalled && timeoutUsed) {
            game.awayTimeouts = game.awayTimeouts!! - 1
        }

        // If game quarter is 0, then the game is over
        if (quarter == 0) {
            game.quarter = 4
            game.gameStatus = GameStatus.FINAL
        } else if (quarter >= 5) {
            game.gameStatus = GameStatus.END_OF_REGULATION
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
            play.actualResult == ActualResult.KICKING_TEAM_TOUCHDOWN ||
            play.actualResult == ActualResult.RETURN_TOUCHDOWN
        ) {
            game.currentPlayType = PlayType.PAT
        } else if (play.actualResult == ActualResult.SAFETY ||
            (play.playCall == PlayCall.FIELD_GOAL && play.result == Scenario.GOOD) ||
            play.playCall == PlayCall.PAT ||
            play.playCall == PlayCall.TWO_POINT
        ) {
            game.currentPlayType = PlayType.KICKOFF
        } else {
            game.currentPlayType = PlayType.NORMAL
        }

        // Update the current game status
        if (game.gameStatus == GameStatus.OPENING_KICKOFF) {
            game.gameStatus = GameStatus.IN_PROGRESS
        }

        // Handle halftime
        if (quarter == 3 && clock == 420 && game.gameStatus != GameStatus.HALFTIME) {
            game.homeTimeouts = 3
            game.awayTimeouts = 3
            game.gameStatus = GameStatus.HALFTIME
            game.currentPlayType = PlayType.KICKOFF
            game.ballLocation = 35
        } else if (game.gameStatus == GameStatus.HALFTIME) {
            game.gameStatus = GameStatus.IN_PROGRESS
        }

        // Update everything else
        game.homeScore = homeScore
        game.awayScore = awayScore
        game.possession = possession
        game.quarter = quarter
        game.clock = convertClockToString(clock)
        game.ballLocation = ballLocation
        game.down = down
        game.yardsToGo = yardsToGo
        game.winProbability = play.winProbability
        game.numPlays = play.playNumber
        game.waitingOn = waitingOn

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
        var difference = Math.abs(defesiveNumber - offensiveNumber)
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

    fun handleHalfTimePossessionChange(game: Game): TeamSide? {
        var possession: TeamSide? = null
        if (game.coinTossWinner == TeamSide.HOME && game.coinTossChoice == CoinTossChoice.DEFER) {
            possession = TeamSide.AWAY
        } else if (game.coinTossWinner == TeamSide.HOME && game.coinTossChoice == CoinTossChoice.RECEIVE) {
            possession = TeamSide.HOME
        } else if (game.coinTossWinner == TeamSide.AWAY && game.coinTossChoice == CoinTossChoice.DEFER) {
            possession = TeamSide.HOME
        } else if (game.coinTossWinner == TeamSide.AWAY && game.coinTossChoice == CoinTossChoice.RECEIVE) {
            possession = TeamSide.AWAY
        }
        return possession
    } // TODO Win probability and ELO rating methods
}
