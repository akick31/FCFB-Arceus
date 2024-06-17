package com.fcfb.arceus.dto

import com.fcfb.arceus.domain.GamePlaysEntity
import com.fcfb.arceus.domain.GameStatsEntity
import com.fcfb.arceus.domain.OngoingGamesEntity
import com.fcfb.arceus.models.ExceptionType
import com.fcfb.arceus.models.game.Game.ActualResult
import com.fcfb.arceus.models.game.Game.PlayType
import com.fcfb.arceus.models.game.Game.Play
import com.fcfb.arceus.models.game.Game.Possession
import com.fcfb.arceus.models.game.Game.Result
import com.fcfb.arceus.models.handleException
import com.fcfb.arceus.utils.GameUtils
import org.springframework.stereotype.Component

@Component
class GameDTO(
    private val gameUtils: GameUtils
) {
    fun updateGameInformation(
        game: OngoingGamesEntity,
        play: GamePlaysEntity,
        playCall: Play,
        clockStopped: Boolean,
        offensiveTimeout: Boolean,
        defensiveTimeout: Boolean
    ): OngoingGamesEntity {

        // Update if the clock is stopped
        game.clockStopped = play.play == Play.SPIKE || play.result == Result.INCOMPLETE ||
                play.actualResult == ActualResult.TURNOVER_ON_DOWNS ||
                play.actualResult == ActualResult.TOUCHDOWN || play.play == Play.FIELD_GOAL ||
                play.play == Play.PAT || play.play == Play.KICKOFF_NORMAL ||
                play.play == Play.KICKOFF_ONSIDE || play.play == Play.KICKOFF_SQUIB ||
                play.play == Play.PUNT || play.actualResult == ActualResult.TURNOVER ||
                play.actualResult == ActualResult.TURNOVER_TOUCHDOWN || play.actualResult == ActualResult.SAFETY

        // Update timeouts
        val possession: Possession? = game.possession
        if (!clockStopped) {
            if (possession == Possession.HOME && defensiveTimeout) {
                game.awayTimeouts = game.awayTimeouts!! - 1
            } else if (possession == Possession.HOME && offensiveTimeout) {
                game.homeTimeouts = game.homeTimeouts!! - 1
            } else if (possession == Possession.AWAY && defensiveTimeout) {
                game.homeTimeouts = game.homeTimeouts!! - 1
            } else if (possession == Possession.AWAY && offensiveTimeout) {
                game.awayTimeouts = game.awayTimeouts!! - 1
            }
        }

        // If game quarter is 0, then the game is over
        if (play.gameQuarter == 0) {
            game.final = true
        } else if (play.gameQuarter!! >= 5) {
            game.ot = true
        }

        // Update the play type
        if (play.actualResult == ActualResult.TOUCHDOWN || play.actualResult == ActualResult.TURNOVER_TOUCHDOWN) {
            game.currentPlayType = PlayType.PAT
        } else if (play.actualResult == ActualResult.SAFETY ||
            (playCall == Play.FIELD_GOAL && play.result == Result.GOOD) || playCall == Play.PAT || playCall == Play.TWO_POINT
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
        return game
    }

    fun updateGameStats(stats: GameStatsEntity?, play: GamePlaysEntity?): GameStatsEntity? {
        return null
    }
}
