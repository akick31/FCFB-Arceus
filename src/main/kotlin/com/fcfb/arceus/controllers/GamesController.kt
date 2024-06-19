package com.fcfb.arceus.controllers

import com.fcfb.arceus.models.game.Game.CoinTossCall
import com.fcfb.arceus.models.game.Game.CoinTossChoice
import com.fcfb.arceus.models.requests.StartRequest
import com.fcfb.arceus.repositories.GamesRepository
import com.fcfb.arceus.repositories.TeamsRepository
import com.fcfb.arceus.service.discord.DiscordService
import com.fcfb.arceus.service.game.GamesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
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

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/ongoing_games")
class GamesController(
    private var discordService: DiscordService,
    private var gamesService: GamesService
) {
    @Autowired
    var gamesRepository: GamesRepository? = null

    @Autowired
    var teamsRepository: TeamsRepository? = null

    private var emptyHeaders: HttpHeaders = HttpHeaders()

    /**
     * Get a ongoing game by id
     * @param id
     * @return
     */
    @GetMapping("/id")
    fun getOngoingGameById(
        @RequestParam("id") id: Int
    ) = gamesService.getGameById(id)

    /**
     * Get an ongoing game by platform id
     * @param channelId
     * @return
     */
    @GetMapping("/discord")
    fun getOngoingGameByDiscordChannelId(
        @RequestParam("channelId") channelId: String?
    ) = gamesService.getOngoingGameByDiscordChannelId(channelId)

    /**
     * Start a game
     * @param startRequest
     * @return
     */
    @PostMapping("/start")
    fun startGame(
        @RequestBody startRequest: StartRequest
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
        @RequestParam("coinTossCall") coinTossCall: CoinTossCall
    ) = gamesService.runCoinToss(gameId, coinTossCall)

    /**
     * Set the coin toss receive or defer choice
     * @param gameId
     * @param coinTossChoice
     * @return
     */
    @PutMapping("/coin_toss_choice")
    fun updateCoinTossChoice(
        @RequestParam("gameId") gameId: String,
        @RequestParam("coinTossChoice") coinTossChoice: CoinTossChoice
    ) = gamesService.updateCoinTossChoice(gameId, coinTossChoice)

    /**
     * Update the user being waited on, also update the game timer
     * @param gameId
     * @param username
     * @return
     */
    @PutMapping("/update_waiting_on")
    fun updateWaitingOn(
        @RequestParam("gameId") gameId: String,
        @RequestParam("username") username: String
    ) = gamesService.updateWaitingOn(gameId, username)

    /**
     * Delete an ongoing game
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    fun deleteOngoingGame(
        @PathVariable("id") id: Int
    ) = gamesService.deleteOngoingGame(id)
}
