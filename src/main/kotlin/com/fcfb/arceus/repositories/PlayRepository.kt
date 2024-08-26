package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Play
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayRepository : CrudRepository<Play?, Int?> {
    @Query(value = "SELECT * FROM play WHERE play_id =?", nativeQuery = true)
    fun findByPlayId(playId: Int): Play?
}
