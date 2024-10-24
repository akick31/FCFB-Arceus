package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.repositories.GameWriteupRepository
import com.fcfb.arceus.utils.Logger
import org.springframework.stereotype.Component

@Component
class GameWriteupService(
    private var gameWriteupRepository: GameWriteupRepository?,
) {
    fun getGameMessageByScenario(
        scenario: String,
        passOrRun: String?,
    ) = gameWriteupRepository?.findByScenario(scenario, passOrRun)?.message ?: run {
        Logger.error("No message found for scenario: $scenario and passOrRun: $passOrRun")
        "No message found"
    }
}
