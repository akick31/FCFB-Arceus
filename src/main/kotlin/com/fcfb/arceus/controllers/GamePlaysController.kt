package com.fcfb.arceus.controllers

import com.fcfb.arceus.models.game.Game.Play
import com.fcfb.arceus.models.game.Game.RunoffType
import com.fcfb.arceus.service.game.GamePlaysService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
