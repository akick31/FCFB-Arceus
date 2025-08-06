package com.fcfb.arceus.models.requests

import com.fasterxml.jackson.annotation.JsonProperty
import com.fcfb.arceus.domain.enums.GameType
import com.fcfb.arceus.domain.enums.Platform
import com.fcfb.arceus.domain.enums.Subdivision
import com.fcfb.arceus.domain.enums.TVChannel

data class StartRequest(
    @JsonProperty("homePlatform") val homePlatform: Platform,
    @JsonProperty("awayPlatform") val awayPlatform: Platform,
    @JsonProperty("subdivision") val subdivision: Subdivision,
    @JsonProperty("homeTeam") val homeTeam: String,
    @JsonProperty("awayTeam") val awayTeam: String,
    @JsonProperty("tvChannel") val tvChannel: TVChannel?,
    @JsonProperty("gameType") val gameType: GameType,
)
