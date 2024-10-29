package com.fcfb.arceus.service.fcfb

import com.fcfb.arceus.domain.Team
import com.fcfb.arceus.domain.User.CoachPosition
import com.fcfb.arceus.domain.User.CoachPosition.DEFENSIVE_COORDINATOR
import com.fcfb.arceus.domain.User.CoachPosition.HEAD_COACH
import com.fcfb.arceus.domain.User.CoachPosition.OFFENSIVE_COORDINATOR
import com.fcfb.arceus.domain.User.CoachPosition.RETIRED
import com.fcfb.arceus.models.NoTeamFoundException
import com.fcfb.arceus.repositories.TeamRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class TeamService(
    private val teamRepository: TeamRepository,
    private val userService: UserService,
) {
    /**
     * Get a team by its ID
     * @param id
     */
    fun getTeamById(id: Int): Team {
        val teamData = teamRepository.findById(id)
        if (!teamData.isPresent) {
            throw NoTeamFoundException()
        }
        return teamData.get()
    }

    /**
     * Get all teams
     */
    fun getAllTeams(): List<Team> {
        val teamData = teamRepository.findAll()
        if (!teamData.iterator().hasNext()) {
            throw NoTeamFoundException()
        }
        return teamData.filterNotNull()
    }

    /**
     * Get a team by its name
     * @param name
     */
    fun getTeamByName(name: String?) = teamRepository.getTeamByName(name)

    /**
     * Create a new team
     * @param team
     */
    fun createTeam(team: Team): Team {
        try {
            val newTeam =
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
            return newTeam
        } catch (e: Exception) {
            throw e
        }
    }

    fun updateTeam(
        name: String?,
        team: Team,
    ): Team {
        val existingTeam = getTeamByName(name)

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
        return existingTeam
    }

    /**
     * Hire a coach for a team
     * @param name
     * @param discordId
     * @param coachPosition
     */
    suspend fun hireCoach(
        name: String?,
        discordId: String,
        coachPosition: CoachPosition,
    ): Team {
        val updatedName = name?.replace("_", " ")
        val existingTeam = getTeamByName(updatedName)
        val user = userService.getUserByDiscordId(discordId)
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
            saveTeam(existingTeam)
            userService.updateUser(user)
        }
        return existingTeam
    }

    /**
     * Save a team
     * @param team
     */
    fun saveTeam(team: Team) = teamRepository.save(team)

    /**
     * Delete a team
     * @param id
     */
    fun deleteTeam(id: Int): HttpStatus {
        teamRepository.findById(id) ?: return HttpStatus.NOT_FOUND
        if (!teamRepository.findById(id).isPresent) {
            return HttpStatus.NOT_FOUND
        }
        teamRepository.deleteById(id)
        return HttpStatus.OK
    }
}
