package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Game
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameRepository : CrudRepository<Game?, Int?> {
    @Query(value = "SELECT * FROM game WHERE home_platform = ? && home_platform_id = ?", nativeQuery = true)
    fun findByHomePlatformId(platform: String?, platformId: String?): Game?

    @Query(value = "SELECT * FROM game WHERE away_platform = ? && away_platform_id = ?", nativeQuery = true)
    fun findByAwayPlatformId(platform: String?, platformId: String?): Game?

    @Query(value = "SELECT * FROM game WHERE game_id =?", nativeQuery = true)
    fun findByGameId(gameId: Int): Game?
}
