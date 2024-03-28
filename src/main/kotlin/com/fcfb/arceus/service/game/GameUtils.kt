package com.fcfb.arceus.service.game

import com.fcfb.arceus.domain.OngoingGamesEntity
import org.springframework.stereotype.Component

@Component
class GameUtils {
    /**
     * Returns the difference between the offensive and defensive numbers.
     * @param offensiveNumber
     * @param defesiveNumber
     * @return
     */
    fun getDifference(
        offensiveNumber: Int, 
        defesiveNumber: Int
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
    fun convertClockToSeconds(
        clock: String
    ): Int {
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
    fun convertClockToString(
        seconds: Int
    ): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "$minutes:$remainingSeconds"
    }

    fun handleHalfTimePossessionChange(
        game: OngoingGamesEntity
    ): String {
        var possession = ""
        if (game.coinTossWinner == game.homeTeam && game.coinTossChoice == "defer") {
            possession = "away"
        } else if (game.coinTossWinner == game.homeTeam && game.coinTossChoice == "receive") {
            possession = "home"
        } else if (game.coinTossWinner == game.awayTeam && game.coinTossChoice == "defer") {
            possession = "home"
        } else if (game.coinTossWinner == game.awayTeam && game.coinTossChoice == "receive") {
            possession = "away"
        }
        return possession
    } //TODO Win probability and ELO rating methods
}
