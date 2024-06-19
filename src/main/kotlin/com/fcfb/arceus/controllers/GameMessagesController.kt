package com.fcfb.arceus.controllers

import com.fcfb.arceus.service.game.GameMessagesService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/game_messages")
class GameMessagesController(
    private var gameMessagesService: GameMessagesService
) {
    @GetMapping("/{scenario}")
    fun getGameMessageByScenario(
        @PathVariable scenario: String
    ) = gameMessagesService.getGameMessageByScenario(scenario)
}
