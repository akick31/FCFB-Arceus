package com.fcfb.arceus.api.repositories

import com.fcfb.arceus.domain.OngoingGamesEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OngoingGamesRepository : CrudRepository<OngoingGamesEntity?, Int?> {
    @Query(value = "SELECT * FROM ongoing_games WHERE home_platform =? && home_platform_id =?", nativeQuery = true)
    fun findByHomePlatformId(platform: String?, platformId: String?): Optional<OngoingGamesEntity?>?

    @Query(value = "SELECT * FROM ongoing_games WHERE away_platform =? && away_platform_id =?", nativeQuery = true)
    fun findByAwayPlatformId(platform: String?, platformId: String?): Optional<OngoingGamesEntity?>?
}