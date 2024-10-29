package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.repositories.GameWriteupRepository
import com.fcfb.arceus.utils.Logger
import org.springframework.stereotype.Component

@Component
class GameWriteupService(
    private val gameWriteupRepository: GameWriteupRepository,
) {
    /**
     * Get a game message by scenario
     * @param scenario
     * @param passOrRun
     */
    fun getGameMessageByScenario(
        scenario: String,
        passOrRun: String?,
    ) = gameWriteupRepository.findByScenario(scenario, passOrRun)?.message ?: "No message found"
}
