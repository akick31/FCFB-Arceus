package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.GameWriteup
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameWriteupRepository : CrudRepository<GameWriteup?, Int?> {
    fun findByScenario(scenario: String): GameWriteup?
}
