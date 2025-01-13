package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.GameStats
import com.fcfb.arceus.handlers.game.StatsHandler
import com.fcfb.arceus.repositories.GameRepository
import com.fcfb.arceus.repositories.GameStatsRepository
import com.fcfb.arceus.repositories.PlayRepository
import com.fcfb.arceus.service.fcfb.TeamService
import org.springframework.stereotype.Component

@Component
class GameStatsService(
    private val gameStatsRepository: GameStatsRepository,
    private val teamService: TeamService,
    private val gameRepository: GameRepository,
    private val playRepository: PlayRepository,
    private val statsHandler: StatsHandler,
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
        gameStatsRepository.save(awayStats) ?: throw Exception("Could not create game stats")

        return listOf(homeStats, awayStats)
    }

    /**
     * Generate game stats for a game
     */
    fun generateGameStats(gameId: Int) {
        // Delete previous stats from game
        deleteByGameId(gameId)

        // Create new game stats for game
        val game = gameRepository.getGameById(gameId)
        createGameStats(game)

        // Get game and all plays
        val allPlays = playRepository.getAllPlaysByGameId(gameId)

        // Iterate through the plays and update the game stats
        for (play in allPlays) {
            statsHandler.updateGameStats(
                game,
                allPlays,
                play)
        }
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
