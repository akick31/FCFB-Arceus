package com.fcfb.arceus.models

data class StartGameRequest(
    val homePlatform: String?,
    val homePlatformId: String?,
    val awayPlatform: String?,
    val awayPlatformId: String?,
    val season: String,
    val week: String,
    val subdivision: String?,
    val homeTeam: String,
    val awayTeam: String,
    val tvChannel: String?,
    val startTime: String?,
    val location: String?,
    val isScrimmage: Boolean?
)
