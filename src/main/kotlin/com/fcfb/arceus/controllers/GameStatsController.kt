package com.fcfb.arceus.controllers

import com.fcfb.arceus.service.fcfb.GameStatsService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
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
    fun getGameStatsByIdAndTeam(
        @RequestParam("gameId") gameId: Int,
        @RequestParam("team") team: String,
    ) = gameStatsService.getGameStatsByIdAndTeam(gameId, team)

    /**
     * Generate game stats for a game
     * @return
     */
    @PostMapping("/generate")
    fun generateGameStats(
        @RequestParam("gameId") gameId: Int,
    ) = gameStatsService.generateGameStats(gameId)

    /**
     * Generate game stats for all games more recent than a game id
     */
    @PostMapping("/generate/all/more_recent_than")
    fun generateAllGameStatsMoreRecentThanGameId(
        @RequestParam("gameId") gameId: Int,
    ) = gameStatsService.generateGameStatsForGamesMoreRecentThanGameId(gameId)

    /**
     * Generate game stats for all games
     * @return
     */
    @PostMapping("/generate/all")
    fun generateAllGameStats() = gameStatsService.generateAllGameStats()
}
