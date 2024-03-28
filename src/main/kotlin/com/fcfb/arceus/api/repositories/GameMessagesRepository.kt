package com.fcfb.arceus.api.repositories

import com.fcfb.arceus.domain.GameMessagesEntity
import com.fcfb.arceus.domain.GamePlaysEntity
import com.fcfb.arceus.models.GameScenario
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameMessagesRepository : CrudRepository<GameMessagesEntity?, Int?> {
    fun findByScenario(scenario: GameScenario): GameMessagesEntity?
}