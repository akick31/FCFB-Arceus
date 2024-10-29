package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.GameStats
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameStatsRepository : CrudRepository<GameStats?, Int?> {
    @Query(value = "SELECT * FROM game_stats WHERE game_id =?", nativeQuery = true)
    fun findByGameId(gameId: Int): GameStats
}
