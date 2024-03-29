package com.fcfb.arceus.api.repositories

import com.fcfb.arceus.domain.GameMessagesEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameMessagesRepository : CrudRepository<GameMessagesEntity?, Int?> {
    fun findByScenario(scenario: String): GameMessagesEntity?
}