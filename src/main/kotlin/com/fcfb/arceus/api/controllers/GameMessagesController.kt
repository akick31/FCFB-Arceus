package com.fcfb.arceus.api.controllers

import com.fcfb.arceus.api.repositories.GameMessagesRepository
import com.fcfb.arceus.api.repositories.GameStatsRepository
import com.fcfb.arceus.domain.OngoingGamesEntity
import com.fcfb.arceus.service.discord.DiscordService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/game_messages")
class GameMessagesController {
    @Autowired
    var gameMessagesRepository: GameMessagesRepository? = null

    @GetMapping("/{scenario}")
    fun getGameMessageByScenario(
        @PathVariable scenario: String
    ) = gameMessagesRepository?.findByScenario(scenario)?.message
}