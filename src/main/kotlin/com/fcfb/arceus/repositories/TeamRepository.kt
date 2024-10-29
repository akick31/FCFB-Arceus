package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Team
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamRepository : CrudRepository<Team?, Int?> {
    fun getTeamByName(name: String?): Team

    @Query(value = "SELECT * FROM team", nativeQuery = true)
    fun getAllTeams(): Team
}
