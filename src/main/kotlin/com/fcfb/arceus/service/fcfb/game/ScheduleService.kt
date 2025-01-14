package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Schedule
import com.fcfb.arceus.repositories.ScheduleRepository
import com.fcfb.arceus.service.fcfb.SeasonService
import org.springframework.stereotype.Component

@Component
class ScheduleService(
    private val seasonService: SeasonService,
    private val scheduleRepository: ScheduleRepository,
) {
    /**
     * Get all games for a given season and week
     */
    fun getGamesToStartBySeasonAndWeek(
        season: Int,
        week: Int,
    ) = scheduleRepository.getGamesToStartBySeasonAndWeek(season, week)

    /**
     * Mark a game as started
     */
    fun markGameAsStarted(gameToSchedule: Schedule) {
        gameToSchedule.started = true
        scheduleRepository.save(gameToSchedule)
    }

    /**
     * Mark a game as finished
     */
    fun markGameAsFinished(game: Game) {
        val gameInSchedule = findGameInSchedule(game)
        gameInSchedule?.finished = true
        if (gameInSchedule != null) {
            scheduleRepository.save(gameInSchedule)
        }
    }

    /**
     * Find the game in the schedule
     */
    private fun findGameInSchedule(game: Game) =
        scheduleRepository.findGameInSchedule(
            game.homeTeam,
            game.season ?: 0,
            game.week ?: 0,
        )

    /**
     * Get an opponent team
     * @param team
     */
    fun getTeamOpponent(team: String) =
        scheduleRepository.getTeamOpponent(
            seasonService.getCurrentSeason().seasonNumber,
            seasonService.getCurrentWeek(),
            team,
        )

    /**
     * Get the schedule for a given season for a team
     * @param season
     * @param team
     */
    fun getScheduleBySeasonAndTeam(
        season: Int,
        team: String,
    ) = scheduleRepository.getScheduleBySeasonAndTeam(season, team)
}
