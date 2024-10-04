package com.fcfb.arceus.service.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.GameStats
import com.fcfb.arceus.models.requests.StartRequest
import com.fcfb.arceus.repositories.GameStatsRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class GameStatsService(
    private val gameStatsRepository: GameStatsRepository
) {
    fun createGameStats(
        game: Game
    ): GameStats {
        val gameStats = GameStats(
            gameId = game.gameId,
            homeTeam = game.homeTeam,
            awayTeam = game.awayTeam,
            startTime = game.startTime,
            location = game.location,
            tvChannel = game.tvChannel,
            homeCoach = game.homeCoach,
            awayCoach = game.awayCoach,
            homeOffensivePlaybook = game.homeOffensivePlaybook,
            awayOffensivePlaybook = game.awayOffensivePlaybook,
            homeDefensivePlaybook = game.homeDefensivePlaybook,
            awayDefensivePlaybook = game.awayDefensivePlaybook,
            season = game.season,
            week = game.week,
            subdivision = game.subdivision,
            isScrimmage = game.scrimmage
        )
        gameStatsRepository.save(gameStats) ?: throw Exception("Could not create game stats")
        return gameStats
    }
}