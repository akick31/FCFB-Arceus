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
            wins = user.wins ?: 0,
            losses = user.losses ?: 0,
            winPercentage = user.winPercentage ?: 0.0,
            offensivePlaybook = user.offensivePlaybook,
            defensivePlaybook = user.defensivePlaybook,
        )
    }
}
