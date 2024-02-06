package com.fcfb.arceus.api.repositories

import com.fcfb.arceus.domain.GamePlaysEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GamePlaysRepository : CrudRepository<GamePlaysEntity?, Int?>