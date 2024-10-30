package com.fcfb.arceus.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.fcfb.arceus.domain.Game.GameType
import com.fcfb.arceus.domain.Game.Platform
import com.fcfb.arceus.domain.Game.Subdivision
import com.fcfb.arceus.domain.Game.TVChannel

data class StartRequest(
    @JsonProperty("homePlatform") val homePlatform: Platform,
    @JsonProperty("awayPlatform") val awayPlatform: Platform,
    @JsonProperty("subdivision") val subdivision: Subdivision,
    @JsonProperty("homeTeam") val homeTeam: String,
    @JsonProperty("awayTeam") val awayTeam: String,
    @JsonProperty("tvChannel") val tvChannel: TVChannel?,
    @JsonProperty("startTime") val startTime: String?,
    @JsonProperty("location") val location: String?,
    @JsonProperty("gameType") val gameType: GameType,
)
