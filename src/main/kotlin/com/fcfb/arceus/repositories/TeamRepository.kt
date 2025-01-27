package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Team
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamRepository : CrudRepository<Team?, Int?> {
    @Query(value = "SELECT * FROM team WHERE conference = :conference", nativeQuery = true)
    fun getTeamsInConference(conference: String): List<Team>?

    fun getTeamByName(name: String?): Team

    @Query(
        value =
            "SELECT t.name " +
                "FROM team t " +
                "WHERE NOT EXISTS (" +
                "SELECT 1 " +
                "FROM user u " +
                "WHERE u.team = t.name)",
        nativeQuery = true,
    )
    fun getOpenTeams(): List<String>?

    @Query(value = "SELECT * FROM team", nativeQuery = true)
    fun getAllTeams(): Team
}
