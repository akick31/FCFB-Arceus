package com.fcfb.arceus.models.response

import com.fcfb.arceus.domain.Game

data class ScorebugResponse(
    val gameId: Int,
    val scorebug: ByteArray?,
    val homeTeam: String,
    val awayTeam: String,
    val status: Game.GameStatus?
)