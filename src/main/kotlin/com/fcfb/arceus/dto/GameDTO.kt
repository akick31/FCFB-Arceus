package com.fcfb.arceus.dto

import com.fcfb.arceus.domain.FinishedGame
import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.ActualResult
import com.fcfb.arceus.domain.Game.GameStatus
import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.PlayType
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.domain.Game.Result
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.models.ExceptionType
import com.fcfb.arceus.models.handleException
import com.fcfb.arceus.utils.GameUtils
import org.springframework.stereotype.Component

@Component
class GameDTO(
    private val gameUtils: GameUtils
) {
    fun updateGameInformation(
        game: Game,
        play: Play,
        playCall: PlayCall,
        clockStopped: Boolean,
        offensiveTimeout: Boolean,
        defensiveTimeout: Boolean,
        waitingOn: TeamSide
    ): Game {
        // Update if the clock is stopped
        game.clockStopped = play.playCall == PlayCall.SPIKE || play.result == Result.INCOMPLETE ||
            play.actualResult == ActualResult.TURNOVER_ON_DOWNS ||
            play.actualResult == ActualResult.TOUCHDOWN || play.playCall == PlayCall.FIELD_GOAL ||
            play.playCall == PlayCall.PAT || play.playCall == PlayCall.KICKOFF_NORMAL ||
            play.playCall == PlayCall.KICKOFF_ONSIDE || play.playCall == PlayCall.KICKOFF_SQUIB ||
            play.playCall == PlayCall.PUNT || play.actualResult == ActualResult.TURNOVER ||
            play.actualResult == ActualResult.TURNOVER_TOUCHDOWN || play.actualResult == ActualResult.SAFETY

        // Update timeouts
        val possession = game.possession
        if (!clockStopped) {
            if (possession == TeamSide.HOME && defensiveTimeout) {
                game.awayTimeouts = game.awayTimeouts!! - 1
            } else if (possession == TeamSide.HOME && offensiveTimeout) {
                game.homeTimeouts = game.homeTimeouts!! - 1
            } else if (possession == TeamSide.AWAY && defensiveTimeout) {
                game.homeTimeouts = game.homeTimeouts!! - 1
            } else if (possession == TeamSide.AWAY && offensiveTimeout) {
                game.awayTimeouts = game.awayTimeouts!! - 1
            }
        }

        // If game quarter is 0, then the game is over
        if (play.gameQuarter == 0) {
            game.gameStatus = GameStatus.FINAL
        } else if (play.gameQuarter!! >= 5) {
            game.gameStatus = GameStatus.END_OF_REGULATION
        }

        // Update the play type
        if (play.actualResult == ActualResult.TOUCHDOWN || play.actualResult == ActualResult.TURNOVER_TOUCHDOWN) {
            game.currentPlayType = PlayType.PAT
        } else if (play.actualResult == ActualResult.SAFETY ||
            (playCall == PlayCall.FIELD_GOAL && play.result == Result.GOOD) || playCall == PlayCall.PAT || playCall == PlayCall.TWO_POINT
        ) {
            game.currentPlayType = PlayType.KICKOFF
        } else {
            game.currentPlayType = PlayType.NORMAL
        }

        // Update everything else
        game.homeScore = play.homeScore ?: handleException(ExceptionType.INVALID_HOME_SCORE)
        game.awayScore = play.awayScore ?: handleException(ExceptionType.INVALID_AWAY_SCORE)
        game.possession = play.possession
        game.quarter = play.gameQuarter ?: handleException(ExceptionType.INVALID_QUARTER)
        game.clock = gameUtils.convertClockToString(play.clock ?: 420)
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
