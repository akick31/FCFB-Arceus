package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Team
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamRepository : CrudRepository<Team?, Int?> {
    @Query(value = "SELECT * FROM team WHERE active = true", nativeQuery = true)
    fun getAllActiveTeams(): List<Team>

    @Query(value = "SELECT * FROM team WHERE conference = :conference", nativeQuery = true)
    fun getTeamsInConference(conference: String): List<Team>?

    @Query("SELECT * FROM team t WHERE LOWER(t.name) = LOWER(:name)", nativeQuery = true)
    fun getTeamByName(name: String?): Team

    @Query(
        value =
        "SELECT t.name " +
                "FROM team t " +
                "WHERE t.active = true " +  // Ensure the team is active
                "AND NOT EXISTS (" +
                "SELECT 1 " +
                "FROM user u " +
                "WHERE u.team = t.name)",
        nativeQuery = true,
    )
    fun getOpenTeams(): List<String>?

    @Query(value = "SELECT * FROM team", nativeQuery = true)
    fun getAllTeams(): Team
}
