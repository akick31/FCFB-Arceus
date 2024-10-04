package com.fcfb.arceus.handlers.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.Scenario
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.domain.GameStats
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.repositories.GameStatsRepository
import org.springframework.stereotype.Component

@Component
class StatsHandler(
    private val gameStatsRepository: GameStatsRepository
) {
    fun updateGameStats(
        game: Game,
        play: Play,
        playCall: PlayCall
    ): GameStats? {
        val stats = gameStatsRepository.findByGameId(game.gameId ?: return null) ?: return null

        if (game.possession == TeamSide.HOME) {
            stats.homeScore = game.homeScore
            stats.homePassAttempts = calculatePassAttempts(play, stats.homePassAttempts ?: 0)
            stats.homePassCompletions = calculatePassCompletions(play, stats.homePassCompletions ?: 0)
            stats.homePassCompletionPercentage = calculatePercentage(stats.homePassCompletions ?: 0, stats.homePassAttempts ?: 0)
            stats.homePassYards = calculatePassYards(play, stats.homePassYards ?: 0)
            stats.homeRushAttempts = calculateRushAttempts(play, stats.homeRushAttempts ?: 0)
            stats.homeRush3YardsOrMore = calculateRush3YardsOrMore(play, stats.homeRush3YardsOrMore ?: 0)
            stats.homeRushSuccessPercentage = calculatePercentage(stats.homeRush3YardsOrMore ?: 0, stats.homeRushAttempts ?: 0)
            stats.homeRushYards = calculateRushYards(play, stats.homeRushYards ?: 0)
            stats.homeTotalYards = calculateTotalYards(stats.homePassYards ?: 0, stats.homeRushYards ?: 0)

        } else {
            stats.awayScore = game.awayScore
            stats.awayPassAttempts = calculatePassAttempts(play, stats.awayPassAttempts ?: 0)
            stats.awayPassCompletions = calculatePassCompletions(play, stats.awayPassCompletions ?: 0)
            stats.awayPassCompletionPercentage = calculatePercentage(stats.awayPassCompletions ?: 0, stats.awayPassAttempts ?: 0)
            stats.awayPassYards = calculatePassYards(play, stats.awayPassYards ?: 0)
            stats.awayRushAttempts = calculateRushAttempts(play, stats.awayRushAttempts ?: 0)
            stats.awayRush3YardsOrMore = calculateRush3YardsOrMore(play, stats.awayRush3YardsOrMore ?: 0)
            stats.awayRushSuccessPercentage = calculatePercentage(stats.awayRush3YardsOrMore ?: 0, stats.awayRushAttempts ?: 0)
            stats.awayRushYards = calculateRushYards(play, stats.awayRushYards ?: 0)
            stats.awayTotalYards = calculateTotalYards(stats.awayPassYards ?: 0, stats.awayRushYards ?: 0)
        }

        gameStatsRepository.save(stats)
        return stats
    }

    fun calculatePassAttempts(
        play: Play,
        currentPassAttempts: Int
    ) : Int {
        // Don't count sacks as pass attempts
        if (play.playCall == PlayCall.PASS && (play.result != Scenario.LOSS_OF_10_YARDS ||
            play.result != Scenario.LOSS_OF_5_YARDS ||
            play.result != Scenario.LOSS_OF_3_YARDS ||
            play.result != Scenario.LOSS_OF_1_YARD)) {
            return currentPassAttempts
        }
        if (play.playCall == PlayCall.PASS || play.playCall == PlayCall.SPIKE) {
            return currentPassAttempts + 1
        }
        return currentPassAttempts
    }

    fun calculatePassCompletions(
        play: Play,
        currentPassCompletions: Int
    ): Int {
        if (play.playCall == PlayCall.PASS &&
            (play.result != Scenario.INCOMPLETE ||
            play.result != Scenario.LOSS_OF_10_YARDS ||
            play.result != Scenario.LOSS_OF_5_YARDS ||
            play.result != Scenario.LOSS_OF_3_YARDS ||
            play.result != Scenario.LOSS_OF_1_YARD ||
            play.result != Scenario.TURNOVER_PLUS_20_YARDS ||
            play.result != Scenario.TURNOVER_PLUS_15_YARDS ||
            play.result != Scenario.TURNOVER_PLUS_10_YARDS ||
            play.result != Scenario.TURNOVER_PLUS_5_YARDS ||
            play.result != Scenario.TURNOVER ||
            play.result != Scenario.TURNOVER_MINUS_5_YARDS ||
            play.result != Scenario.TURNOVER_MINUS_10_YARDS ||
            play.result != Scenario.TURNOVER_MINUS_15_YARDS ||
            play.result != Scenario.TURNOVER_MINUS_20_YARDS ||
            play.result != Scenario.TURNOVER_TOUCHDOWN ||
            play.result != Scenario.SAFETY)
        ) {
            return currentPassCompletions + 1
        }
        return currentPassCompletions
    }

    fun calculatePassYards(
        play: Play,
        currentPassYards: Int
    ): Int {
        // Don't count sacks towards passing yards
        if (play.playCall == PlayCall.PASS && (play.result != Scenario.LOSS_OF_10_YARDS ||
                play.result != Scenario.LOSS_OF_5_YARDS ||
                play.result != Scenario.LOSS_OF_3_YARDS ||
                play.result != Scenario.LOSS_OF_1_YARD)) {
            return currentPassYards
        }
        if (play.playCall == PlayCall.PASS) {
            return currentPassYards + (play.yards ?: 0)
        }
        return currentPassYards
    }

    fun calculateRushAttempts(
        play: Play,
        currentRushAttempts: Int
    ): Int {
        if (play.playCall == PlayCall.RUN) {
            return currentRushAttempts + 1
        }
        return currentRushAttempts
    }

    fun calculateRush3YardsOrMore(
        play: Play,
        currentRush3YardsOrMore: Int
    ): Int {
        if (play.playCall == PlayCall.RUN && (play.yards ?: 0) >= 3) {
            return currentRush3YardsOrMore + 1
        }
        return currentRush3YardsOrMore
    }

    fun calculateRushYards(
        play: Play,
        currentRushYards: Int
    ): Int {
        // Count sacks towards rushing yards
        if (play.playCall == PlayCall.PASS && (play.result != Scenario.LOSS_OF_10_YARDS ||
                    play.result != Scenario.LOSS_OF_5_YARDS ||
                    play.result != Scenario.LOSS_OF_3_YARDS ||
                    play.result != Scenario.LOSS_OF_1_YARD)) {
            return currentRushYards + (play.yards ?: 0)
        }
        if (play.playCall == PlayCall.RUN && (
                play.result != Scenario.TURNOVER_PLUS_20_YARDS ||
                play.result != Scenario.TURNOVER_PLUS_15_YARDS ||
                play.result != Scenario.TURNOVER_PLUS_10_YARDS ||
                play.result != Scenario.TURNOVER_PLUS_5_YARDS ||
                play.result != Scenario.TURNOVER ||
                play.result != Scenario.TURNOVER_MINUS_5_YARDS ||
                play.result != Scenario.TURNOVER_MINUS_10_YARDS ||
                play.result != Scenario.TURNOVER_MINUS_15_YARDS ||
                play.result != Scenario.TURNOVER_MINUS_20_YARDS ||
                play.result != Scenario.TURNOVER_TOUCHDOWN ||
                play.result != Scenario.SAFETY)
        ) {
            return currentRushYards + (play.yards ?: 0)
        }
        return currentRushYards
    }

    fun calculateTotalYards(
        passingYards: Int,
        rushingYards: Int
    ): Int {
        return passingYards + rushingYards
    }

    fun calculatePercentage(
        successes: Int,
        attempts: Int
    ): Double {
        if (attempts == 0) {
            return 0.0
        }
        return (successes.toDouble() / attempts.toDouble()) * 100
    }


}