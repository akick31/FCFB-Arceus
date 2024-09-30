package com.fcfb.arceus.controllers

import com.fcfb.arceus.service.game.ScorebugService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/scorebug")
class ScorebugController(
    private var scorebugService: ScorebugService
) {
    @GetMapping("")
    fun updateGame(
        @RequestParam("gameId") gameId: Int
    ) = scorebugService.getScorebugByGameId(gameId)
}