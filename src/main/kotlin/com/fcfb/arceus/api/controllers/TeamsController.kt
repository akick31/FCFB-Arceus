package com.fcfb.arceus.api.controllers

import com.fcfb.arceus.domain.TeamsEntity
import com.fcfb.arceus.api.repositories.TeamsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/teams")
class TeamsController {
    @Autowired
    var teamsRepository: TeamsRepository? = null

    private var emptyHeaders: HttpHeaders = HttpHeaders()

    /**
     * Get a team by id
     * @param id
     * @return
     */
    @GetMapping("/id")
    fun getTeamById(
        @RequestParam id: Int
    ): ResponseEntity<TeamsEntity> {
        val teamData: Optional<TeamsEntity?> = teamsRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(teamData.get(), HttpStatus.OK)
    }
    
    @GetMapping("")
    fun getAllTeams(): ResponseEntity<List<TeamsEntity>> {
        val teamData: Iterable<TeamsEntity?> = teamsRepository?.findAll() ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamData.iterator().hasNext()) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(teamData.filterNotNull(), HttpStatus.OK)
    }

    /**
     * Get a team by name
     * @param name
     * @return
     */
    @GetMapping("/name")
    fun getTeamByName(
        @RequestParam name: String?
    ): ResponseEntity<TeamsEntity> {
        val teamData: Optional<TeamsEntity?> = teamsRepository?.findByName(name) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(teamData.get(), HttpStatus.OK)
    }

    /**
     * Create a new team
     * @param team
     * @return
     */
    @PostMapping("")
    fun createTeam(
        @RequestBody team: TeamsEntity
    ): ResponseEntity<TeamsEntity> {
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
                )
            )
            ResponseEntity(newTeam, HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
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
    ): ResponseEntity<TeamsEntity> {
        val teamData: Optional<TeamsEntity?> = teamsRepository?.findByName(name) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
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
        return ResponseEntity(existingTeam, HttpStatus.OK)
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
        teamsRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamsRepository?.findById(id)!!.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        teamsRepository?.deleteById(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
