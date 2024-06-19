package com.fcfb.arceus.models.requests

import com.fcfb.arceus.models.game.Game.Platform
import com.fcfb.arceus.models.game.Game.Subdivision
import com.fcfb.arceus.models.game.Game.TVChannel
import com.google.gson.annotations.SerializedName

data class StartRequest(
    @SerializedName("home_platform") val homePlatform: Platform?,
    @SerializedName("away_platform") val awayPlatform: Platform?,
    @SerializedName("season") val season: String?,
    @SerializedName("week") val week: String?,
    @SerializedName("subdivision") val subdivision: Subdivision?,
    @SerializedName("home_team") val homeTeam: String?,
    @SerializedName("away_team") val awayTeam: String?,
    @SerializedName("tv_channel") val tvChannel: TVChannel?,
    @SerializedName("start_time") val startTime: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("is_scrimmage") val isScrimmage: Boolean?
)
