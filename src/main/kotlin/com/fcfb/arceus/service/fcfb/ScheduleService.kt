package com.fcfb.arceus.service.fcfb

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Schedule
import com.fcfb.arceus.repositories.ScheduleRepository
import com.fcfb.arceus.utils.Logger
import com.fcfb.arceus.utils.ScheduleNotFoundException
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val seasonService: SeasonService,
    private val scheduleRepository: ScheduleRepository,
) {
    /**
     * Get all games for a given season and week
     * @param season
     * @param week
     */
    fun getGamesToStartBySeasonAndWeek(
        season: Int,
        week: Int,
    ) = scheduleRepository.getGamesToStartBySeasonAndWeek(season, week)

    /**
     * Mark a game as started
     * @param gameToSchedule
     */
    fun markGameAsStarted(gameToSchedule: Schedule) {
        gameToSchedule.started = true
        scheduleRepository.save(gameToSchedule)
    }

    /**
     * Mark a manually started game as started
     * @param game
     */
    fun markManuallyStartedGameAsStarted(game: Game) {
        val gameInSchedule = findGameInSchedule(game)
        gameInSchedule.started = true
        scheduleRepository.save(gameInSchedule)
    }

    /**
     * Mark a game as finished
     * @param game
     */
    fun markGameAsFinished(game: Game) {
        try {
            val gameInSchedule = findGameInSchedule(game)
            gameInSchedule.finished = true
            scheduleRepository.save(gameInSchedule)
            if (checkIfWeekIsOver(game.season ?: 0, game.week ?: 0)) {
                seasonService.incrementWeek()
            }
        } catch (e: Exception) {
            Logger.error("Unable to mark game as finished", e)
        }
    }

    /**
     * Check if the current week is over
     * @param season
     * @param week
     */
    private fun checkIfWeekIsOver(
        season: Int,
        week: Int,
    ) = scheduleRepository.checkIfWeekIsOver(season, week) == 1

    /**
     * Find the game in the schedule
     */
    private fun findGameInSchedule(game: Game) =
        scheduleRepository.findGameInSchedule(
            game.homeTeam,
            game.awayTeam,
            game.season ?: 0,
            game.week ?: 0,
        ) ?: throw ScheduleNotFoundException("Game not found in schedule")

    /**
     * Get an opponent team
     * @param team
     */
    fun getTeamOpponent(team: String) =
        scheduleRepository.getTeamOpponent(
            seasonService.getCurrentSeason().seasonNumber,
            seasonService.getCurrentWeek(),
            team,
        ) ?: throw ScheduleNotFoundException("Opponent not found for $team")

    /**
     * Get the schedule for a given season for a team
     * @param season
     * @param team
     */
    fun getScheduleBySeasonAndTeam(
        season: Int,
        team: String,
    ) = scheduleRepository.getScheduleBySeasonAndTeam(season, team)
        ?: throw ScheduleNotFoundException("Schedule not found for $team")
}
