package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Game
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameRepository : CrudRepository<Game?, Int?> {
    @Query(value = "SELECT * FROM game WHERE game_id =?", nativeQuery = true)
    fun getGameById(gameId: Int): Game

    @Query(value = "SELECT * FROM game WHERE JSON_CONTAINS(request_message_id, ?, '\$')", nativeQuery = true)
    fun getGameByRequestMessageId(requestMessageId: String): Game?

    @Query(value = "SELECT * FROM game WHERE home_platform_id = ?", nativeQuery = true)
    fun getGameByHomePlatformId(homePlatformId: ULong): Game?

    @Query(value = "SELECT * FROM game WHERE away_platform_id = ?", nativeQuery = true)
    fun getGameByAwayPlatformId(awayPlatformId: ULong): Game?

    @Query(
        value =
            "SELECT * FROM game WHERE STR_TO_DATE(game_timer, '%m/%d/%Y %H:%i:%s') < NOW() " +
                "AND game_status != 'FINAL' AND game_status != 'PREGAME'",
        nativeQuery = true,
    )
    fun findExpiredTimers(): List<Game>
}
