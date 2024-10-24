package com.fcfb.arceus.handlers.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.ActualResult
import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.Scenario
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.domain.GameStats
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.repositories.GameStatsRepository
import com.fcfb.arceus.repositories.PlayRepository
import org.springframework.stereotype.Component

@Component
class StatsHandler(
    private val gameStatsRepository: GameStatsRepository,
    private val playRepository: PlayRepository,
) {
    fun updateGameStats(
        game: Game,
        play: Play,
    ): GameStats? {
        val stats = gameStatsRepository.findByGameId(game.gameId ?: return null) ?: return null
        val allPlays = playRepository.findAllByGameId(game.gameId ?: return null) ?: return null

        if (game.possession == TeamSide.HOME) {
            stats.homeScore = game.homeScore
            stats.homePassAttempts =
                calculatePassAttempts(
                    play, stats.homePassAttempts ?: 0,
                )
            stats.homePassCompletions =
                calculatePassCompletions(
                    play, stats.homePassCompletions ?: 0,
                )
            stats.homePassCompletionPercentage =
                calculatePercentage(
                    stats.homePassCompletions ?: 0, stats.homePassAttempts ?: 0,
                )
            stats.homePassYards =
                calculatePassYards(
                    play, stats.homePassYards ?: 0,
                )
            stats.homeRushAttempts =
                calculateRushAttempts(
                    play, stats.homeRushAttempts ?: 0,
                )
            stats.homeRushSuccesses =
                calculateRushSuccesses(
                    play, stats.homeRushSuccesses ?: 0,
                )
            stats.homeRushSuccessPercentage =
                calculatePercentage(
                    stats.homeRushSuccesses ?: 0, stats.homeRushAttempts ?: 0,
                )
            stats.homeRushYards =
                calculateRushYards(
                    play, stats.homeRushYards ?: 0,
                )
            stats.homeTotalYards =
                calculateTotalYards(
                    stats.homePassYards ?: 0, stats.homeRushYards ?: 0,
                )
            stats.homeInterceptionsLost =
                calculateInterceptionsLost(
                    play, stats.homeInterceptionsLost ?: 0,
                )
            stats.awayInterceptionsForced = stats.homeInterceptionsLost
            stats.homeFumblesLost =
                calculateFumblesLost(
                    play, stats.homeFumblesLost ?: 0,
                )
            stats.awayFumblesForced = stats.homeFumblesLost
            stats.homeTurnoversLost =
                calculateTurnoversLost(
                    stats.homeInterceptionsLost ?: 0, stats.homeFumblesLost ?: 0,
                )
            stats.awayTurnoversForced = stats.homeTurnoversLost
            stats.homeTurnoverTouchdownsLost =
                calculateTurnoverTouchdownsLost(
                    play, stats.homeTurnoverTouchdownsLost ?: 0,
                )
            stats.awayTurnoverTouchdownsForced = stats.homeTurnoverTouchdownsLost
            stats.homeFieldGoalMade =
                calculateFieldGoalMade(
                    play, stats.homeFieldGoalMade ?: 0,
                )
            stats.homeFieldGoalAttempts =
                calculateFieldGoalAttempts(
                    play, stats.homeFieldGoalAttempts ?: 0,
                )
            stats.homeFieldGoalPercentage =
                calculatePercentage(
                    stats.homeFieldGoalMade ?: 0, stats.homeFieldGoalAttempts ?: 0,
                )
            stats.homeLongestFieldGoal =
                calculateLongestFieldGoal(
                    play, stats.homeLongestFieldGoal ?: 0,
                )
            stats.awayBlockedOpponentFieldGoals =
                calculateBlockedOpponentFieldGoals(
                    play, stats.awayBlockedOpponentFieldGoals ?: 0,
                )
            stats.homeFieldGoalTouchdown =
                calculateFieldGoalTouchdown(
                    play, stats.homeFieldGoalTouchdown ?: 0,
                )
            stats.homePuntsAttempted =
                calculatePuntsAttempted(
                    play, stats.homePuntsAttempted ?: 0,
                )
            stats.homeLongestPunt =
                calculateLongestPunt(
                    play, stats.homeLongestPunt ?: 0,
                )
            stats.homeAveragePuntLength =
                calculateAveragePuntLength(
                    allPlays, TeamSide.HOME,
                )
            stats.awayBlockedOpponentPunt =
                calculateBlockedOpponentPunt(
                    play, stats.awayBlockedOpponentPunt ?: 0,
                )
            stats.awayPuntReturnTd =
                calculatePuntReturnTd(
                    play, stats.awayPuntReturnTd ?: 0,
                )
            stats.awayPuntReturnTdPercentage =
                calculatePercentage(
                    stats.awayPuntReturnTd ?: 0, stats.awayPuntsAttempted ?: 0,
                )
            stats.homeNumberOfKickoffs =
                calculateNumberOfKickoffs(
                    play, stats.homeNumberOfKickoffs ?: 0,
                )
            stats.homeOnsideAttempts =
                calculateOnsideAttempts(
                    play, stats.homeOnsideAttempts ?: 0,
                )
            stats.homeOnsideSuccess =
                calculateOnsideSuccess(
                    play, stats.homeOnsideSuccess ?: 0,
                )
            stats.homeOnsideSuccessPercentage =
                calculatePercentage(
                    stats.homeOnsideSuccess ?: 0, stats.homeOnsideAttempts ?: 0,
                )
            stats.homeNormalKickoffAttempts =
                calculateNormalKickoffAttempts(
                    play, stats.homeNormalKickoffAttempts ?: 0,
                )
            stats.homeTouchbacks =
                calculateTouchbacks(
                    play, stats.homeTouchbacks ?: 0,
                )
            stats.homeTouchbackPercentage =
                calculatePercentage(
                    stats.homeTouchbacks ?: 0, stats.homeNormalKickoffAttempts ?: 0,
                )
            stats.awayKickReturnTd =
                calculateKickReturnTd(
                    play, stats.awayKickReturnTd ?: 0,
                )
            stats.awayKickReturnTdPercentage =
                calculatePercentage(
                    stats.awayKickReturnTd ?: 0, stats.awayNumberOfKickoffs ?: 0,
                )
            stats.homeNumberOfDrives =
                calculateNumberOfDrives(
                    allPlays, TeamSide.HOME,
                )
            stats.homeTimeOfPossession =
                calculateTimeOfPossession(
                    allPlays, TeamSide.HOME,
                )
            stats.homeTouchdowns =
                calculateTouchdowns(
                    allPlays, TeamSide.HOME,
                )
            stats.averageHomeOffensiveDiff =
                calculateAverageOffensiveDiff(
                    allPlays, TeamSide.HOME,
                )
            stats.averageHomeDefensiveDiff =
                calculateAverageDefensiveDiff(
                    allPlays, TeamSide.HOME,
                )
            stats.averageHomeOffensiveSpecialTeamsDiff =
                calculateAverageOffensiveSpecialTeamsDiff(
                    allPlays, TeamSide.HOME,
                )
            stats.averageHomeDefensiveSpecialTeamsDiff =
                calculateAverageDefensiveSpecialTeamsDiff(
                    allPlays, TeamSide.HOME,
                )
            stats.homeAverageYardsPerPlay =
                calculateAverageYardsPerPlay(
                    allPlays, TeamSide.HOME,
                )
            stats.homeThirdDownConversionSuccess =
                calculateThirdDownConversionSuccess(
                    play, stats.homeThirdDownConversionSuccess ?: 0,
                )
            stats.homeThirdDownConversionAttempts =
                calculateThirdDownConversionAttempts(
                    play, stats.homeThirdDownConversionAttempts ?: 0,
                )
            stats.homeThirdDownConversionPercentage =
                calculatePercentage(
                    stats.homeThirdDownConversionSuccess ?: 0, stats.homeThirdDownConversionAttempts ?: 0,
                )
            stats.homeFourthDownConversionSuccess =
                calculateFourthDownConversionSuccess(
                    play, stats.homeFourthDownConversionSuccess ?: 0,
                )
            stats.homeFourthDownConversionAttempts =
                calculateFourthDownConversionAttempts(
                    play, stats.homeFourthDownConversionAttempts ?: 0,
                )
            stats.homeFourthDownConversionPercentage =
                calculatePercentage(
                    stats.homeFourthDownConversionSuccess ?: 0, stats.homeFourthDownConversionAttempts ?: 0,
                )
            stats.homeLargestLead =
                calculateLargestLead(
                    game, stats.homeLargestLead ?: 0, TeamSide.HOME,
                )
            stats.homeLargestDeficit =
                calculateLargestDeficit(
                    game, stats.homeLargestDeficit ?: 0, TeamSide.AWAY,
                )
            stats.homePassTouchdowns =
                calculatePassTouchdowns(
                    play, stats.homePassTouchdowns ?: 0,
                )
            stats.homeRushTouchdowns =
                calculateRushTouchdowns(
                    play, stats.homeRushTouchdowns ?: 0,
                )
            stats.homeRedZoneAttempts =
                calculateRedZoneAttempts(
                    allPlays, TeamSide.HOME,
                )
            stats.homeRedZoneSuccesses =
                calculateRedZoneSuccesses(
                    allPlays, TeamSide.HOME,
                )
            stats.homeRedZoneSuccessPercentage =
                calculatePercentage(
                    stats.homeRedZoneSuccesses ?: 0, stats.homeRedZoneAttempts ?: 0,
                )
            stats.homeTurnoverDifferential =
                calculateTurnoverDifferential(
                    stats.homeTurnoversLost ?: 0, stats.homeTurnoversForced ?: 0,
                )
            stats.homePickSixesThrown =
                calculatePickSixes(
                    play, stats.homePickSixesThrown ?: 0,
                )
            stats.awayPickSixesForced = stats.homePickSixesThrown
            stats.homeFumbleReturnTdsCommitted =
                calculateFumbleReturnTds(
                    play, stats.homeFumbleReturnTdsCommitted ?: 0,
                )
            stats.awayFumbleReturnTdsForced = stats.homeFumbleReturnTdsCommitted
            stats.homeSafetiesCommitted =
                calculateSafeties(
                    play, stats.homeSafetiesCommitted ?: 0,
                )
            stats.awaySafetiesForced = stats.homeSafetiesCommitted
        } else {
            stats.awayScore = game.awayScore
            stats.awayPassAttempts =
                calculatePassAttempts(
                    play, stats.awayPassAttempts ?: 0,
                )
            stats.awayPassCompletions =
                calculatePassCompletions(
                    play, stats.awayPassCompletions ?: 0,
                )
            stats.awayPassCompletionPercentage =
                calculatePercentage(
                    stats.awayPassCompletions ?: 0, stats.awayPassAttempts ?: 0,
                )
            stats.awayPassYards =
                calculatePassYards(
                    play, stats.awayPassYards ?: 0,
                )
            stats.awayRushAttempts =
                calculateRushAttempts(
                    play, stats.awayRushAttempts ?: 0,
                )
            stats.awayRushSuccesses =
                calculateRushSuccesses(
                    play, stats.awayRushSuccesses ?: 0,
                )
            stats.awayRushSuccessPercentage =
                calculatePercentage(
                    stats.awayRushSuccesses ?: 0, stats.awayRushAttempts ?: 0,
                )
            stats.awayRushYards =
                calculateRushYards(
                    play, stats.awayRushYards ?: 0,
                )
            stats.awayTotalYards =
                calculateTotalYards(
                    stats.awayPassYards ?: 0, stats.awayRushYards ?: 0,
                )
            stats.awayInterceptionsLost =
                calculateInterceptionsLost(
                    play, stats.awayInterceptionsLost ?: 0,
                )
            stats.homeInterceptionsForced = stats.awayInterceptionsLost
            stats.awayFumblesLost =
                calculateFumblesLost(
                    play, stats.awayFumblesLost ?: 0,
                )
            stats.homeFumblesForced = stats.awayFumblesLost
            stats.awayTurnoversLost =
                calculateTurnoversLost(
                    stats.awayInterceptionsLost ?: 0, stats.awayFumblesLost ?: 0,
                )
            stats.homeTurnoversForced = stats.awayTurnoversLost
            stats.awayTurnoverTouchdownsLost =
                calculateTurnoverTouchdownsLost(
                    play, stats.awayTurnoverTouchdownsLost ?: 0,
                )
            stats.homeTurnoverTouchdownsForced = stats.awayTurnoverTouchdownsLost
            stats.awayFieldGoalMade =
                calculateFieldGoalMade(
                    play, stats.awayFieldGoalMade ?: 0,
                )
            stats.awayFieldGoalAttempts =
                calculateFieldGoalAttempts(
                    play, stats.awayFieldGoalAttempts ?: 0,
                )
            stats.awayFieldGoalPercentage =
                calculatePercentage(
                    stats.awayFieldGoalMade ?: 0, stats.awayFieldGoalAttempts ?: 0,
                )
            stats.awayLongestFieldGoal =
                calculateLongestFieldGoal(
                    play, stats.awayLongestFieldGoal ?: 0,
                )
            stats.homeBlockedOpponentFieldGoals =
                calculateBlockedOpponentFieldGoals(
                    play, stats.homeBlockedOpponentFieldGoals ?: 0,
                )
            stats.homeFieldGoalTouchdown =
                calculateFieldGoalTouchdown(
                    play, stats.homeFieldGoalTouchdown ?: 0,
                )
            stats.awayPuntsAttempted =
                calculatePuntsAttempted(
                    play, stats.awayPuntsAttempted ?: 0,
                )
            stats.awayLongestPunt =
                calculateLongestPunt(
                    play, stats.awayLongestPunt ?: 0,
                )
            stats.awayAveragePuntLength =
                calculateAveragePuntLength(
                    allPlays, TeamSide.AWAY,
                )
            stats.homeBlockedOpponentPunt =
                calculateBlockedOpponentPunt(
                    play, stats.homeBlockedOpponentPunt ?: 0,
                )
            stats.homePuntReturnTd =
                calculatePuntReturnTd(
                    play, stats.homePuntReturnTd ?: 0,
                )
            stats.homePuntReturnTdPercentage =
                calculatePercentage(
                    stats.homePuntReturnTd ?: 0, stats.homePuntsAttempted ?: 0,
                )
            stats.awayNumberOfKickoffs =
                calculateNumberOfKickoffs(
                    play, stats.awayNumberOfKickoffs ?: 0,
                )
            stats.awayOnsideAttempts =
                calculateOnsideAttempts(
                    play, stats.awayOnsideAttempts ?: 0,
                )
            stats.awayOnsideSuccess =
                calculateOnsideSuccess(
                    play, stats.awayOnsideSuccess ?: 0,
                )
            stats.awayOnsideSuccessPercentage =
                calculatePercentage(
                    stats.awayOnsideSuccess ?: 0, stats.awayOnsideAttempts ?: 0,
                )
            stats.awayNormalKickoffAttempts =
                calculateNormalKickoffAttempts(
                    play, stats.awayNormalKickoffAttempts ?: 0,
                )
            stats.awayTouchbacks =
                calculateTouchbacks(
                    play, stats.awayTouchbacks ?: 0,
                )
            stats.awayTouchbackPercentage =
                calculatePercentage(
                    stats.awayTouchbacks ?: 0, stats.awayNormalKickoffAttempts ?: 0,
                )
            stats.homeKickReturnTd =
                calculateKickReturnTd(
                    play, stats.homeKickReturnTd ?: 0,
                )
            stats.homeKickReturnTdPercentage =
                calculatePercentage(
                    stats.homeKickReturnTd ?: 0, stats.homeNumberOfKickoffs ?: 0,
                )
            stats.awayNumberOfDrives =
                calculateNumberOfDrives(
                    allPlays, TeamSide.AWAY,
                )
            stats.awayTimeOfPossession =
                calculateTimeOfPossession(
                    allPlays, TeamSide.AWAY,
                )
            stats.awayTouchdowns =
                calculateTouchdowns(
                    allPlays, TeamSide.AWAY,
                )
            stats.averageAwayOffensiveDiff =
                calculateAverageOffensiveDiff(
                    allPlays, TeamSide.AWAY,
                )
            stats.averageAwayDefensiveDiff =
                calculateAverageDefensiveDiff(
                    allPlays, TeamSide.AWAY,
                )
            stats.averageAwayOffensiveSpecialTeamsDiff =
                calculateAverageOffensiveSpecialTeamsDiff(
                    allPlays, TeamSide.AWAY,
                )
            stats.averageAwayDefensiveSpecialTeamsDiff =
                calculateAverageDefensiveSpecialTeamsDiff(
                    allPlays, TeamSide.AWAY,
                )
            stats.awayAverageYardsPerPlay =
                calculateAverageYardsPerPlay(
                    allPlays, TeamSide.AWAY,
                )
            stats.awayThirdDownConversionSuccess =
                calculateThirdDownConversionSuccess(
                    play, stats.awayThirdDownConversionSuccess ?: 0,
                )
            stats.awayThirdDownConversionAttempts =
                calculateThirdDownConversionAttempts(
                    play, stats.awayThirdDownConversionAttempts ?: 0,
                )
            stats.awayThirdDownConversionPercentage =
                calculatePercentage(
                    stats.awayThirdDownConversionSuccess ?: 0, stats.awayThirdDownConversionAttempts ?: 0,
                )
            stats.awayFourthDownConversionSuccess =
                calculateFourthDownConversionSuccess(
                    play, stats.awayFourthDownConversionSuccess ?: 0,
                )
            stats.awayFourthDownConversionAttempts =
                calculateFourthDownConversionAttempts(
                    play, stats.awayFourthDownConversionAttempts ?: 0,
                )
            stats.awayFourthDownConversionPercentage =
                calculatePercentage(
                    stats.awayFourthDownConversionSuccess ?: 0, stats.awayFourthDownConversionAttempts ?: 0,
                )
            stats.awayLargestLead =
                calculateLargestLead(
                    game, stats.awayLargestLead ?: 0, TeamSide.AWAY,
                )
            stats.awayLargestDeficit =
                calculateLargestDeficit(
                    game, stats.awayLargestDeficit ?: 0, TeamSide.HOME,
                )
            stats.awayPassTouchdowns =
                calculatePassTouchdowns(
                    play, stats.awayPassTouchdowns ?: 0,
                )
            stats.awayRushTouchdowns =
                calculateRushTouchdowns(
                    play, stats.awayRushTouchdowns ?: 0,
                )
            stats.awayRedZoneAttempts =
                calculateRedZoneAttempts(
                    allPlays, TeamSide.AWAY,
                )
            stats.awayRedZoneSuccesses =
                calculateRedZoneSuccesses(
                    allPlays, TeamSide.AWAY,
                )
            stats.awayRedZoneSuccessPercentage =
                calculatePercentage(
                    stats.awayRedZoneSuccesses ?: 0, stats.awayRedZoneAttempts ?: 0,
                )
            stats.awayTurnoverDifferential =
                calculateTurnoverDifferential(
                    stats.awayTurnoversLost ?: 0, stats.awayTurnoversForced ?: 0,
                )
            stats.awayPickSixesThrown =
                calculatePickSixes(
                    play, stats.awayPickSixesThrown ?: 0,
                )
            stats.homePickSixesForced = stats.awayPickSixesThrown
            stats.awayFumbleReturnTdsCommitted =
                calculateFumbleReturnTds(
                    play, stats.awayFumbleReturnTdsCommitted ?: 0,
                )
            stats.homeFumbleReturnTdsForced = stats.awayFumbleReturnTdsCommitted
            stats.awaySafetiesCommitted =
                calculateSafeties(
                    play, stats.awaySafetiesCommitted ?: 0,
                )
            stats.homeSafetiesForced = stats.awaySafetiesCommitted
        }
        if (play.quarter == 1) {
            stats.q1HomeScore = calculateQuarterScore(play, stats.q1HomeScore ?: 0, TeamSide.HOME)
            stats.q1AwayScore = calculateQuarterScore(play, stats.q1AwayScore ?: 0, TeamSide.AWAY)
        }
        if (play.quarter == 2) {
            stats.q2HomeScore = calculateQuarterScore(play, stats.q2HomeScore ?: 0, TeamSide.HOME)
            stats.q2AwayScore = calculateQuarterScore(play, stats.q2AwayScore ?: 0, TeamSide.AWAY)
        }
        if (play.quarter == 3) {
            stats.q3HomeScore = calculateQuarterScore(play, stats.q3HomeScore ?: 0, TeamSide.HOME)
            stats.q3AwayScore = calculateQuarterScore(play, stats.q3AwayScore ?: 0, TeamSide.AWAY)
        }
        if (play.quarter == 4) {
            stats.q4HomeScore = calculateQuarterScore(play, stats.q4HomeScore ?: 0, TeamSide.HOME)
            stats.q4AwayScore = calculateQuarterScore(play, stats.q4AwayScore ?: 0, TeamSide.AWAY)
        }
        if (play.quarter == 5) {
            stats.otHomeScore = calculateQuarterScore(play, stats.otHomeScore ?: 0, TeamSide.HOME)
            stats.otAwayScore = calculateQuarterScore(play, stats.otAwayScore ?: 0, TeamSide.AWAY)
        }

        stats.averageDiff = calculateAverageDiff(allPlays)
        stats.statsUpdated = true

        gameStatsRepository.save(stats)
        return stats
    }

    private fun calculatePassAttempts(
        play: Play,
        currentPassAttempts: Int,
    ): Int {
        // Don't count sacks as pass attempts
        if (play.playCall == PlayCall.PASS && (
                play.result != Scenario.LOSS_OF_10_YARDS ||
                    play.result != Scenario.LOSS_OF_5_YARDS ||
                    play.result != Scenario.LOSS_OF_3_YARDS ||
                    play.result != Scenario.LOSS_OF_1_YARD
            )
        ) {
            return currentPassAttempts + 1
        }
        if (play.playCall == PlayCall.SPIKE) {
            return currentPassAttempts + 1
        }
        return currentPassAttempts
    }

    private fun calculatePassCompletions(
        play: Play,
        currentPassCompletions: Int,
    ): Int {
        if (play.playCall == PlayCall.PASS && (
                play.result != Scenario.INCOMPLETE ||
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
                play.result != Scenario.LOSS_OF_10_YARDS ||
                    play.result != Scenario.LOSS_OF_5_YARDS ||
                    play.result != Scenario.LOSS_OF_3_YARDS ||
                    play.result != Scenario.LOSS_OF_1_YARD
            )
        ) {
            return currentPassYards
        }
        if (play.playCall == PlayCall.PASS) {
            return currentPassYards + (play.yards ?: 0)
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

        val yardsToGo = play.yardsToGo ?: 100
        val yardsGained = play.yards ?: 0
        val down = play.down ?: 1

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
                play.result != Scenario.LOSS_OF_10_YARDS ||
                    play.result != Scenario.LOSS_OF_5_YARDS ||
                    play.result != Scenario.LOSS_OF_3_YARDS ||
                    play.result != Scenario.LOSS_OF_1_YARD
            )
        ) {
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
                    play.result != Scenario.SAFETY
            )
        ) {
            return currentRushYards + (play.yards ?: 0)
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
                ((play.ballLocation ?: 0) + 17) > currentLongestFieldGoal
            )
        ) {
            return (play.ballLocation ?: 0) + 17
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
            val puntDistance = play.result?.description?.substringBefore(" YARD PUNT")?.toInt()
            if ((puntDistance ?: 0) > currentLongestPunt) {
                return puntDistance ?: 0
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
        }.sumOf { (it.playTime ?: 0) + (it.runoffTime ?: 0) }
    }

    private fun calculateTouchdowns(
        allPlays: List<Play>,
        possession: TeamSide,
    ): Int {
        val offensiveTouchdowns =
            allPlays.filter {
                it.possession == possession &&
                    (
                        it.actualResult == ActualResult.TOUCHDOWN ||
                            it.actualResult == ActualResult.KICKING_TEAM_TOUCHDOWN ||
                            it.actualResult == ActualResult.PUNT_TEAM_TOUCHDOWN
                    )
            }.count()
        val defensiveTouchdowns =
            allPlays.filter {
                it.possession != possession &&
                    (
                        it.actualResult == ActualResult.TURNOVER_TOUCHDOWN ||
                            it.actualResult == ActualResult.RETURN_TOUCHDOWN ||
                            it.actualResult == ActualResult.PUNT_RETURN_TOUCHDOWN ||
                            it.actualResult == ActualResult.KICK_SIX
                    )
            }.count()
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
            }.map { it.difference ?: 0 }.average()
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
            }.map { it.difference ?: 0 }.average()
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
            }.map { it.difference ?: 0 }.average()
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
            }.map { it.difference ?: 0 }.average()
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
            }.map { it.yards ?: 0 }.average()
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
        if (play.down == 3 && (play.yards ?: 0) > (play.yardsToGo ?: 0)) {
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
        if (play.down == 4 && (play.yards ?: 0) > (play.yardsToGo ?: 0)) {
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
            if ((game.homeScore ?: 0) - (game.awayScore ?: 0) > currentLargestLead) {
                return (game.homeScore ?: 0) - (game.awayScore ?: 0)
            }
        } else if (possession == TeamSide.AWAY) {
            if ((game.awayScore ?: 0) - (game.homeScore ?: 0) > currentLargestLead) {
                return (game.awayScore ?: 0) - (game.homeScore ?: 0)
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
            if ((game.awayScore ?: 0) - (game.homeScore ?: 0) > currentLargestDeficit) {
                return (game.awayScore ?: 0) - (game.homeScore ?: 0)
            }
        } else if (possession == TeamSide.AWAY) {
            if ((game.homeScore ?: 0) - (game.awayScore ?: 0) > currentLargestDeficit) {
                return (game.homeScore ?: 0) - (game.awayScore ?: 0)
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
                (it.ballLocation ?: 0) >= 80
        }
    }

    private fun calculateRedZoneSuccesses(
        allPlays: List<Play>,
        possession: TeamSide,
    ): Int {
        return allPlays.count {
            it.possession == possession &&
                (it.ballLocation ?: 0) >= 80 &&
                it.actualResult == ActualResult.TOUCHDOWN
        }
    }

    private fun calculateAverageDiff(allPlays: List<Play>): Double {
        val average = allPlays.map { it.difference ?: 0 }.average()
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
