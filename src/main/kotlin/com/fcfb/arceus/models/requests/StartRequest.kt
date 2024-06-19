package com.fcfb.arceus.models.requests

import com.fcfb.arceus.domain.Game.Platform
import com.fcfb.arceus.domain.Game.Subdivision
import com.fcfb.arceus.domain.Game.TVChannel

data class StartRequest(
    val homePlatform: Platform?,
    val awayPlatform: Platform?,
    val season: String?,
    val week: String?,
    val subdivision: Subdivision?,
    val homeTeam: String?,
    val awayTeam: String?,
    val tvChannel: TVChannel?,
    val startTime: String?,
    val location: String?,
    val isScrimmage: Boolean?
)
