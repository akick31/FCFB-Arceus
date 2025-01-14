package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Game
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

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

    @Query(value = "SELECT * FROM game", nativeQuery = true)
    fun getAllGames(): List<Game>

    @Query(value = "SELECT * FROM game WHERE game_status != 'FINAL' AND game_type != 'SCRIMMAGE'", nativeQuery = true)
    fun getAllOngoingGames(): List<Game>

    @Query(value = "SELECT * FROM game WHERE game_status = 'FINAL' AND game_type != 'SCRIMMAGE'", nativeQuery = true)
    fun getAllPastGames(): List<Game>

    @Query(value = "SELECT * FROM game WHERE game_status = 'FINAL' AND game_type = 'SCRIMMAGE'", nativeQuery = true)
    fun getAllPastScrimmageGames(): List<Game>

    @Query(value = "SELECT * FROM game WHERE game_status != 'FINAL' AND game_type = 'SCRIMMAGE'", nativeQuery = true)
    fun getAllOngoingScrimmageGames(): List<Game>

    @Query(
        value =
            "SELECT * FROM game " +
                "WHERE (home_team = :team OR away_team = :team) " +
                "AND season = :season " +
                "AND week = :week " +
                "AND game_type != 'SCRIMMAGE'",
        nativeQuery = true,
    )
    fun getGamesByTeamSeasonAndWeek(
        team: String,
        season: Int,
        week: Int,
    ): Game?

    @Query(
        value =
            "SELECT * FROM game " +
                "WHERE STR_TO_DATE(game_timer, '%m/%d/%Y %H:%i:%s') <= CONVERT_TZ(NOW(), 'UTC', 'America/New_York') " +
                "AND game_status != 'FINAL' " +
                "AND game_status != 'PREGAME'",
        nativeQuery = true,
    )
    fun findExpiredTimers(): List<Game>

    @Query(
        value =
            "SELECT * FROM game " +
                "WHERE STR_TO_DATE(game_timer, '%m/%d/%Y %H:%i:%s') " +
                "BETWEEN CONVERT_TZ(NOW(), 'UTC', 'America/New_York') " +
                "AND DATE_ADD(CONVERT_TZ(NOW(), 'UTC', 'America/New_York'), INTERVAL 6 HOUR) " +
                "AND game_status != 'FINAL' " +
                "AND game_status != 'PREGAME'" +
                "AND game_warned = False",
        nativeQuery = true,
    )
    fun findGamesToWarn(): List<Game>

    @Transactional
    @Modifying
    @Query(value = "UPDATE game SET game_warned = True WHERE game_id = ?", nativeQuery = true)
    fun updateGameAsWarned(gameId: Int)
}
