package com.fcfb.arceus.controllers

import com.fcfb.arceus.repositories.GameMessagesRepository
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