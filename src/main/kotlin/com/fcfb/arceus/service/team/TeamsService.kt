package com.fcfb.arceus.service.team

import com.fcfb.arceus.domain.TeamsEntity
import com.fcfb.arceus.repositories.TeamsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class TeamsService {

    @Autowired
    var teamsRepository: TeamsRepository? = null

    private var emptyHeaders: HttpHeaders = HttpHeaders()

    fun getTeamById(id: Int): ResponseEntity<TeamsEntity> {
        val teamData: Optional<TeamsEntity?> = teamsRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(teamData.get(), HttpStatus.OK)
    }

    fun getAllTeams(): ResponseEntity<List<TeamsEntity>> {
        val teamData: Iterable<TeamsEntity?> = teamsRepository?.findAll() ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamData.iterator().hasNext()) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(teamData.filterNotNull(), HttpStatus.OK)
    }

    fun getTeamByName(name: String?): ResponseEntity<TeamsEntity> {
        val teamData: Optional<TeamsEntity> = teamsRepository?.findByName(name) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(teamData.get(), HttpStatus.OK)
    }

    fun createTeam(team: TeamsEntity): ResponseEntity<TeamsEntity> {
        return try {
            val newTeam: TeamsEntity? = teamsRepository?.save(
                TeamsEntity(
                    team.logo,
                    team.coachUsername,
                    team.coachName,
                    team.coachDiscordTag,
                    team.coachDiscordId,
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

    fun updateTeam(name: String?, team: TeamsEntity): ResponseEntity<TeamsEntity> {
        val teamData = teamsRepository?.findByName(name) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        val existingTeam: TeamsEntity = teamData.get()
        existingTeam.apply {
            this.name = team.name
            coachUsername = team.coachUsername
            coachName = team.coachName
            coachDiscordTag = team.coachDiscordTag
            coachDiscordId = team.coachDiscordId
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

    fun deleteTeam(id: Int): ResponseEntity<HttpStatus> {
        teamsRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamsRepository?.findById(id)!!.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        teamsRepository?.deleteById(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
