package com.fcfb.arceus.api.controllers

import com.fcfb.arceus.domain.GamePlaysEntity
import com.fcfb.arceus.domain.OngoingGamesEntity
import com.fcfb.arceus.api.repositories.GamePlaysRepository
import com.fcfb.arceus.api.repositories.GameStatsRepository
import com.fcfb.arceus.api.repositories.OngoingGamesRepository
import com.fcfb.arceus.models.game.Game.Possession
import com.fcfb.arceus.models.game.Game.RunoffType
import com.fcfb.arceus.models.game.Game.Play
import com.fcfb.arceus.service.game.*
import com.fcfb.arceus.utils.EncryptionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/game_plays")
class GamePlaysController(
    private var gamePlaysService: GamePlaysService
) {
    /**
     * Start a new play, the defensive number was submitted. The defensive number is encrypted
     * @param gameId
     * @param defensiveNumber
     * @return
     */
    @PostMapping("/defense_submitted")
    fun defensiveNumberSubmitted(
        @RequestParam("gameId") gameId: Int,
        @RequestParam("defensiveNumber") defensiveNumber: Int,
        @RequestParam("timeoutCalled") timeoutCalled: Boolean?
    ) = gamePlaysService.defensiveNumberSubmitted(gameId, defensiveNumber, timeoutCalled)

    /**
     * The offensive number was submitted, run the play
     * @param playId
     * @param offensiveNumber
     * @param playCall
     * @param runoffType
     * @param offensiveTimeoutCalled
     * @param defensiveTimeoutCalled
     * @return
     */
    @PutMapping("/offense_submitted")
    fun offensiveNumberSubmitted(
        @RequestParam("playId") playId: Int,
        @RequestParam("offensiveNumber") offensiveNumber: Int,
        @RequestParam("playCall") playCall: Play,
        @RequestParam("runoffType") runoffType: RunoffType,
        @RequestParam("offensiveTimeoutCalled") offensiveTimeoutCalled: Boolean,
        @RequestParam("defensiveTimeoutCalled") defensiveTimeoutCalled: Boolean
    ) = gamePlaysService.offensiveNumberSubmitted(playId, offensiveNumber, playCall, runoffType, offensiveTimeoutCalled, defensiveTimeoutCalled)
}
