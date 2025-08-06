package com.fcfb.arceus.models.dto

import com.fcfb.arceus.domain.enums.DefensivePlaybook
import com.fcfb.arceus.domain.enums.OffensivePlaybook
import com.fcfb.arceus.domain.User.CoachPosition

data class NewSignupDTO(
    val id: Long,
    var username: String,
    var coachName: String,
    var discordTag: String,
    var discordId: String?,
    var position: CoachPosition,
    var teamChoiceOne: String?,
    var teamChoiceTwo: String?,
    var teamChoiceThree: String?,
    var offensivePlaybook: OffensivePlaybook,
    var defensivePlaybook: DefensivePlaybook,
    var approved: Boolean,
)
