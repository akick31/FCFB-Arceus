package com.fcfb.arceus.dto

import com.fcfb.arceus.domain.Game.DefensivePlaybook
import com.fcfb.arceus.domain.Game.OffensivePlaybook
import com.fcfb.arceus.domain.User.CoachPosition
import com.fcfb.arceus.domain.User.Role

data class UserDTO(
    val id: Long?,
    val username: String,
    val coachName: String?,
    val discordTag: String,
    val discordId: String?,
    val position: CoachPosition?,
    val redditUsername: String?,
    val role: Role?,
    val team: String?,
    val wins: Int,
    val losses: Int,
    val winPercentage: Double,
    val offensivePlaybook: OffensivePlaybook?,
    val defensivePlaybook: DefensivePlaybook?,
)
