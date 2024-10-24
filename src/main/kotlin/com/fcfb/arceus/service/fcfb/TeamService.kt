package com.fcfb.arceus.service.fcfb

import com.fcfb.arceus.domain.Team
import com.fcfb.arceus.domain.User.CoachPosition
import com.fcfb.arceus.domain.User.CoachPosition.DEFENSIVE_COORDINATOR
import com.fcfb.arceus.domain.User.CoachPosition.HEAD_COACH
import com.fcfb.arceus.domain.User.CoachPosition.OFFENSIVE_COORDINATOR
import com.fcfb.arceus.domain.User.CoachPosition.RETIRED
import com.fcfb.arceus.repositories.TeamRepository
import com.fcfb.arceus.repositories.UserRepository
import com.fcfb.arceus.service.discord.DiscordService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class TeamService(
    var teamRepository: TeamRepository,
    var userRepository: UserRepository,
    private val discordService: DiscordService,
) {
    private var emptyHeaders: HttpHeaders = HttpHeaders()

    fun getTeamById(id: Int): ResponseEntity<Team> {
        val teamData: Optional<Team?> = teamRepository.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(teamData.get(), HttpStatus.OK)
    }

    fun getAllTeams(): ResponseEntity<List<Team>> {
        val teamData: Iterable<Team?> = teamRepository.findAll() ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamData.iterator().hasNext()) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(teamData.filterNotNull(), HttpStatus.OK)
    }

    fun getTeamByName(name: String?): ResponseEntity<Team> {
        val teamData = teamRepository.findByName(name) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        return ResponseEntity(teamData, HttpStatus.OK)
    }

    fun createTeam(team: Team): ResponseEntity<Team> {
        return try {
            val newTeam: Team? =
                teamRepository.save(
                    Team(
                        team.logo,
                        team.coachUsername1,
                        team.coachName1,
                        team.coachDiscordTag1,
                        team.coachDiscordId1,
                        team.coachUsername2,
                        team.coachName2,
                        team.coachDiscordTag2,
                        team.coachDiscordId2,
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
                        0,
                    ),
                )
            ResponseEntity(newTeam, HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }

    fun updateTeam(
        name: String?,
        team: Team,
    ): ResponseEntity<Team> {
        val existingTeam = teamRepository.findByName(name) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        existingTeam.apply {
            this.name = team.name
            coachUsername1 = team.coachUsername1
            coachName1 = team.coachName1
            coachDiscordTag1 = team.coachDiscordTag1
            coachDiscordId1 = team.coachDiscordId1
            coachUsername2 = team.coachUsername2
            coachName2 = team.coachName2
            coachDiscordTag2 = team.coachDiscordTag2
            coachDiscordId2 = team.coachDiscordId2
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
        teamRepository.save(existingTeam)
        return ResponseEntity(existingTeam, HttpStatus.OK)
    }

    suspend fun hireCoach(
        name: String?,
        discordId: String,
        coachPosition: CoachPosition,
    ): ResponseEntity<Team> {
        val updatedName = name?.replace("_", " ")
        val existingTeam =
            withContext(Dispatchers.IO) {
                teamRepository.findByName(updatedName)
            } ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        val user =
            withContext(Dispatchers.IO) {
                userRepository.findByDiscordId(discordId)
            } ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        user.team = existingTeam.name
        when (coachPosition) {
            HEAD_COACH -> {
                existingTeam.coachUsername1 = user.username
                existingTeam.coachName1 = user.coachName
                existingTeam.coachDiscordTag1 = user.discordTag
                existingTeam.coachDiscordId1 = discordId
                existingTeam.offensivePlaybook = user.offensivePlaybook
                existingTeam.defensivePlaybook = user.defensivePlaybook
            }
            OFFENSIVE_COORDINATOR -> {
                existingTeam.coachUsername1 = user.username
                existingTeam.coachName1 = user.coachName
                existingTeam.coachDiscordTag1 = user.discordTag
                existingTeam.coachDiscordId1 = discordId
                existingTeam.offensivePlaybook = user.offensivePlaybook
            }
            DEFENSIVE_COORDINATOR -> {
                existingTeam.coachUsername2 = user.username
                existingTeam.coachName2 = user.coachName
                existingTeam.coachDiscordTag2 = user.discordTag
                existingTeam.coachDiscordId2 = discordId
                existingTeam.defensivePlaybook = user.defensivePlaybook
            }
            RETIRED -> {}
        }
        withContext(Dispatchers.IO) {
            teamRepository.save(existingTeam)
            userRepository.save(user)
        }
        return ResponseEntity(existingTeam, HttpStatus.OK)
    }

    fun deleteTeam(id: Int): ResponseEntity<HttpStatus> {
        teamRepository.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!teamRepository.findById(id).isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        teamRepository.deleteById(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
