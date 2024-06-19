package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.FinishedGamesEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FinishedGamesRepository : CrudRepository<FinishedGamesEntity?, Int?>
