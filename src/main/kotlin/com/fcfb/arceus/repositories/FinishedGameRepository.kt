package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.FinishedGame
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FinishedGameRepository : CrudRepository<FinishedGame?, Int?>
