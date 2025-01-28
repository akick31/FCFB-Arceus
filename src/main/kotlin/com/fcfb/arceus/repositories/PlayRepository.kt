package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Play
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface PlayRepository : CrudRepository<Play?, Int?> {
    @Query(value = "SELECT * FROM play WHERE play_id =?", nativeQuery = true)
    fun getPlayById(playId: Int): Play

    @Query(value = "SELECT * FROM play WHERE game_id = ? ORDER BY play_id DESC", nativeQuery = true)
    fun getAllPlaysByGameId(gameId: Int): List<Play>

    @Query(
        value =
            "SELECT play.* " +
                "FROM play " +
                "JOIN game g ON play.game_id = g.game_id " +
                "WHERE (offensive_submitter = :discordTag OR defensive_submitter = :discordTag)" +
                " AND g.game_type != 'SCRIMMAGE' " +
                "ORDER BY play_id DESC;",
        nativeQuery = true,
    )
    fun getAllPlaysByDiscordTag(discordTag: String): List<Play>

    @Query(value = "SELECT * FROM play WHERE game_id = ? AND play_finished = false ORDER BY play_id DESC LIMIT 1", nativeQuery = true)
    fun getCurrentPlay(gameId: Int): Play?

    @Query(value = "SELECT * FROM play WHERE game_id = ? AND play_finished = true ORDER BY play_id DESC LIMIT 1", nativeQuery = true)
    fun getPreviousPlay(gameId: Int): Play?

    @Query(
        value = "SELECT COUNT(*) FROM play WHERE game_id = :gameId AND result = 'DELAY OF GAME ON HOME TEAM'",
        nativeQuery = true,
    )
    fun getHomeDelayOfGameInstances(gameId: Int): Int

    @Query(
        value = "SELECT COUNT(*) FROM play WHERE game_id = :gameId AND result = 'DELAY OF GAME ON AWAY TEAM'",
        nativeQuery = true,
    )
    fun getAwayDelayOfGameInstances(gameId: Int): Int

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM play WHERE game_id =?", nativeQuery = true)
    fun deleteAllPlaysByGameId(gameId: Int)
}
