package com.fcfb.arceus.controllers

import com.fcfb.arceus.service.fcfb.game.GameWriteupService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/game_writeup")
class GameWriteupController(
    private var gameMessagesService: GameWriteupService,
) {
    @GetMapping("/{scenario}/{playCall}")
    fun getGameMessageByScenario(
        @PathVariable scenario: String,
        @PathVariable playCall: String,
    ) = gameMessagesService.getGameMessageByScenario(scenario, playCall)
}
