package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.domain.Game.DefensivePlaybook
import com.fcfb.arceus.domain.Game.OffensivePlaybook
import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.PlayType
import com.fcfb.arceus.domain.Game.Scenario
import com.fcfb.arceus.domain.Ranges
import com.fcfb.arceus.models.ResultNotFoundException
import com.fcfb.arceus.repositories.RangesRepository
import org.springframework.stereotype.Component

@Component
class RangesService(
    private val rangesRepository: RangesRepository,
) {
    /**
     * Get the result of a normal play
     * @param playCall
     * @param offensivePlaybook
     * @param defensivePlaybook
     * @param difference
     */
    fun getNormalResult(
        playCall: PlayCall,
        offensivePlaybook: OffensivePlaybook,
        defensivePlaybook: DefensivePlaybook,
        difference: Int,
    ): Ranges {
        return if (playCall == PlayCall.SPIKE) {
            Ranges(
                PlayType.NORMAL.description,
                offensivePlaybook.description,
                defensivePlaybook.description,
                0,
                0,
                0,
                Scenario.SPIKE,
                0,
                0,
                0,
            )
        } else if (playCall == PlayCall.KNEEL) {
            Ranges(
                PlayType.NORMAL.description,
                offensivePlaybook.description,
                defensivePlaybook.description,
                0,
                0,
                0,
                Scenario.KNEEL,
                0,
                0,
                0,
            )
        } else {
            rangesRepository.getNormalResult(
                playCall.description,
                offensivePlaybook.description,
                defensivePlaybook.description,
                difference.toString(),
            ) ?: throw ResultNotFoundException()
        }
    }

    /**
     * Get the result of a normal play that does not require playbooks
     * @param playCall
     * @param difference
     */
    fun getNonNormalResult(
        playCall: PlayCall,
        difference: Int,
    ): Ranges {
        return rangesRepository.getNonNormalResult(
            playCall.description,
            difference.toString(),
        ) ?: throw ResultNotFoundException()
    }

    /**
     * Get the result of a field goal play
     * @param playCall
     * @param distance
     * @param difference
     */
    fun getFieldGoalResult(
        playCall: PlayCall,
        distance: Int,
        difference: Int,
    ): Ranges {
        return rangesRepository.getFieldGoalResult(
            playCall.description,
            distance.toString(),
            difference.toString(),
        ) ?: throw ResultNotFoundException()
    }

    /**
     * Get the result of a punt play
     * @param playCall
     * @param ballLocation
     * @param difference
     */
    fun getPuntResult(
        playCall: PlayCall,
        ballLocation: Int,
        difference: Int,
    ): Ranges {
        return rangesRepository.getPuntResult(
            playCall.description,
            ballLocation.toString(),
            difference.toString(),
        ) ?: throw ResultNotFoundException()
    }
}
