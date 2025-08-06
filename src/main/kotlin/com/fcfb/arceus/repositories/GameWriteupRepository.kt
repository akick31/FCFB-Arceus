package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.GameWriteup
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameWriteupRepository : CrudRepository<GameWriteup, Int> {
    @Query(value = "SELECT * FROM game_writeup g WHERE g.scenario = ? && g.play_call = ?", nativeQuery = true)
    fun findByScenario(
        scenario: String,
        playCall: String?,
    ): List<GameWriteup>

    fun findByScenario(scenario: String): List<GameWriteup>

    fun findByPlayCall(playCall: String): List<GameWriteup>

    fun findByMessage(message: String): List<GameWriteup>
}
