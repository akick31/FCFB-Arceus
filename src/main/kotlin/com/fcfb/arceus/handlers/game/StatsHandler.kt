package com.fcfb.arceus.handlers.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.ActualResult
import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.Scenario
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.domain.GameStats
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.repositories.GameStatsRepository
import org.springframework.stereotype.Component

@Component
class StatsHandler(
    private val gameStatsRepository: GameStatsRepository,
) {
    /**
     * Calculate the stats for each team from the current play
     */
    private fun updateStatsForEachTeam(
        allPlays: List<Play>,
        play: Play,
        possession: TeamSide,
        game: Game,
        stats: GameStats,
        opponentStats: GameStats,
    ): List<GameStats> {
        val defendingTeam = if (possession == TeamSide.HOME) TeamSide.AWAY else TeamSide.HOME

        stats.score = game.homeScore
        stats.passAttempts =
            calculatePassAttempts(
                play, stats.passAttempts,
            )
        stats.passCompletions =
            calculatePassCompletions(
                play, stats.passCompletions,
            )
        stats.passCompletionPercentage =
            calculatePercentage(
                stats.passCompletions, stats.passAttempts,
            )
        stats.passYards =
            calculatePassYards(
                play, stats.passYards,
            )
        stats.rushAttempts =
            calculateRushAttempts(
                play, stats.rushAttempts,
            )
        stats.rushSuccesses =
            calculateRushSuccesses(
                play, stats.rushSuccesses,
            )
        stats.rushSuccessPercentage =
            calculatePercentage(
                stats.rushSuccesses, stats.rushAttempts,
            )
        stats.rushYards =
            calculateRushYards(
                play, stats.rushYards,
            )
        stats.totalYards =
            calculateTotalYards(
                stats.passYards, stats.rushYards,
            )
        stats.interceptionsLost =
            calculateInterceptionsLost(
                play, stats.interceptionsLost,
            )
        opponentStats.interceptionsForced = stats.interceptionsLost
        stats.fumblesLost =
            calculateFumblesLost(
                play, stats.fumblesLost,
            )
        opponentStats.fumblesForced = stats.fumblesLost
        stats.turnoversLost =
            calculateTurnoversLost(
                stats.interceptionsLost, stats.fumblesLost,
            )
        opponentStats.turnoversForced = stats.turnoversLost
        stats.turnoverTouchdownsLost =
            calculateTurnoverTouchdownsLost(
                play, stats.turnoverTouchdownsLost,
            )
        opponentStats.turnoverTouchdownsForced = stats.turnoverTouchdownsLost
        stats.fieldGoalMade =
            calculateFieldGoalMade(
                play, stats.fieldGoalMade,
            )
        stats.fieldGoalAttempts =
            calculateFieldGoalAttempts(
                play, stats.fieldGoalAttempts,
            )
        stats.fieldGoalPercentage =
            calculatePercentage(
                stats.fieldGoalMade, stats.fieldGoalAttempts,
            )
        stats.longestFieldGoal =
            calculateLongestFieldGoal(
                play, stats.longestFieldGoal,
            )
        opponentStats.blockedOpponentFieldGoals =
            calculateBlockedOpponentFieldGoals(
                play, opponentStats.blockedOpponentFieldGoals,
            )
        stats.fieldGoalTouchdown =
            calculateFieldGoalTouchdown(
                play, stats.fieldGoalTouchdown,
            )
        stats.puntsAttempted =
            calculatePuntsAttempted(
                play, stats.puntsAttempted,
            )
        stats.longestPunt =
            calculateLongestPunt(
                play, stats.longestPunt,
            )
        stats.averagePuntLength =
            calculateAveragePuntLength(
                allPlays, possession,
            )
        opponentStats.blockedOpponentPunt =
            calculateBlockedOpponentPunt(
                play, opponentStats.blockedOpponentPunt,
            )
        opponentStats.puntReturnTd =
            calculatePuntReturnTd(
                play, opponentStats.puntReturnTd,
            )
        opponentStats.puntReturnTdPercentage =
            calculatePercentage(
                opponentStats.puntReturnTd, opponentStats.puntsAttempted,
            )
        stats.numberOfKickoffs =
            calculateNumberOfKickoffs(
                play, stats.numberOfKickoffs,
            )
        stats.onsideAttempts =
            calculateOnsideAttempts(
                play, stats.onsideAttempts,
            )
        stats.onsideSuccess =
            calculateOnsideSuccess(
                play, stats.onsideSuccess,
            )
        stats.onsideSuccessPercentage =
            calculatePercentage(
                stats.onsideSuccess, stats.onsideAttempts,
            )
        stats.normalKickoffAttempts =
            calculateNormalKickoffAttempts(
                play, stats.normalKickoffAttempts,
            )
        stats.touchbacks =
            calculateTouchbacks(
                play, stats.touchbacks,
            )
        stats.touchbackPercentage =
            calculatePercentage(
                stats.touchbacks, stats.normalKickoffAttempts,
            )
        opponentStats.kickReturnTd =
            calculateKickReturnTd(
                play, opponentStats.kickReturnTd,
            )
        opponentStats.kickReturnTdPercentage =
            calculatePercentage(
                opponentStats.kickReturnTd, opponentStats.numberOfKickoffs,
            )
        stats.numberOfDrives =
            calculateNumberOfDrives(
                allPlays, possession,
            )
        stats.timeOfPossession =
            calculateTimeOfPossession(
                allPlays, possession,
            )
        stats.touchdowns =
            calculateTouchdowns(
                allPlays, possession,
            )
        stats.averageOffensiveDiff =
            calculateAverageOffensiveDiff(
                allPlays, possession,
            )
        stats.averageDefensiveDiff =
            calculateAverageDefensiveDiff(
                allPlays, possession,
            )
        stats.averageOffensiveSpecialTeamsDiff =
            calculateAverageOffensiveSpecialTeamsDiff(
                allPlays, possession,
            )
        stats.averageDefensiveSpecialTeamsDiff =
            calculateAverageDefensiveSpecialTeamsDiff(
                allPlays, possession,
            )
        stats.averageYardsPerPlay =
            calculateAverageYardsPerPlay(
                allPlays, possession,
            )
        stats.thirdDownConversionSuccess =
            calculateThirdDownConversionSuccess(
                play, stats.thirdDownConversionSuccess,
            )
        stats.thirdDownConversionAttempts =
            calculateThirdDownConversionAttempts(
                play, stats.thirdDownConversionAttempts,
            )
        stats.thirdDownConversionPercentage =
            calculatePercentage(
                stats.thirdDownConversionSuccess, stats.thirdDownConversionAttempts,
            )
        stats.fourthDownConversionSuccess =
            calculateFourthDownConversionSuccess(
                play, stats.fourthDownConversionSuccess,
            )
        stats.fourthDownConversionAttempts =
            calculateFourthDownConversionAttempts(
                play, stats.fourthDownConversionAttempts,
            )
        stats.fourthDownConversionPercentage =
            calculatePercentage(
                stats.fourthDownConversionSuccess, stats.fourthDownConversionAttempts,
            )
        stats.largestLead =
            calculateLargestLead(
                game, stats.largestLead, possession,
            )
        stats.largestDeficit =
            calculateLargestDeficit(
                game, stats.largestDeficit, defendingTeam,
            )
        stats.passTouchdowns =
            calculatePassTouchdowns(
                play, stats.passTouchdowns,
            )
        stats.rushTouchdowns =
            calculateRushTouchdowns(
                play, stats.rushTouchdowns,
            )
        stats.redZoneAttempts =
            calculateRedZoneAttempts(
                allPlays, possession,
            )
        stats.redZoneSuccesses =
            calculateRedZoneSuccesses(
                allPlays, possession,
            )
        stats.redZoneSuccessPercentage =
            calculatePercentage(
                stats.redZoneSuccesses, stats.redZoneAttempts,
            )
        stats.turnoverDifferential =
            calculateTurnoverDifferential(
                stats.turnoversLost, stats.turnoversForced,
            )
        stats.pickSixesThrown =
            calculatePickSixes(
                play, stats.pickSixesThrown,
            )
        opponentStats.pickSixesForced = stats.pickSixesThrown
        stats.fumbleReturnTdsCommitted =
            calculateFumbleReturnTds(
                play, stats.fumbleReturnTdsCommitted,
            )
        opponentStats.fumbleReturnTdsForced = stats.fumbleReturnTdsCommitted
        stats.safetiesCommitted =
            calculateSafeties(
                play, stats.safetiesCommitted,
            )
        opponentStats.safetiesForced = stats.safetiesCommitted

        if (play.quarter == 1) {
            stats.q1Score = calculateQuarterScore(play, stats.q1Score, possession)
            opponentStats.q1Score = calculateQuarterScore(play, stats.q1Score, defendingTeam)
        }
        if (play.quarter == 2) {
            stats.q2Score = calculateQuarterScore(play, stats.q2Score, possession)
            opponentStats.q2Score = calculateQuarterScore(play, stats.q2Score, defendingTeam)
        }
        if (play.quarter == 3) {
            stats.q3Score = calculateQuarterScore(play, stats.q3Score, possession)
            opponentStats.q3Score = calculateQuarterScore(play, stats.q3Score, defendingTeam)
        }
        if (play.quarter == 4) {
            stats.q4Score = calculateQuarterScore(play, stats.q4Score, possession)
            opponentStats.q4Score = calculateQuarterScore(play, stats.q4Score, defendingTeam)
        }
        if (play.quarter == 5) {
            stats.otScore = calculateQuarterScore(play, stats.otScore, possession)
            opponentStats.otScore = calculateQuarterScore(play, stats.otScore, defendingTeam)
        }
        stats.gameStatus = game.gameStatus
        opponentStats.gameStatus = game.gameStatus
        stats.averageDiff = calculateAverageDiff(allPlays)
        opponentStats.averageDiff = calculateAverageDiff(allPlays)
        gameStatsRepository.save(stats)
        gameStatsRepository.save(opponentStats)
        return listOf(stats, opponentStats)
    }

    /**
     * Update the game stats for the current game
     */
    fun updateGameStats(
        game: Game,
        allPlays: List<Play>,
        play: Play,
    ): List<GameStats> {
        if (game.possession == TeamSide.HOME) {
            val stats = gameStatsRepository.getGameStatsByIdAndTeam(game.gameId, game.homeTeam)
            val opponentStats = gameStatsRepository.getGameStatsByIdAndTeam(game.gameId, game.awayTeam)
            return updateStatsForEachTeam(allPlays, play, TeamSide.HOME, game, stats, opponentStats)
        } else {
            val stats = gameStatsRepository.getGameStatsByIdAndTeam(game.gameId, game.awayTeam)
            val opponentStats = gameStatsRepository.getGameStatsByIdAndTeam(game.gameId, game.homeTeam)
            return updateStatsForEachTeam(allPlays, play, TeamSide.AWAY, game, stats, opponentStats)
        }
    }

    private fun calculatePassAttempts(
        play: Play,
        currentPassAttempts: Int,
    ): Int {
        // Don't count sacks as pass attempts
        if (play.playCall == PlayCall.PASS && (
                play.result == Scenario.LOSS_OF_10_YARDS ||
                    play.result == Scenario.LOSS_OF_7_YARDS ||
                    play.result == Scenario.LOSS_OF_5_YARDS ||
                    play.result == Scenario.LOSS_OF_3_YARDS ||
                    play.result == Scenario.LOSS_OF_2_YARDS ||
                    play.result == Scenario.LOSS_OF_1_YARD
            )
        ) {
            return currentPassAttempts
        }
        if (play.playCall == PlayCall.SPIKE) {
            return currentPassAttempts + 1
        }
        if (play.playCall == PlayCall.PASS) {
            return currentPassAttempts + 1
        }
        return currentPassAttempts
    }

    private fun calculatePassCompletions(
        play: Play,
        currentPassCompletions: Int,
    ): Int {
        if (play.playCall == PlayCall.PASS && (
                play.result != Scenario.INCOMPLETE &&
                    play.result != Scenario.LOSS_OF_10_YARDS &&
                    play.result != Scenario.LOSS_OF_7_YARDS &&
                    play.result != Scenario.LOSS_OF_5_YARDS &&
                    play.result != Scenario.LOSS_OF_3_YARDS &&
                    play.result != Scenario.LOSS_OF_2_YARDS &&
                    play.result != Scenario.LOSS_OF_1_YARD &&
                    play.result != Scenario.TURNOVER_PLUS_20_YARDS &&
                    play.result != Scenario.TURNOVER_PLUS_15_YARDS &&
                    play.result != Scenario.TURNOVER_PLUS_10_YARDS &&
                    play.result != Scenario.TURNOVER_PLUS_5_YARDS &&
                    play.result != Scenario.TURNOVER &&
                    play.result != Scenario.TURNOVER_MINUS_5_YARDS &&
                    play.result != Scenario.TURNOVER_MINUS_10_YARDS &&
                    play.result != Scenario.TURNOVER_MINUS_15_YARDS &&
                    play.result != Scenario.TURNOVER_MINUS_20_YARDS &&
                    play.result != Scenario.TURNOVER_TOUCHDOWN &&
                    play.result != Scenario.SAFETY
            )
        ) {
            return currentPassCompletions + 1
        }
        return currentPassCompletions
    }

    private fun calculatePassYards(
        play: Play,
        currentPassYards: Int,
    ): Int {
        // Don't count sacks towards passing yards
        if (play.playCall == PlayCall.PASS && (
                play.result == Scenario.LOSS_OF_10_YARDS ||
                    play.result == Scenario.LOSS_OF_7_YARDS ||
                    play.result == Scenario.LOSS_OF_5_YARDS ||
                    play.result == Scenario.LOSS_OF_3_YARDS ||
                    play.result == Scenario.LOSS_OF_2_YARDS ||
                    play.result == Scenario.LOSS_OF_1_YARD
            )
        ) {
            return currentPassYards
        }
        if (play.playCall == PlayCall.PASS) {
            return currentPassYards + (play.yards)
        }
        return currentPassYards
    }

    private fun calculateRushAttempts(
        play: Play,
        currentRushAttempts: Int,
    ): Int {
        if (play.playCall == PlayCall.RUN) {
            return currentRushAttempts + 1
        }
        return currentRushAttempts
    }

    private fun calculateRushSuccesses(
        play: Play,
        currentRushSuccesses: Int,
    ): Int {
        if (play.playCall != PlayCall.RUN) return currentRushSuccesses

        val yardsToGo = play.yardsToGo
        val yardsGained = play.yards
        val down = play.down

        val isSuccess =
            when (down) {
                1 -> yardsGained >= (yardsToGo * 0.5)
                2 -> yardsGained >= (yardsToGo * 0.7)
                3, 4 -> yardsGained >= yardsToGo
                else -> false
            }

        return if (isSuccess || play.actualResult == ActualResult.TOUCHDOWN) {
            currentRushSuccesses + 1
        } else {
            currentRushSuccesses
        }
    }

    private fun calculateRushYards(
        play: Play,
        currentRushYards: Int,
    ): Int {
        // Count sacks towards rushing yards
        if (play.playCall == PlayCall.PASS && (
                play.result == Scenario.LOSS_OF_10_YARDS ||
                    play.result == Scenario.LOSS_OF_7_YARDS ||
                    play.result == Scenario.LOSS_OF_5_YARDS ||
                    play.result == Scenario.LOSS_OF_3_YARDS ||
                    play.result == Scenario.LOSS_OF_2_YARDS ||
                    play.result == Scenario.LOSS_OF_1_YARD
            )
        ) {
            return currentRushYards + (play.yards)
        }
        if (play.playCall == PlayCall.RUN && (
                play.result != Scenario.TURNOVER_PLUS_20_YARDS &&
                    play.result != Scenario.TURNOVER_PLUS_15_YARDS &&
                    play.result != Scenario.TURNOVER_PLUS_10_YARDS &&
                    play.result != Scenario.TURNOVER_PLUS_5_YARDS &&
                    play.result != Scenario.TURNOVER &&
                    play.result != Scenario.TURNOVER_MINUS_5_YARDS &&
                    play.result != Scenario.TURNOVER_MINUS_10_YARDS &&
                    play.result != Scenario.TURNOVER_MINUS_15_YARDS &&
                    play.result != Scenario.TURNOVER_MINUS_20_YARDS &&
                    play.result != Scenario.TURNOVER_TOUCHDOWN &&
                    play.result != Scenario.SAFETY
            )
        ) {
            return currentRushYards + (play.yards)
        }
        return currentRushYards
    }

    private fun calculateTotalYards(
        passingYards: Int,
        rushingYards: Int,
    ): Int {
        return passingYards + rushingYards
    }

    private fun calculateInterceptionsLost(
        play: Play,
        currentInterceptionsLost: Int,
    ): Int {
        if (play.playCall == PlayCall.PASS && (
                play.actualResult == ActualResult.TURNOVER ||
                    play.actualResult == ActualResult.TURNOVER_TOUCHDOWN
            )
        ) {
            return currentInterceptionsLost + 1
        }
        return currentInterceptionsLost
    }

    private fun calculateFumblesLost(
        play: Play,
        currentFumblesLost: Int,
    ): Int {
        if (play.playCall == PlayCall.RUN &&
            (
                play.actualResult == ActualResult.TURNOVER ||
                    play.actualResult == ActualResult.TURNOVER_TOUCHDOWN
            )
        ) {
            return currentFumblesLost + 1
        }
        return currentFumblesLost
    }

    private fun calculateTurnoversLost(
        interceptionsLost: Int,
        fumblesLost: Int,
    ): Int {
        return interceptionsLost + fumblesLost
    }

    private fun calculateTurnoverTouchdownsLost(
        play: Play,
        currentTurnoverTouchdownsLost: Int,
    ): Int {
        if (play.actualResult == ActualResult.TURNOVER_TOUCHDOWN) {
            return currentTurnoverTouchdownsLost + 1
        }
        return currentTurnoverTouchdownsLost
    }

    private fun calculateFieldGoalMade(
        play: Play,
        currentFieldGoalMade: Int,
    ): Int {
        if (play.playCall == PlayCall.FIELD_GOAL && play.result == Scenario.GOOD) {
            return currentFieldGoalMade + 1
        }
        return currentFieldGoalMade
    }

    private fun calculateFieldGoalAttempts(
        play: Play,
        currentFieldGoalAttempts: Int,
    ): Int {
        if (play.playCall == PlayCall.FIELD_GOAL) {
            return currentFieldGoalAttempts + 1
        }
        return currentFieldGoalAttempts
    }

    private fun calculateLongestFieldGoal(
        play: Play,
        currentLongestFieldGoal: Int,
    ): Int {
        if (play.playCall == PlayCall.FIELD_GOAL && play.result == Scenario.GOOD && (
                ((play.ballLocation) + 17) > currentLongestFieldGoal
            )
        ) {
            return (play.ballLocation) + 17
        }
        return currentLongestFieldGoal
    }

    private fun calculateBlockedOpponentFieldGoals(
        play: Play,
        currentBlockedOpponentFieldGoals: Int,
    ): Int {
        if (play.playCall == PlayCall.FIELD_GOAL && play.actualResult == ActualResult.BLOCKED) {
            return currentBlockedOpponentFieldGoals + 1
        }
        return currentBlockedOpponentFieldGoals
    }

    private fun calculateFieldGoalTouchdown(
        play: Play,
        currentFieldGoalTouchdown: Int,
    ): Int {
        if (play.playCall == PlayCall.FIELD_GOAL && play.actualResult == ActualResult.KICK_SIX) {
            return currentFieldGoalTouchdown + 1
        }
        return currentFieldGoalTouchdown
    }

    private fun calculatePuntsAttempted(
        play: Play,
        currentPuntsAttempted: Int,
    ): Int {
        if (play.playCall == PlayCall.PUNT) {
            return currentPuntsAttempted + 1
        }
        return currentPuntsAttempted
    }

    private fun calculateLongestPunt(
        play: Play,
        currentLongestPunt: Int,
    ): Int {
        val puntDisances =
            listOf(
                Scenario.FIVE_YARD_PUNT, Scenario.TEN_YARD_PUNT, Scenario.FIFTEEN_YARD_PUNT, Scenario.TWENTY_YARD_PUNT,
                Scenario.TWENTY_FIVE_YARD_PUNT, Scenario.THIRTY_YARD_PUNT, Scenario.THIRTY_FIVE_YARD_PUNT,
                Scenario.FORTY_YARD_PUNT, Scenario.FORTY_FIVE_YARD_PUNT, Scenario.FIFTY_YARD_PUNT,
                Scenario.FIFTY_FIVE_YARD_PUNT, Scenario.SIXTY_YARD_PUNT, Scenario.SIXTY_FIVE_YARD_PUNT,
                Scenario.SEVENTY_YARD_PUNT,
            )
        if (play.result in puntDisances) {
            val puntDistance = play.result?.description?.substringBefore(" YARD PUNT")?.toInt() ?: 0
            if ((puntDistance) > currentLongestPunt) {
                return puntDistance
            }
        }
        return currentLongestPunt
    }

    private fun calculateAveragePuntLength(
        allPlays: List<Play>,
        possession: TeamSide,
    ): Double {
        val average =
            allPlays.filter {
                it.playCall == PlayCall.PUNT && it.possession == possession &&
                    it.result?.description?.contains(" YARD PUNT") ?: false
            }.map {
                it.result?.description?.substringBefore(" YARD PUNT")?.toInt() ?: 0
            }.average()
        return if (average.isNaN()) {
            0.0
        } else {
            average
        }
    }

    private fun calculateBlockedOpponentPunt(
        play: Play,
        currentBlockedOpponentPunt: Int,
    ): Int {
        if (play.playCall == PlayCall.PUNT && play.actualResult == ActualResult.BLOCKED) {
            return currentBlockedOpponentPunt + 1
        }
        return currentBlockedOpponentPunt
    }

    private fun calculatePuntReturnTd(
        play: Play,
        currentPuntReturnTd: Int,
    ): Int {
        if (play.playCall == PlayCall.PUNT && play.actualResult == ActualResult.PUNT_RETURN_TOUCHDOWN) {
            return currentPuntReturnTd + 1
        }
        return currentPuntReturnTd
    }

    private fun calculateNumberOfKickoffs(
        play: Play,
        currentNumberOfKickoffs: Int,
    ): Int {
        if (play.playCall == PlayCall.KICKOFF_NORMAL ||
            play.playCall == PlayCall.KICKOFF_ONSIDE ||
            play.playCall == PlayCall.KICKOFF_SQUIB
        ) {
            return currentNumberOfKickoffs + 1
        }
        return currentNumberOfKickoffs
    }

    private fun calculateOnsideAttempts(
        play: Play,
        currentOnsideAttempts: Int,
    ): Int {
        if (play.playCall == PlayCall.KICKOFF_ONSIDE) {
            return currentOnsideAttempts + 1
        }
        return currentOnsideAttempts
    }

    private fun calculateOnsideSuccess(
        play: Play,
        currentOnsideSuccess: Int,
    ): Int {
        if (play.playCall == PlayCall.KICKOFF_ONSIDE && play.result == Scenario.RECOVERED) {
            return currentOnsideSuccess + 1
        }
        return currentOnsideSuccess
    }

    private fun calculateNormalKickoffAttempts(
        play: Play,
        currentNormalKickoffAttempts: Int,
    ): Int {
        if (play.playCall == PlayCall.KICKOFF_NORMAL) {
            return currentNormalKickoffAttempts + 1
        }
        return currentNormalKickoffAttempts
    }

    private fun calculateTouchbacks(
        play: Play,
        currentTouchbacks: Int,
    ): Int {
        if (play.playCall == PlayCall.KICKOFF_NORMAL && play.result == Scenario.TOUCHBACK) {
            return currentTouchbacks + 1
        }
        return currentTouchbacks
    }

    private fun calculateKickReturnTd(
        play: Play,
        currentKickReturnTd: Int,
    ): Int {
        if (play.playCall == PlayCall.KICKOFF_NORMAL && play.actualResult == ActualResult.RETURN_TOUCHDOWN) {
            return currentKickReturnTd + 1
        }
        return currentKickReturnTd
    }

    private fun calculateNumberOfDrives(
        allPlays: List<Play>,
        possession: TeamSide,
    ): Int {
        var driveCount = 0
        var isDriveInProgress = false

        allPlays.sortedBy { it.playId }.forEach { play ->
            when {
                // If the current play is a kickoff, end the current drive
                (
                    play.playCall == PlayCall.KICKOFF_NORMAL ||
                        play.playCall == PlayCall.KICKOFF_ONSIDE ||
                        play.playCall == PlayCall.KICKOFF_SQUIB
                ) && play.possession == possession -> {
                    isDriveInProgress = false
                }

                // Player starts or continues a drive (possession belongs to the player)
                play.possession == possession && !isDriveInProgress -> {
                    // Start a new drive
                    driveCount++
                    isDriveInProgress = true
                }

                // If possession changes to another player or a turnover happens
                play.possession != possession ||
                    play.actualResult == ActualResult.TURNOVER ||
                    play.actualResult == ActualResult.TURNOVER_TOUCHDOWN -> {
                    // End the current drive
                    isDriveInProgress = false
                }
            }
        }

        return driveCount
    }

    private fun calculateTimeOfPossession(
        allPlays: List<Play>,
        possession: TeamSide,
    ): Int {
        return allPlays.filter {
            it.possession == possession &&
                it.playCall != PlayCall.KICKOFF_NORMAL &&
                it.playCall != PlayCall.KICKOFF_ONSIDE &&
                it.playCall != PlayCall.KICKOFF_SQUIB
        }.sumOf { (it.playTime) + (it.runoffTime) }
    }

    private fun calculateTouchdowns(
        allPlays: List<Play>,
        possession: TeamSide,
    ): Int {
        val offensiveTouchdowns =
            allPlays.count {
                it.possession == possession &&
                    (
                        it.actualResult == ActualResult.TOUCHDOWN ||
                            it.actualResult == ActualResult.KICKING_TEAM_TOUCHDOWN ||
                            it.actualResult == ActualResult.PUNT_TEAM_TOUCHDOWN
                    )
            }
        val defensiveTouchdowns =
            allPlays.count {
                it.possession != possession &&
                    (
                        it.actualResult == ActualResult.TURNOVER_TOUCHDOWN ||
                            it.actualResult == ActualResult.RETURN_TOUCHDOWN ||
                            it.actualResult == ActualResult.PUNT_RETURN_TOUCHDOWN ||
                            it.actualResult == ActualResult.KICK_SIX
                    )
            }
        return offensiveTouchdowns + defensiveTouchdowns
    }

    private fun calculateAverageOffensiveDiff(
        allPlays: List<Play>,
        possession: TeamSide,
    ): Double {
        val average =
            allPlays.filter {
                it.possession == possession &&
                    it.playCall != PlayCall.KICKOFF_NORMAL &&
                    it.playCall != PlayCall.KICKOFF_ONSIDE &&
                    it.playCall != PlayCall.KICKOFF_SQUIB &&
                    it.playCall != PlayCall.PAT &&
                    it.playCall != PlayCall.TWO_POINT &&
                    it.playCall != PlayCall.KNEEL &&
                    it.playCall != PlayCall.SPIKE
            }.map { it.difference }.average()
        return if (average.isNaN()) {
            0.0
        } else {
            average
        }
    }

    private fun calculateAverageDefensiveDiff(
        allPlays: List<Play>,
        possession: TeamSide,
    ): Double {
        val average =
            allPlays.filter {
                it.possession != possession &&
                    it.playCall != PlayCall.KICKOFF_NORMAL &&
                    it.playCall != PlayCall.KICKOFF_ONSIDE &&
                    it.playCall != PlayCall.KICKOFF_SQUIB &&
                    it.playCall != PlayCall.PAT &&
                    it.playCall != PlayCall.TWO_POINT &&
                    it.playCall != PlayCall.KNEEL &&
                    it.playCall != PlayCall.SPIKE
            }.map { it.difference }.average()
        return if (average.isNaN()) {
            0.0
        } else {
            average
        }
    }

    private fun calculateAverageOffensiveSpecialTeamsDiff(
        allPlays: List<Play>,
        possession: TeamSide,
    ): Double {
        val average =
            allPlays.filter {
                it.possession == possession &&
                    (
                        it.playCall == PlayCall.KICKOFF_NORMAL ||
                            it.playCall == PlayCall.KICKOFF_ONSIDE ||
                            it.playCall == PlayCall.KICKOFF_SQUIB ||
                            it.playCall == PlayCall.FIELD_GOAL ||
                            it.playCall == PlayCall.PUNT
                    )
            }.map { it.difference }.average()
        return if (average.isNaN()) {
            0.0
        } else {
            average
        }
    }

    private fun calculateAverageDefensiveSpecialTeamsDiff(
        allPlays: List<Play>,
        possession: TeamSide,
    ): Double {
        val average =
            allPlays.filter {
                it.possession != possession &&
                    (
                        it.playCall == PlayCall.KICKOFF_NORMAL ||
                            it.playCall == PlayCall.KICKOFF_ONSIDE ||
                            it.playCall == PlayCall.KICKOFF_SQUIB ||
                            it.playCall == PlayCall.FIELD_GOAL ||
                            it.playCall == PlayCall.PUNT
                    )
            }.map { it.difference }.average()
        return if (average.isNaN()) {
            0.0
        } else {
            average
        }
    }

    private fun calculateAverageYardsPerPlay(
        allPlays: List<Play>,
        possession: TeamSide,
    ): Double {
        val average =
            allPlays.filter {
                it.possession == possession &&
                    it.playCall != PlayCall.KICKOFF_NORMAL &&
                    it.playCall != PlayCall.KICKOFF_ONSIDE &&
                    it.playCall != PlayCall.KICKOFF_SQUIB &&
                    it.playCall != PlayCall.PAT &&
                    it.playCall != PlayCall.TWO_POINT
            }.map { it.yards }.average()
        return if (average.isNaN()) {
            0.0
        } else {
            average
        }
    }

    private fun calculateThirdDownConversionSuccess(
        play: Play,
        currentThirdDownConversionSuccess: Int,
    ): Int {
        if (play.down == 3 && (play.yards) > (play.yardsToGo)) {
            return currentThirdDownConversionSuccess + 1
        }
        return currentThirdDownConversionSuccess
    }

    private fun calculateThirdDownConversionAttempts(
        play: Play,
        currentThirdDownConversionAttempts: Int,
    ): Int {
        if (play.down == 3) {
            return currentThirdDownConversionAttempts + 1
        }
        return currentThirdDownConversionAttempts
    }

    private fun calculateFourthDownConversionSuccess(
        play: Play,
        currentFourthDownConversionSuccess: Int,
    ): Int {
        if (play.down == 4 && (play.yards) > (play.yardsToGo)) {
            return currentFourthDownConversionSuccess + 1
        }
        return currentFourthDownConversionSuccess
    }

    private fun calculateFourthDownConversionAttempts(
        play: Play,
        currentFourthDownConversionAttempts: Int,
    ): Int {
        if (play.down == 4 && (play.playCall == PlayCall.RUN || play.playCall == PlayCall.PASS)) {
            return currentFourthDownConversionAttempts + 1
        }
        return currentFourthDownConversionAttempts
    }

    private fun calculateLargestLead(
        game: Game,
        currentLargestLead: Int,
        possession: TeamSide,
    ): Int {
        if (possession == TeamSide.HOME) {
            if ((game.homeScore) - (game.awayScore) > currentLargestLead) {
                return (game.homeScore) - (game.awayScore)
            }
        } else if (possession == TeamSide.AWAY) {
            if ((game.awayScore) - (game.homeScore) > currentLargestLead) {
                return (game.awayScore) - (game.homeScore)
            }
        }
        return currentLargestLead
    }

    private fun calculateLargestDeficit(
        game: Game,
        currentLargestDeficit: Int,
        possession: TeamSide,
    ): Int {
        if (possession == TeamSide.HOME) {
            if ((game.awayScore) - (game.homeScore) > currentLargestDeficit) {
                return (game.awayScore) - (game.homeScore)
            }
        } else if (possession == TeamSide.AWAY) {
            if ((game.homeScore) - (game.awayScore) > currentLargestDeficit) {
                return (game.homeScore) - (game.awayScore)
            }
        }
        return currentLargestDeficit
    }

    private fun calculatePassTouchdowns(
        play: Play,
        currentPassTouchdowns: Int,
    ): Int {
        if (play.playCall == PlayCall.PASS && play.actualResult == ActualResult.TOUCHDOWN) {
            return currentPassTouchdowns + 1
        }
        return currentPassTouchdowns
    }

    private fun calculateRushTouchdowns(
        play: Play,
        currentRushTouchdowns: Int,
    ): Int {
        if (play.playCall == PlayCall.RUN && play.actualResult == ActualResult.TOUCHDOWN) {
            return currentRushTouchdowns + 1
        }
        return currentRushTouchdowns
    }

    private fun calculateRedZoneAttempts(
        allPlays: List<Play>,
        possession: TeamSide,
    ): Int {
        return allPlays.count {
            it.possession == possession &&
                (it.ballLocation) >= 80
        }
    }

    private fun calculateRedZoneSuccesses(
        allPlays: List<Play>,
        possession: TeamSide,
    ): Int {
        return allPlays.count {
            it.possession == possession &&
                (it.ballLocation) >= 80 &&
                it.actualResult == ActualResult.TOUCHDOWN
        }
    }

    private fun calculateAverageDiff(allPlays: List<Play>): Double {
        val average = allPlays.map { it.difference }.average()
        return if (average.isNaN()) {
            0.0
        } else {
            average
        }
    }

    private fun calculateTurnoverDifferential(
        turnoversLost: Int,
        turnoversForced: Int,
    ): Int {
        return turnoversForced - turnoversLost
    }

    private fun calculatePickSixes(
        play: Play,
        currentPickSixes: Int,
    ): Int {
        if (play.playCall == PlayCall.PASS && play.actualResult == ActualResult.TURNOVER_TOUCHDOWN) {
            return currentPickSixes + 1
        }
        return currentPickSixes
    }

    private fun calculateFumbleReturnTds(
        play: Play,
        currentFumbleReturnTds: Int,
    ): Int {
        if (play.playCall == PlayCall.RUN && play.actualResult == ActualResult.TURNOVER_TOUCHDOWN) {
            return currentFumbleReturnTds + 1
        }
        return currentFumbleReturnTds
    }

    private fun calculateSafeties(
        play: Play,
        currentSafeties: Int,
    ): Int {
        if (play.actualResult == ActualResult.SAFETY) {
            return currentSafeties + 1
        }
        return currentSafeties
    }

    private fun calculateQuarterScore(
        play: Play,
        currentQuarterScore: Int,
        possession: TeamSide,
    ): Int {
        if (play.possession == possession) {
            if (play.actualResult == ActualResult.TOUCHDOWN || play.actualResult == ActualResult.KICKING_TEAM_TOUCHDOWN ||
                play.actualResult == ActualResult.PUNT_RETURN_TOUCHDOWN
            ) {
                return currentQuarterScore + 6
            }
            if (play.playCall == PlayCall.PAT && play.actualResult == ActualResult.GOOD) {
                return currentQuarterScore + 1
            }
            if (play.playCall == PlayCall.TWO_POINT && play.actualResult == ActualResult.GOOD) {
                return currentQuarterScore + 2
            }
            if (play.playCall == PlayCall.FIELD_GOAL && play.result == Scenario.GOOD) {
                return currentQuarterScore + 3
            }
        }
        if (play.possession != possession) {
            if (play.actualResult == ActualResult.TURNOVER_TOUCHDOWN || play.actualResult == ActualResult.RETURN_TOUCHDOWN ||
                play.actualResult == ActualResult.PUNT_RETURN_TOUCHDOWN || play.actualResult == ActualResult.KICK_SIX
            ) {
                return currentQuarterScore + 6
            }
            if (play.playCall == PlayCall.PAT && play.actualResult == ActualResult.DEFENSE_TWO_POINT) {
                return currentQuarterScore + 1
            }
            if (play.playCall == PlayCall.TWO_POINT && play.actualResult == ActualResult.DEFENSE_TWO_POINT) {
                return currentQuarterScore + 2
            }
            if (play.actualResult == ActualResult.SAFETY) {
                return currentQuarterScore + 2
            }
        }
        return currentQuarterScore
    }

    private fun calculatePercentage(
        successes: Int,
        attempts: Int,
    ): Double {
        if (attempts == 0) {
            return 0.0
        }
        return (successes.toDouble() / attempts.toDouble()) * 100
    }
}
