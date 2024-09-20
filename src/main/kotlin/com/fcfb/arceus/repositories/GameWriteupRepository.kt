package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.GameWriteup
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameWriteupRepository : CrudRepository<GameWriteup?, Int?> {
    @Query(value = "SELECT * FROM game_writeup WHERE scenario = ? && pass_or_run = ?", nativeQuery = true)
    fun findByScenario(
        scenario: String,
        passOrRun: String?,
    ): GameWriteup?
}
