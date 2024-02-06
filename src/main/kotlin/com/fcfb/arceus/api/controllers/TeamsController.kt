package com.fcfb.arceus.api.controllers

import com.fcfb.arceus.domain.TeamsEntity
import com.fcfb.arceus.api.repositories.TeamsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/arceus/teams")
class TeamsController {
    @Autowired
    var teamsRepository: TeamsRepository? = null

    /**
     * Get a team by id
     * @param id
     * @return
     */
    @GetMapping("/id")
    fun getTeamById(
        @RequestParam id: Int
    ): ResponseEntity<String> {
        val teamData: Optional<TeamsEntity?> = teamsRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)

        return ResponseEntity(teamData.get().toString(), HttpStatus.OK)
    }
    
    @GetMapping("")
    fun getAllTeams(): ResponseEntity<String> {
        val teamData: Iterable<TeamsEntity?> =
            teamsRepository?.findAll() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        return if (teamData.iterator().hasNext()) {
            val teamsString = teamData.joinToString("\n") { team -> team.toString() }
            ResponseEntity(teamsString, HttpStatus.OK)
        } else {
            ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Get a team by name
     * @param name
     * @return
     */
    @GetMapping("/name")
    fun getTeamByName(
        @RequestParam name: String?
    ): ResponseEntity<String> {
        val teamData: Optional<TeamsEntity?> = teamsRepository?.findByName(name) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)

        return ResponseEntity(teamData.get().toString(), HttpStatus.OK)
    }

    /**
     * Create a new team
     * @param team
     * @return
     */
    @PostMapping("")
    fun createTeam(
        @RequestBody team: TeamsEntity
    ): ResponseEntity<String> {
        return try {
            val newTeam: TeamsEntity? = teamsRepository?.save(
                TeamsEntity(
                    team.logo,
                    team.coach,
                    0,
                    team.name,
                    0,
                    team.primaryColor,
                    team.secondaryColor,
                    team.subdivision,
                    team.offensivePlaybook,
                    team.defensivePlaybook,
                    team.conference,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0
                ) ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST)
            )
            ResponseEntity(newTeam.toString(), HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * Update a team
     * @param name
     * @param team
     * @return
     */
    @PutMapping("/{name}")
    fun updateTeam(
        @PathVariable("name") name: String?,
        @RequestBody team: TeamsEntity
    ): ResponseEntity<String> {
        val teamData: Optional<TeamsEntity?> = teamsRepository?.findByName(name) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)

        val existingTeam: TeamsEntity = teamData.get()
        existingTeam.apply {
            this.name = team.name
            coach = team.coach
            subdivision = team.subdivision
            conference = team.conference
            primaryColor = team.primaryColor
            secondaryColor = team.secondaryColor
            coachesPollRanking = team.coachesPollRanking
            playoffCommitteeRanking = team.playoffCommitteeRanking
            offensivePlaybook = team.offensivePlaybook
            defensivePlaybook = team.defensivePlaybook
            currentWins = team.currentWins
            currentLosses = team.currentLosses
            currentConferenceWins = team.currentConferenceWins
            currentConferenceLosses = team.currentConferenceLosses
            overallWins = team.overallWins
            overallLosses = team.overallLosses
            overallConferenceWins = team.overallConferenceWins
            overallConferenceLosses = team.overallConferenceLosses
        }
        teamsRepository?.save(existingTeam)
        return ResponseEntity(existingTeam.toString(), HttpStatus.OK)
    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    fun deleteTeam(
        @PathVariable("id") id: Int
    ): ResponseEntity<HttpStatus> {
        teamsRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        teamsRepository?.deleteById(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
