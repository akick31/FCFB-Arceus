package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.domain.Schedule
import com.fcfb.arceus.repositories.ScheduleRepository
import org.springframework.stereotype.Component

@Component
class ScheduleService(
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
}
