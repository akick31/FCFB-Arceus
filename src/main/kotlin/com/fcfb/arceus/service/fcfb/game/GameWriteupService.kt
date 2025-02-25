package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.repositories.GameWriteupRepository
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
    ): String {
        val writeups = gameWriteupRepository.findByScenario(scenario, passOrRun)

        return if (writeups.isNotEmpty()) {
            writeups.random().message ?: "No message found"
        } else {
            "No message found"
        }
    }
}
