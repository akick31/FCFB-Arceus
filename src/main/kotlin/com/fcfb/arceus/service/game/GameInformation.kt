package com.fcfb.arceus.service.game

import com.fcfb.arceus.domain.GamePlaysEntity
import com.fcfb.arceus.domain.OngoingGamesEntity
import org.springframework.stereotype.Component

@Component
class GameInformation(private val gameUtils: GameUtils) {
    fun updateGameInformation(
        game: OngoingGamesEntity,
        play: GamePlaysEntity,
        playCall: String,
        clockStopped: Boolean,
        offensiveTimeout: Boolean,
        defensiveTimeout: Boolean
    ): OngoingGamesEntity {

        // Update if the clock is stopped
        game.clockStopped = play.play == "SPIKE" || play.result == "INCOMPLETE" ||
                play.actualResult == "TURNOVER ON DOWNS" ||
                play.actualResult == "TOUCHDOWN" || play.play == "FIELD GOAL" ||
                play.play == "PAT" || play.play == "KICKOFF NORMAL" ||
                play.play == "KICKOFF ONSIDE" || play.play == "KICKOFF SQUIB" ||
                play.play == "PUNT" || play.actualResult == "TURNOVER" ||
                play.actualResult == "TURNOVER TOUCHDOWN" || play.actualResult == "SAFETY"

        // Update timeouts
        val possession: String? = game.possession
        if (!clockStopped) {
            if (possession == "home" && defensiveTimeout) {
                game.awayTimeouts = game.awayTimeouts - 1
            } else if (possession == "home" && offensiveTimeout) {
                game.homeTimeouts = game.homeTimeouts - 1
            } else if (possession == "away" && defensiveTimeout) {
                game.homeTimeouts = game.homeTimeouts - 1
            } else if (possession == "away" && offensiveTimeout) {
                game.awayTimeouts = game.awayTimeouts - 1
            }
        }

        // If game quarter is 0, then the game is over
        if (play.gameQuarter == 0) {
            game.final = true
        } else if (play.gameQuarter!! >= 5) {
            game.ot = true
        }

        // Update the play type
        if (play.actualResult?.contains("TOUCHDOWN") == true) {
            game.currentPlayType = "POINT AFTER"
        } else if (play.actualResult?.contains("SAFETY") == true || playCall == "field goal" && play.actualResult
                .equals("GOOD") || playCall == "pat" || playCall == "two point"
        ) {
            game.currentPlayType = "KICKOFF"
        } else {
            game.currentPlayType = "NORMAL"
        }

        // Update everything else
        game.homeScore = play.homeScore ?: throw IllegalArgumentException("Home score is null")
        game.awayScore = play.awayScore ?: throw IllegalArgumentException("Away score is null")
        game.possession = play.possession
        game.quarter = play.gameQuarter ?: throw IllegalArgumentException("Game quarter is null")
        game.clock = gameUtils.convertClockToString(play.clock ?: 420)
        game.ballLocation = play.ballLocation
        game.down = play.down ?: throw IllegalArgumentException("Down is null")
        game.yardsToGo = play.yardsToGo ?: throw IllegalArgumentException("Yards to go is null")
        game.numPlays = play.playNumber
        return game
    }
}
