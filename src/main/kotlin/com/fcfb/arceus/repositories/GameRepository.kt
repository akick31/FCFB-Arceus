package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Game
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameRepository : CrudRepository<Game?, Int?> {
    @Query(value = "SELECT * FROM game WHERE home_platform = ? && home_platform_id = ?", nativeQuery = true)
    fun findByHomePlatformId(
        platform: String?,
        platformId: String?,
    ): Game?

    @Query(value = "SELECT * FROM game WHERE away_platform = ? && away_platform_id = ?", nativeQuery = true)
    fun findByAwayPlatformId(
        platform: String?,
        platformId: String?,
    ): Game?

    @Query(value = "SELECT * FROM game WHERE home_coach_discord_id1 = ?", nativeQuery = true)
    fun findByHomeCoachDiscordId1(homeCoachDiscordId: String?): Game?

    @Query(value = "SELECT * FROM game WHERE home_coach_discord_id2 = ?", nativeQuery = true)
    fun findByHomeCoachDiscordId2(homeCoachDiscordId: String?): Game?

    @Query(value = "SELECT * FROM game WHERE away_coach_discord_id1 = ?", nativeQuery = true)
    fun findByAwayCoachDiscordId1(awayCoachDiscordId: String?): Game?

    @Query(value = "SELECT * FROM game WHERE away_coach_discord_id2 = ?", nativeQuery = true)
    fun findByAwayCoachDiscordId2(awayCoachDiscordId: String?): Game?

    @Query(value = "SELECT * FROM game WHERE game_id =?", nativeQuery = true)
    fun findByGameId(gameId: Int): Game?
}
