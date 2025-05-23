package com.fcfb.arceus.controllers

import com.fcfb.arceus.domain.Game.CoinTossCall
import com.fcfb.arceus.domain.Game.CoinTossChoice
import com.fcfb.arceus.domain.Game.OvertimeCoinTossChoice
import com.fcfb.arceus.models.requests.StartRequest
import com.fcfb.arceus.service.fcfb.GameService
import com.fcfb.arceus.service.fcfb.GameSpecificationService.GameCategory
import com.fcfb.arceus.service.fcfb.GameSpecificationService.GameFilter
import com.fcfb.arceus.service.fcfb.GameSpecificationService.GameSort
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
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
     * Get all ongoing games
     * @return
     */
    @GetMapping("/filtered")
    fun getFilteredGames(
        @RequestParam(required = false) filters: List<GameFilter>?,
        @RequestParam(required = false) category: GameCategory?,
        @RequestParam(defaultValue = "CLOSEST_TO_END") sort: GameSort,
        @RequestParam(required = false) conference: String?,
        @RequestParam(required = false) season: Int?,
        @RequestParam(required = false) week: Int?,
        @PageableDefault(size = 20) pageable: Pageable,
    ) = gameService.getFilteredGames(
        filters = filters ?: emptyList(),
        category = category,
        conference = conference,
        season = season,
        week = week,
        sort = sort,
        pageable = pageable,
    )

    /**
     * Start a game
     * @param startRequest
     * @return
     */
    @PostMapping("/start")
    suspend fun startGame(
        @RequestBody startRequest: StartRequest,
    ) = gameService.startSingleGame(startRequest, null)

    /**
     * Start a game in overtime
     * @param startRequest
     * @return
     */
    @PostMapping("/start_overtime")
    suspend fun startOvertimeGame(
        @RequestBody startRequest: StartRequest,
    ) = gameService.startOvertimeGame(startRequest)

    /**
     * Start all games for a week
     * @param season
     * @param week
     * @return
     */
    @PostMapping("/start_week")
    suspend fun startWeek(
        @RequestParam("season") season: Int,
        @RequestParam("week") week: Int,
    ) = gameService.startWeek(season, week)

    /**
     * End a game
     */
    @PostMapping("/end")
    fun endGame(
        @RequestParam("channelId") channelId: ULong,
    ) = gameService.endSingleGame(channelId)

    /**
     * End all ongoing games
     */
    @PostMapping("/end_all")
    fun endAllGames() = gameService.endAllGames()

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
     * Set the coin toss offense or defense choice
     * @param gameId
     * @param coinTossChoice
     * @return
     */
    @PutMapping("/make_overtime_coin_toss_choice")
    fun makeCoinTossChoice(
        @RequestParam("gameId") gameId: String,
        @RequestParam("coinTossChoice") coinTossChoice: OvertimeCoinTossChoice,
    ) = gameService.makeOvertimeCoinTossChoice(gameId, coinTossChoice)

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
     * Update the last message timestamp
     * @param gameId
     * @param gameId
     * @return
     */
    @PutMapping("/last_message_timestamp")
    fun updateLastMessageTimestamp(
        @RequestParam("gameId") gameId: Int,
    ) = gameService.updateLastMessageTimestamp(gameId)

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
     * @param discordId
     */
    @PutMapping("/sub")
    fun subCoachIntoGame(
        @RequestParam("gameId") gameId: Int,
        @RequestParam("team") team: String,
        @RequestParam("discordId") discordId: String,
    ) = gameService.subCoachIntoGame(gameId, team, discordId)

    /**
     * Restart a game
     * @param channelId
     */
    @PostMapping("/restart")
    suspend fun restartGame(
        @RequestParam("channelId") channelId: ULong,
    ) = gameService.restartGame(channelId)

    /**
     * Mark the game as having pinged the close game role
     * @param gameId
     */
    @PutMapping("/close_game_pinged")
    fun markCloseGamePinged(
        @RequestParam("gameId") gameId: Int,
    ) = gameService.markCloseGamePinged(gameId)

    /**
     * Mark the game as having pinged the close game role
     * @param gameId
     */
    @PutMapping("/upset_alert_pinged")
    fun markUpsetAlertPinged(
        @RequestParam("gameId") gameId: Int,
    ) = gameService.markUpsetAlertPinged(gameId)

    /**
     * Delete an ongoing game
     * @param channelId
     * @return
     */
    @DeleteMapping("")
    fun deleteOngoingGame(
        @RequestParam("channelId") channelId: ULong,
    ) = gameService.deleteOngoingGame(channelId)
}
