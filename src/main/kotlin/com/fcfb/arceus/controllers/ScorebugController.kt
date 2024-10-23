package com.fcfb.arceus.controllers

import com.fcfb.arceus.service.fcfb.game.ScorebugService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/scorebug")
class ScorebugController(
    private var scorebugService: ScorebugService,
) {
    @GetMapping("")
    fun getScorebugByGameId(
        @RequestParam("gameId") gameId: Int,
    ) = scorebugService.getScorebugByGameId(gameId)

    @PostMapping("/generate")
    fun generateScorebug(
        @RequestParam("gameId") gameId: Int,
    ) = scorebugService.generateScorebug(gameId)
}
