package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.GameStats
import com.fcfb.arceus.repositories.GameStatsRepository
import com.fcfb.arceus.service.fcfb.TeamService
import org.springframework.stereotype.Component

@Component
class GameStatsService(
    private val gameStatsRepository: GameStatsRepository,
    private val teamService: TeamService,
) {
    /**
     * Create a game stats entry
     * @param game
     */
    fun createGameStats(game: Game): GameStats {
        val homeTeam = teamService.getTeamByName(game.homeTeam)
        val awayTeam = teamService.getTeamByName(game.awayTeam)

        val gameStats =
            GameStats(
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
                awayRecord = awayTeam.currentWins.toString() + "-" + awayTeam.currentLosses.toString(),
            )
        gameStatsRepository.save(gameStats) ?: throw Exception("Could not create game stats")
        return gameStats
    }

    /**
     * Get game stats entry by game ID
     * @param gameId
     */
    fun getGameStatsById(gameId: Int) = gameStatsRepository.findByGameId(gameId)

    /**
     * Delete game stats entry by game ID
     */
    fun deleteById(gameId: Int) = gameStatsRepository.deleteById(gameId)
}
