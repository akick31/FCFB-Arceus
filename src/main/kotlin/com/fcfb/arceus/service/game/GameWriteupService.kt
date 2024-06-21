package com.fcfb.arceus.service.game

import com.fcfb.arceus.repositories.GameWriteupRepository
import org.springframework.stereotype.Component

@Component
class GameWriteupService(
    private var gameWriteupRepository: GameWriteupRepository?
) {
    fun getGameMessageByScenario(
        scenario: String
    ) = gameWriteupRepository?.findByScenario(scenario)?.message
}
