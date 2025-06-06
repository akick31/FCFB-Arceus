package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Team
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamRepository : CrudRepository<Team?, Int?> {
    @Query(
        value = """
            SELECT * 
            FROM team 
            WHERE active = true
        """,
        nativeQuery = true,
    )
    fun getAllActiveTeams(): List<Team>

    @Query(
        value = """
            SELECT * 
            FROM team 
            WHERE conference = :conference
        """,
        nativeQuery = true,
    )
    fun getTeamsInConference(conference: String): List<Team>?

    @Query(
        value = """
            SELECT * 
            FROM team t 
            WHERE LOWER(t.name) = LOWER(:name)
        """,
        nativeQuery = true,
    )
    fun getTeamByName(name: String?): Team?

    @Query(
        value = """
            SELECT t.name 
            FROM team t 
            WHERE t.active = true 
            AND t.is_taken = false
        """,
        nativeQuery = true,
    )
    fun getOpenTeams(): List<String>?

    @Query(
        value = """
            SELECT CASE 
                WHEN COUNT(*) > 0 THEN 1 
                ELSE 0 
            END 
            FROM team 
            WHERE active = true 
            AND playoff_committee_ranking IS NOT NULL
        """,
        nativeQuery = true,
    )
    fun usePlayoffRanking(): Int

    @Query(
        value = """
            SELECT playoff_committee_ranking 
            FROM team 
            WHERE id = :id
        """,
        nativeQuery = true,
    )
    fun getPlayoffRankingById(id: Int): Int?

    @Query(
        value = """
            SELECT coaches_poll_ranking 
            FROM team 
            WHERE id = :id
        """,
        nativeQuery = true,
    )
    fun getCoachesPollRankingById(id: Int): Int?

    @Query(
        value = """
            SELECT * 
            FROM team
        """,
        nativeQuery = true,
    )
    fun getAllTeams(): Team?
}
