package com.fcfb.arceus.service.game

import com.fcfb.arceus.domain.OngoingGamesEntity
import com.fcfb.arceus.models.game.Game.CoinTossWinner
import com.fcfb.arceus.models.game.Game.Possession
import com.fcfb.arceus.models.game.Game.CoinTossChoice
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
    ): Possession? {
        var possession: Possession? = null
        if (game.coinTossWinner == CoinTossWinner.HOME && game.coinTossChoice == CoinTossChoice.DEFER) {
            possession = Possession.AWAY
        } else if (game.coinTossWinner == CoinTossWinner.HOME && game.coinTossChoice == CoinTossChoice.RECEIVE) {
            possession = Possession.HOME
        } else if (game.coinTossWinner == CoinTossWinner.AWAY && game.coinTossChoice == CoinTossChoice.DEFER) {
            possession = Possession.HOME
        } else if (game.coinTossWinner == CoinTossWinner.AWAY && game.coinTossChoice == CoinTossChoice.RECEIVE) {
            possession = Possession.AWAY
        }
        return possession
    } //TODO Win probability and ELO rating methods
}
