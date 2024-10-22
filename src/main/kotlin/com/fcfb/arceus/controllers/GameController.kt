package com.fcfb.arceus.controllers

import com.fcfb.arceus.domain.Game.CoinTossCall
import com.fcfb.arceus.domain.Game.CoinTossChoice
import com.fcfb.arceus.models.requests.StartRequest
import com.fcfb.arceus.service.discord.DiscordService
import com.fcfb.arceus.service.fcfb.game.GameService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
    private var gamesService: GameService,
) {
    /**
     * Get a ongoing game by id
     * @param id
     * @return
     */
    @GetMapping("/id")
    fun getOngoingGameById(
        @RequestParam("id") id: Int,
    ) = gamesService.getGameById(id)

    /**
     * Get an ongoing game by channel or user id
     * @param channelId
     * @param discordId
     * @return
     */
    @GetMapping("/discord")
    fun getOngoingGame(
        @RequestParam("channelId", required = false) channelId: String?,
        @RequestParam("userId", required = false) discordId: String?,
    ) = when {
        channelId != null -> gamesService.getOngoingGameByDiscordChannelId(channelId)
        discordId != null -> gamesService.getOngoingGameByDiscordId(discordId)
        else -> ResponseEntity(HttpHeaders(), HttpStatus.BAD_REQUEST)
    }

    /**
     * Start a game
     * @param startRequest
     * @return
     */
    @PostMapping("/start")
    fun startGame(
        @RequestBody startRequest: StartRequest,
    ) = gamesService.startGame(startRequest)

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
    ) = gamesService.runCoinToss(gameId, coinTossCall)

    /**
     * Set the coin toss receive or defer choice
     * @param gameId
     * @param coinTossChoice
     * @return
     */
    @PutMapping("/make_coin_toss_choice")
    fun updateGame(
        @RequestParam("gameId") gameId: String,
        @RequestParam("coinTossChoice") coinTossChoice: CoinTossChoice,
    ) = gamesService.makeCoinTossChoice(gameId, coinTossChoice)

    /**
     * Delete an ongoing game
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    fun deleteOngoingGame(
        @PathVariable("id") id: Int,
    ) = gamesService.deleteOngoingGame(id)

    // TODO: end game
}
