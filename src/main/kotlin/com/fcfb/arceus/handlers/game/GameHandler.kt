package com.fcfb.arceus.handlers.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.CoinTossChoice
import com.fcfb.arceus.domain.Game.TeamSide
import org.springframework.stereotype.Component

@Component
class GameHandler() {
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
        game: Game
    ): TeamSide? {
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
