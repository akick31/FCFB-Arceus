package com.fcfb.arceus.service.game

import com.fcfb.arceus.repositories.GameMessagesRepository
import org.springframework.stereotype.Component

@Component
class GameMessagesService(
    private var gameMessagesRepository: GameMessagesRepository?
) {
    fun getGameMessageByScenario(
        scenario: String
    ) = gameMessagesRepository?.findByScenario(scenario)?.message
}
