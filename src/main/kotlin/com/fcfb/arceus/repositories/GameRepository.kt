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
    fun getGameByRequestMessageId(requestMessageId: String): Game
}
