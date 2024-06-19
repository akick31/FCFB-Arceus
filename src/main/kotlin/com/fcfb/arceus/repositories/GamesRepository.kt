package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.GamesEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface GamesRepository : CrudRepository<GamesEntity?, Int?> {
    @Query(value = "SELECT * FROM games WHERE home_platform =? && home_platform_id =?", nativeQuery = true)
    fun findByHomePlatformId(platform: String?, platformId: String?): Optional<GamesEntity>

    @Query(value = "SELECT * FROM games WHERE away_platform =? && away_platform_id =?", nativeQuery = true)
    fun findByAwayPlatformId(platform: String?, platformId: String?): Optional<GamesEntity>

    @Query(value = "SELECT * FROM games WHERE game_id =?", nativeQuery = true)
    fun findByGameId(gameId: Int): Optional<GamesEntity>
}
