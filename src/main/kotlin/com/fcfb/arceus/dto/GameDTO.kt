package com.fcfb.arceus.dto

import com.fcfb.arceus.domain.FinishedGame
import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.ActualResult
import com.fcfb.arceus.domain.Game.GameStatus
import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.PlayType
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.domain.Game.Scenario
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.handlers.game.GameHandler
import com.fcfb.arceus.models.ExceptionType
import com.fcfb.arceus.models.handleException
import org.springframework.stereotype.Component

@Component
class GameDTO(
    private val gameHandler: GameHandler
) {
    fun updateGameInformation(
        game: Game,
        play: Play,
        playCall: PlayCall,
        clockStopped: Boolean,
        homeTimeoutCalled: Boolean,
        awayTimeoutCalled: Boolean
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
        if (homeTimeoutCalled) {
            game.homeTimeouts = game.homeTimeouts!! - 1
        } else if (awayTimeoutCalled) {
            game.awayTimeouts = game.awayTimeouts!! - 1
        }

        // If game quarter is 0, then the game is over
        if (play.gameQuarter == 0) {
            game.gameStatus = GameStatus.FINAL
        } else if (play.gameQuarter!! >= 5) {
            game.gameStatus = GameStatus.END_OF_REGULATION
        }

        // Update waiting on
        val waitingOn = if (play.possession == TeamSide.HOME){
            TeamSide.AWAY
        } else {
            TeamSide.HOME
        }

        // Update the play type
        if (play.actualResult == ActualResult.TOUCHDOWN || play.actualResult == ActualResult.TURNOVER_TOUCHDOWN ||
            play.actualResult == ActualResult.KICKING_TEAM_TOUCHDOWN || play.actualResult == ActualResult.RETURN_TOUCHDOWN) {
            game.currentPlayType = PlayType.PAT
        } else if (play.actualResult == ActualResult.SAFETY ||
            (playCall == PlayCall.FIELD_GOAL && play.result == Scenario.GOOD) || playCall == PlayCall.PAT || playCall == PlayCall.TWO_POINT
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
        if (game.quarter == 3 && play.clock == 420 && game.gameStatus != GameStatus.HALFTIME) {
            game.gameStatus = GameStatus.HALFTIME
            game.currentPlayType = PlayType.KICKOFF
            game.ballLocation = 35
        } // Change game status back to in progress after first play of the second half
        else if (game.gameStatus == GameStatus.HALFTIME) {
            game.gameStatus = GameStatus.IN_PROGRESS
        }

        // Update everything else
        game.homeScore = play.homeScore ?: handleException(ExceptionType.INVALID_HOME_SCORE)
        game.awayScore = play.awayScore ?: handleException(ExceptionType.INVALID_AWAY_SCORE)
        game.possession = play.possession
        game.quarter = play.gameQuarter ?: handleException(ExceptionType.INVALID_QUARTER)
        game.clock = gameHandler.convertClockToString(play.clock ?: 420)
        game.ballLocation = play.ballLocation
        game.down = play.down ?: handleException(ExceptionType.INVALID_DOWN)
        game.yardsToGo = play.yardsToGo ?: handleException(ExceptionType.INVALID_YARDS_TO_GO)
        game.numPlays = play.playNumber
        game.waitingOn = waitingOn
        return game
    }

    fun updateGameStats(stats: FinishedGame?, play: com.fcfb.arceus.domain.Play?): FinishedGame? {
        return null
    }
}
