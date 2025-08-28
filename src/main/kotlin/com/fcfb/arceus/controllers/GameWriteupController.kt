package com.fcfb.arceus.controllers

import com.fcfb.arceus.service.fcfb.GameWriteupService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("${ApiConstants.FULL_PATH}/game_writeup")
class GameWriteupController(
    private var gameMessagesService: GameWriteupService,
) {
    @GetMapping("")
    fun getGameMessageByScenario(
        @RequestParam scenario: String,
        @RequestParam playCall: String,
    ) = gameMessagesService.getGameMessageByScenario(scenario, playCall)
}
