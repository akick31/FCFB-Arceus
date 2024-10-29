package com.fcfb.arceus.controllers

import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.RunoffType
import com.fcfb.arceus.service.fcfb.game.PlayService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/play")
class PlayController(
    private var playService: PlayService,
) {
    /**
     * Start a new play, the defensive number was submitted. The defensive number is encrypted
     * @param gameId
     * @param defensiveNumber
     * @return
     */
    @PostMapping("/submit_defense")
    fun defensiveNumberSubmitted(
        @RequestParam("gameId") gameId: Int,
        @RequestParam("defensiveSubmitter") defensiveSubmitter: String,
        @RequestParam("defensiveNumber") defensiveNumber: Int,
        @RequestParam("timeoutCalled") timeoutCalled: Boolean = false,
    ) = playService.defensiveNumberSubmitted(gameId, defensiveSubmitter, defensiveNumber, timeoutCalled)

    /**
     * The offensive number was submitted, run the play
     * @param gameId
     * @param offensiveNumber
     * @param playCall
     * @param runoffType
     * @param timeoutCalled
     * @return
     */
    @PutMapping("/submit_offense")
    fun offensiveNumberSubmitted(
        @RequestParam("gameId") gameId: Int,
        @RequestParam("offensiveSubmitter") offensiveSubmitter: String,
        @RequestParam("offensiveNumber") offensiveNumber: Int,
        @RequestParam("playCall") playCall: PlayCall,
        @RequestParam("runoffType") runoffType: RunoffType,
        @RequestParam("timeoutCalled") timeoutCalled: Boolean,
    ) = playService.offensiveNumberSubmitted(gameId, offensiveSubmitter, offensiveNumber, playCall, runoffType, timeoutCalled)

    /**
     * Get a play by its id
     * @param playId
     * @return
     */
    @GetMapping("")
    fun getPlayById(
        @RequestParam("playId") playId: Int,
    ) = playService.getPlayById(playId)

    @GetMapping("/game")
    fun getAllPlaysByGameId(
        @RequestParam("gameId") gameId: Int,
    ) = playService.getAllPlaysByGameId(gameId)
    // TODO: Rollback play
}
