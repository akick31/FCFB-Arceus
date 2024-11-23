package com.fcfb.arceus.converter

import com.fcfb.arceus.domain.User
import com.fcfb.arceus.dto.UserDTO
import org.springframework.stereotype.Component

@Component
class DTOConverter {
    fun convertToUserDTO(user: User): UserDTO {
        return UserDTO(
            id = user.id,
            username = user.username,
            coachName = user.coachName,
            discordTag = user.discordTag,
            discordId = user.discordId,
            position = user.position,
            redditUsername = user.redditUsername,
            role = user.role,
            team = user.team,
            delayOfGameInstances = user.delayOfGameInstances,
            wins = user.wins,
            losses = user.losses,
            winPercentage = user.winPercentage,
            conferenceWins = user.conferenceWins,
            conferenceLosses = user.conferenceLosses,
            conferenceChampionshipWins = user.conferenceChampionshipWins,
            conferenceChampionshipLosses = user.conferenceChampionshipLosses,
            bowlWins = user.bowlWins,
            bowlLosses = user.bowlLosses,
            playoffWins = user.playoffWins,
            playoffLosses = user.playoffLosses,
            nationalChampionshipWins = user.nationalChampionshipWins,
            nationalChampionshipLosses = user.nationalChampionshipLosses,
            offensivePlaybook = user.offensivePlaybook,
            defensivePlaybook = user.defensivePlaybook,
        )
    }
}
