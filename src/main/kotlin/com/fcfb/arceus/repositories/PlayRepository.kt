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

    @Query(value = "SELECT * FROM play WHERE game_id = ? AND play_id < ? ORDER BY play_id DESC LIMIT 1", nativeQuery = true)
    fun findPreviousPlay(
        gameId: Int,
        playId: Int,
    ): Play

    @Query(value = "SELECT * FROM play WHERE game_id = ? ORDER BY play_id DESC", nativeQuery = true)
    fun getAllPlaysByGameId(gameId: Int): List<Play>

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM play WHERE game_id =?", nativeQuery = true)
    fun deleteAllPlaysByGameId(gameId: Int)
}
