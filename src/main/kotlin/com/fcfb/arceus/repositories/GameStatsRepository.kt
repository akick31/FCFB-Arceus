package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.GameStats
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface GameStatsRepository : CrudRepository<GameStats?, Int?> {
    @Query(value = "SELECT * FROM game_stats WHERE game_id = ? and team = ?", nativeQuery = true)
    fun getGameStatsByIdAndTeam(
        gameId: Int,
        team: String,
    ): GameStats

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM game_stats WHERE game_id = ?", nativeQuery = true)
    fun deleteByGameId(gameId: Int)
}
