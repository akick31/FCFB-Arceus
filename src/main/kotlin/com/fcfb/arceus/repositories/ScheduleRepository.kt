package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Schedule
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ScheduleRepository : CrudRepository<Schedule?, Int?> {
    @Query(value = "SELECT * FROM schedule WHERE season = ? AND week = ? AND started = false", nativeQuery = true)
    fun getGamesToStartBySeasonAndWeek(
        season: Int,
        week: Int,
    ): List<Schedule>?
}
