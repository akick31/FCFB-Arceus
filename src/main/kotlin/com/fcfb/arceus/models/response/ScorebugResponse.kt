package com.fcfb.arceus.models.response

import com.fcfb.arceus.domain.enums.GameStatus

data class ScorebugResponse(
    val gameId: Int,
    val scorebug: ByteArray?,
    val homeTeam: String,
    val awayTeam: String,
    val status: GameStatus?,
)
