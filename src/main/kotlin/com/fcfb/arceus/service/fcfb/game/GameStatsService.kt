package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.GameStats
import com.fcfb.arceus.models.requests.StartRequest
import com.fcfb.arceus.repositories.GameStatsRepository
import com.fcfb.arceus.repositories.TeamRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class GameStatsService(
    private val gameStatsRepository: GameStatsRepository,
    private val teamRepository: TeamRepository
) {
    fun createGameStats(
        game: Game
    ): GameStats {

        val homeTeam = teamRepository.findByName(game.homeTeam ?: throw Exception("Could not create game stats, could not get home team")) ?: throw Exception("Could not create game stats, could not get home team")
        val awayTeam = teamRepository.findByName(game.awayTeam ?: throw Exception("Could not create game stats, could not get away team")) ?: throw Exception("Could not create game stats, could not get away team")

        val gameStats = GameStats(
            gameId = game.gameId,
            homeTeam = game.homeTeam,
            awayTeam = game.awayTeam,
            homeTeamRank = homeTeam.playoffCommitteeRanking ?: homeTeam.coachesPollRanking ?: 0,
            awayTeamRank = awayTeam.playoffCommitteeRanking ?: awayTeam.coachesPollRanking ?: 0,
            startTime = game.startTime,
            location = game.location,
            tvChannel = game.tvChannel,
            homeCoach1 = game.homeCoach1,
            homeCoach2 = game.homeCoach2,
            awayCoach1 = game.awayCoach1,
            awayCoach2 = game.awayCoach2,
            homeOffensivePlaybook = game.homeOffensivePlaybook,
            awayOffensivePlaybook = game.awayOffensivePlaybook,
            homeDefensivePlaybook = game.homeDefensivePlaybook,
            awayDefensivePlaybook = game.awayDefensivePlaybook,
            season = game.season,
            week = game.week,
            subdivision = game.subdivision,
            gameStatus = game.gameStatus,
            gameType = game.gameType,
            homeRecord = homeTeam.currentWins.toString() + "-" + homeTeam.currentLosses.toString(),
            awayRecord = awayTeam.currentWins.toString() + "-" + awayTeam.currentLosses.toString()
        )
        gameStatsRepository.save(gameStats) ?: throw Exception("Could not create game stats")
        return gameStats
    }
}