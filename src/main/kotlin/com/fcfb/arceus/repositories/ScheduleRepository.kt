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

    @Query(
        value = """
        SELECT CASE 
            WHEN home_team = :team THEN away_team 
            WHEN away_team = :team THEN home_team 
            ELSE NULL 
        END AS opponent
        FROM schedule 
        WHERE season = :season AND week = :week AND (home_team = :team OR away_team = :team)
    """,
        nativeQuery = true,
    )
    fun getTeamOpponent(
        season: Int,
        week: Int,
        team: String,
    ): String?

    @Query(value = "SELECT * FROM schedule WHERE season = :season AND (home_team = :team OR away_team = :team)", nativeQuery = true)
    fun getScheduleBySeasonAndTeam(
        season: Int,
        team: String,
    ): List<Schedule>?
}