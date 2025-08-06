package com.fcfb.arceus.models.dto

import com.fcfb.arceus.domain.User.CoachPosition
import com.fcfb.arceus.domain.User.Role
import com.fcfb.arceus.domain.enums.DefensivePlaybook
import com.fcfb.arceus.domain.enums.OffensivePlaybook

data class UserDTO(
    val id: Long,
    var username: String,
    var coachName: String,
    var discordTag: String,
    var discordId: String?,
    var position: CoachPosition,
    var role: Role,
    var team: String?,
    var delayOfGameInstances: Int,
    var wins: Int,
    var losses: Int,
    var winPercentage: Double,
    var conferenceWins: Int,
    var conferenceLosses: Int,
    var conferenceChampionshipWins: Int,
    var conferenceChampionshipLosses: Int,
    var bowlWins: Int,
    var bowlLosses: Int,
    var playoffWins: Int,
    var playoffLosses: Int,
    var nationalChampionshipWins: Int,
    var nationalChampionshipLosses: Int,
    var offensivePlaybook: OffensivePlaybook,
    var defensivePlaybook: DefensivePlaybook,
    var averageResponseTime: Double,
    var delayOfGameWarningOptOut: Boolean,
)
