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
    fun createGameStats(game: Game): List<GameStats> {
        val homeTeam = teamService.getTeamByName(game.homeTeam)
        val awayTeam = teamService.getTeamByName(game.awayTeam)

        val homeStats =
            GameStats(
                gameId = game.gameId,
                team = game.homeTeam,
                teamRank = homeTeam.playoffCommitteeRanking ?: homeTeam.coachesPollRanking ?: 0,
                startTime = game.startTime,
                location = game.location,
                tvChannel = game.tvChannel,
                coaches = game.homeCoaches,
                offensivePlaybook = game.homeOffensivePlaybook,
                defensivePlaybook = game.homeDefensivePlaybook,
                season = game.season,
                week = game.week,
                subdivision = game.subdivision,
                gameStatus = game.gameStatus,
                gameType = game.gameType,
                record = homeTeam.currentWins.toString() + "-" + homeTeam.currentLosses.toString(),
            )
        gameStatsRepository.save(homeStats) ?: throw Exception("Could not create game stats")

        val awayStats =
            GameStats(
                gameId = game.gameId,
                team = game.awayTeam,
                teamRank = awayTeam.playoffCommitteeRanking ?: awayTeam.coachesPollRanking ?: 0,
                startTime = game.startTime,
                location = game.location,
                tvChannel = game.tvChannel,
                coaches = game.awayCoaches,
                offensivePlaybook = game.awayOffensivePlaybook,
                defensivePlaybook = game.awayDefensivePlaybook,
                season = game.season,
                week = game.week,
                subdivision = game.subdivision,
                gameStatus = game.gameStatus,
                gameType = game.gameType,
                record = awayTeam.currentWins.toString() + "-" + awayTeam.currentLosses.toString(),
            )

        return listOf(homeStats, awayStats)
    }

    /**
     * Get game stats entry by game ID
     * @param gameId
     */
    fun getGameStatsByIdAndTeam(
        gameId: Int,
        team: String,
    ) = gameStatsRepository.getGameStatsByIdAndTeam(gameId, team)

    /**
     * Delete game stats entry by game ID
     */
    fun deleteByGameId(gameId: Int) = gameStatsRepository.deleteByGameId(gameId)
}
