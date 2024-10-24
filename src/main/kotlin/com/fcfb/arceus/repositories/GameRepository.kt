package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Game
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameRepository : CrudRepository<Game?, Int?> {
    @Query(value = "SELECT * FROM game WHERE home_platform = ? && home_platform_id = ? AND game_status != 'FINAL'", nativeQuery = true)
    fun findOngoingGameByHomePlatformId(
        platform: String?,
        platformId: String?,
    ): Game?

    @Query(value = "SELECT * FROM game WHERE away_platform = ? && away_platform_id = ? AND game_status != 'FINAL'", nativeQuery = true)
    fun findOngoingGameByAwayPlatformId(
        platform: String?,
        platformId: String?,
    ): Game?

    @Query(value = "SELECT * FROM game WHERE home_coach_discord_id1 = ? AND game_status != 'FINAL'", nativeQuery = true)
    fun findOngoingGameByHomeCoachDiscordId1(homeCoachDiscordId: String?): Game?

    @Query(value = "SELECT * FROM game WHERE home_coach_discord_id2 = ? AND game_status != 'FINAL'", nativeQuery = true)
    fun findOngoingGameByHomeCoachDiscordId2(homeCoachDiscordId: String?): Game?

    @Query(value = "SELECT * FROM game WHERE away_coach_discord_id1 = ? AND game_status != 'FINAL'", nativeQuery = true)
    fun findOngoingGameByAwayCoachDiscordId1(awayCoachDiscordId: String?): Game?

    @Query(value = "SELECT * FROM game WHERE away_coach_discord_id2 = ? AND game_status != 'FINAL'", nativeQuery = true)
    fun findOngoingGameByAwayCoachDiscordId2(awayCoachDiscordId: String?): Game?

    @Query(value = "SELECT * FROM game WHERE game_id =?", nativeQuery = true)
    fun findByGameId(gameId: Int): Game?
}
