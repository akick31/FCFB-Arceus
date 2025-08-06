package com.fcfb.arceus.service.fcfb

import com.fcfb.arceus.domain.enums.DefensivePlaybook
import com.fcfb.arceus.domain.enums.OffensivePlaybook
import com.fcfb.arceus.domain.enums.PlayCall
import com.fcfb.arceus.domain.enums.PlayType
import com.fcfb.arceus.domain.enums.Scenario
import com.fcfb.arceus.domain.Ranges
import com.fcfb.arceus.repositories.RangesRepository
import com.fcfb.arceus.utils.ResultNotFoundException
import org.springframework.stereotype.Service

@Service
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
        return when (playCall) {
            PlayCall.SPIKE -> {
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
            }
            PlayCall.KNEEL -> {
                Ranges(
                    PlayType.NORMAL.description,
                    offensivePlaybook.description,
                    defensivePlaybook.description,
                    0,
                    0,
                    0,
                    Scenario.KNEEL,
                    1,
                    0,
                    0,
                )
            }
            else -> {
                rangesRepository.getNormalResult(
                    playCall.description,
                    offensivePlaybook.description,
                    defensivePlaybook.description,
                    difference.toString(),
                ) ?: throw ResultNotFoundException()
            }
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
    ) = rangesRepository.getNonNormalResult(
        playCall.description,
        difference.toString(),
    ) ?: throw ResultNotFoundException()

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
    ) = rangesRepository.getFieldGoalResult(
        playCall.description,
        distance.toString(),
        difference.toString(),
    ) ?: throw ResultNotFoundException()

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
    ) = rangesRepository.getPuntResult(
        playCall.description,
        ballLocation.toString(),
        difference.toString(),
    ) ?: throw ResultNotFoundException()

    /**
     * Get the play time for a given number of yards
     * @param yards
     */
    fun getPlayTime(
        playCall: PlayCall,
        yards: Int,
    ) = rangesRepository.getPlayTime(playCall.description, yards) ?: throw ResultNotFoundException()
}
