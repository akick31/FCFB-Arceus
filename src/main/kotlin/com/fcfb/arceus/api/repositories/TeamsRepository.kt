package com.fcfb.arceus.api.repositories

import com.fcfb.arceus.domain.TeamsEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TeamsRepository : CrudRepository<TeamsEntity?, Int?> {
    @Query(value = "SELECT * FROM teams WHERE name =?", nativeQuery = true)
    fun findByName(name: String?): Optional<TeamsEntity?>?

    @Query(value = "SELECT * FROM teams", nativeQuery = true)
    fun findAllTeams(): Optional<TeamsEntity?>?
}