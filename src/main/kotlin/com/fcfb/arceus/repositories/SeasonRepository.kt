package com.fcfb.arceus.repositories

import com.fcfb.arceus.model.Season
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SeasonRepository : CrudRepository<Season, Int> {
    @Query(value = "SELECT * FROM season WHERE current_season = true", nativeQuery = true)
    fun getCurrentSeason(): Season?

    @Query(value = "SELECT * FROM season WHERE current_season = false ORDER BY season_number DESC LIMIT 1", nativeQuery = true)
    fun getPreviousSeason(): Season?

    fun findBySeasonNumber(seasonNumber: Int): Season?

    fun findByCurrentSeason(currentSeason: Boolean): List<Season>
}
