package com.fcfb.arceus.service.team

import com.fcfb.arceus.domain.Team
import com.fcfb.arceus.repositories.TeamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class TeamService {

    @Autowired
    var teamsRepository: TeamRepository? = null

    private var emptyHeaders: HttpHeaders = HttpHeaders()

    fun getTeamById(id: Int): ResponseEntity<Team> {
        val teamData: Optional<Team?> = teamsRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(teamData.get(), HttpStatus.OK)
    }

    fun getAllTeams(): ResponseEntity<List<Team>> {
        val teamData: Iterable<Team?> = teamsRepository?.findAll() ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamData.iterator().hasNext()) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(teamData.filterNotNull(), HttpStatus.OK)
    }

    fun getTeamByName(name: String?): ResponseEntity<Team> {
        val teamData = teamsRepository?.findByName(name) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        return ResponseEntity(teamData, HttpStatus.OK)
    }

    fun createTeam(team: Team): ResponseEntity<Team> {
        return try {
            val newTeam: Team? = teamsRepository?.save(
                Team(
                    team.logo,
                    team.coachUsername,
                    team.coachName,
                    team.coachDiscordTag,
                    team.coachDiscordId,
                    0,
                    team.name,
                    0,
                    team.abbreviation,
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

    fun updateTeam(name: String?, team: Team): ResponseEntity<Team> {
        val existingTeam = teamsRepository?.findByName(name) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
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
