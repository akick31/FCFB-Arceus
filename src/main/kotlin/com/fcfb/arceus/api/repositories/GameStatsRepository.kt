package com.fcfb.arceus.api.repositories

import com.fcfb.arceus.domain.GameStatsEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameStatsRepository : CrudRepository<GameStatsEntity?, Int?>