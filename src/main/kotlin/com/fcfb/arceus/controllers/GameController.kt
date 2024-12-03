package com.fcfb.arceus.controllers

import com.fcfb.arceus.domain.Game.CoinTossCall
import com.fcfb.arceus.domain.Game.CoinTossChoice
import com.fcfb.arceus.models.requests.StartRequest
import com.fcfb.arceus.service.fcfb.game.GameService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/game")
class GameController(
    private var gameService: GameService,
) {
    /**
     * Get a ongoing game by id
     * @param id
     * @return
     */
    @GetMapping("/id")
    fun getOngoingGameById(
        @RequestParam("id") id: Int,
    ) = gameService.getGameById(id)

    /**
     * Start a game
     * @param startRequest
     * @return
     */
    @PostMapping("/start")
    fun startGame(
        @RequestBody startRequest: StartRequest,
    ) = gameService.startGame(startRequest)

    /**
     * End a game
     */
    @PostMapping("/end")
    fun endGame(
        @RequestParam("channelId") channelId: ULong,
    ) = gameService.endGame(channelId)

    /**
     * Chew a game
     */
    @PostMapping("/chew")
    fun chewGame(
        @RequestParam("channelId") channelId: ULong,
    ) = gameService.chewGame(channelId)

    /**
     * Run the game's coin toss
     * @param gameId
     * @param coinTossCall
     * @return
     */
    @PutMapping("/coin_toss")
    fun runCoinToss(
        @RequestParam("gameId") gameId: String,
        @RequestParam("coinTossCall") coinTossCall: CoinTossCall,
    ) = gameService.runCoinToss(gameId, coinTossCall)

    /**
     * Set the coin toss receive or defer choice
     * @param gameId
     * @param coinTossChoice
     * @return
     */
    @PutMapping("/make_coin_toss_choice")
    fun makeCoinTossChoice(
        @RequestParam("gameId") gameId: String,
        @RequestParam("coinTossChoice") coinTossChoice: CoinTossChoice,
    ) = gameService.makeCoinTossChoice(gameId, coinTossChoice)

    /**
     * Update the request message id
     * @param gameId
     * @param requestMessageId
     * @return
     */
    @PutMapping("/request_message")
    fun updateRequestMessageId(
        @RequestParam("gameId") gameId: Int,
        @RequestParam("requestMessageId") requestMessageId: String,
    ) = gameService.updateRequestMessageId(gameId, requestMessageId)

    /**
     * Get the game by request message id
     */
    @GetMapping("/request_message")
    fun getGameByRequestMessageId(
        @RequestParam("requestMessageId") requestMessageId: String,
    ) = gameService.getGameByRequestMessageId("\"$requestMessageId\"")

    /**
     * Get the game by platform id
     */
    @GetMapping("/platform_id")
    fun getGameByPlatformId(
        @RequestParam("id") platformId: ULong,
    ) = gameService.getGameByPlatformId(platformId)

    /**
     * Sub a coach in the game for a team
     * @param gameId
     * @param team
     * @param coachId
     */
    @PutMapping("/sub")
    fun subCoach(
        @RequestParam("gameId") gameId: Int,
        @RequestParam("team") team: String,
        @RequestParam("coachId") coachId: String,
    ) = gameService.subCoach(gameId, team, coachId)

    /**
     * Delete an ongoing game
     * @param id
     * @return
     */
    @DeleteMapping("")
    fun deleteOngoingGame(
        @RequestParam("channelId") channelId: ULong,
    ) = gameService.deleteOngoingGame(channelId)

    // TODO: end game
}
