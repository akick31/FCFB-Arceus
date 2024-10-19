package com.fcfb.arceus.controllers

import com.fcfb.arceus.service.fcfb.game.GameStatsService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/game_stats")
class GameStatsController(
    private var gameStatsService: GameStatsService,
) {
    /**
     * Get a game's stats by its game id
     * @param gameId
     * @return
     */
    @GetMapping("")
    fun getGameStatsById(
        @RequestParam("gameId") gameId: String,
    ) = gameStatsService.getGameStatsById(gameId)
}
