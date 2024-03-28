package com.fcfb.arceus.api.controllers

import com.fcfb.arceus.domain.OngoingGamesEntity
import com.fcfb.arceus.domain.TeamsEntity
import com.fcfb.arceus.api.repositories.OngoingGamesRepository
import com.fcfb.arceus.api.repositories.TeamsRepository
import com.fcfb.arceus.models.StartGameRequest
import com.fcfb.arceus.service.discord.DiscordService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/ongoing_games")
class OngoingGamesController(
    private var discordService: DiscordService
) {
    @Autowired
    var ongoingGamesRepository: OngoingGamesRepository? = null

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
    ): ResponseEntity<OngoingGamesEntity> {
        val ongoingGameData: Optional<OngoingGamesEntity?> = ongoingGamesRepository?.findByGameId(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!ongoingGameData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(ongoingGameData.get(), HttpStatus.OK)
    }

    /**
     * Get an ongoing game by platform id
     * @param channelId
     * @return
     */
    @GetMapping("/discord")
    fun getOngoingGameByDiscordChannelId(
        @RequestParam("channelId") channelId: String?
    ): ResponseEntity<OngoingGamesEntity> {
        val ongoingGameData: Optional<OngoingGamesEntity?> = ongoingGamesRepository?.findByHomePlatformId("Discord", channelId)
            ?: ongoingGamesRepository?.findByAwayPlatformId("Discord", channelId) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!ongoingGameData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(ongoingGameData.get(), HttpStatus.OK)
    }

    /**
     * Start a game
     * @param startGameRequest
     * @return
     */
    @PostMapping("/start")
    suspend fun startGame(
        @RequestBody startGameRequest: StartGameRequest
    ): ResponseEntity<OngoingGamesEntity> {
        return try {
            val homeTeamData: Optional<TeamsEntity?> = teamsRepository?.findByName(startGameRequest.homeTeam)
                ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            if (!homeTeamData.isPresent) {
                return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            }
            val awayTeamData: Optional<TeamsEntity?> = teamsRepository?.findByName(startGameRequest.awayTeam)
                ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            if (!awayTeamData.isPresent) {
                return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            }

            // Set the DOG timer
            // Get the current date and time
            val now = LocalDateTime.now()

            // Add 24 hours to the current date and time
            val futureTime = now.plusHours(24)

            // Define the desired date and time format
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")

            // Format the result and set it on the game
            val formattedDateTime = futureTime.format(formatter)
            val newGame: OngoingGamesEntity = ongoingGamesRepository?.save(
                OngoingGamesEntity(
                    startGameRequest.homeTeam,
                    startGameRequest.awayTeam,
                    homeTeamData.get().coach ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST),
                    awayTeamData.get().coach ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST),
                    homeTeamData.get().offensivePlaybook?.lowercase() ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST),
                    awayTeamData.get().offensivePlaybook?.lowercase() ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST),
                    homeTeamData.get().defensivePlaybook?.lowercase() ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST),
                    awayTeamData.get().defensivePlaybook?.lowercase() ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST),
                    0,
                    0,
                    "home",
                    1,
                    "7:00",
                    0,
                    1,
                    10,
                    startGameRequest.tvChannel,
                    startGameRequest.startTime,
                    startGameRequest.location,
                    homeTeamData.get().currentWins,
                    homeTeamData.get().currentLosses,
                    awayTeamData.get().currentWins,
                    awayTeamData.get().currentLosses,
                    "none_scorebug.png",
                    startGameRequest.subdivision,
                    LocalDateTime.now(),
                    0.0,
                    java.lang.Boolean.FALSE,
                    java.lang.Boolean.FALSE,
                    startGameRequest.season.toInt(),
                    startGameRequest.week.toInt(),
                    awayTeamData.get().coach ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST),
                    "none_winprob.png",
                    "none_scoreplot.png",
                    0,
                    3,
                    3,
                    "None",
                    "None",
                    startGameRequest.homePlatform,
                    startGameRequest.homePlatformId,
                    startGameRequest.awayPlatform,
                    startGameRequest.awayPlatformId,
                    formattedDateTime,
                    "KICKOFF",
                    0,
                    startGameRequest.isScrimmage,
                    true
                )
            ) ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)

            // Create image names
            val gameId: String = java.lang.String.valueOf(newGame.gameId)
            val scorebugName = gameId + "_scorebug.png"
            val winprobName = gameId + "_winprob.png"
            val scoreplotName = gameId + "_scoreplot.png"

            // Update the entity with the new image names
            newGame.scorebug = scorebugName
            newGame.winProbabilityPlot = winprobName
            newGame.scorePlot = scoreplotName

            // Save the updated entity
            ongoingGamesRepository?.save(newGame)

            // Create a new Discord thread
            discordService.createGameThread(newGame)
            ResponseEntity(newGame, HttpStatus.CREATED)
            
        } catch (e: Exception) {
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * Run the game's coin toss
     * @param gameId
     * @param coinTossCall
     * @return
     */
    @PutMapping("/coin_toss")
    fun runCoinToss(
        @RequestParam("gameId") gameId: String,
        @RequestParam("coinTossCall") coinTossCall: String
    ): ResponseEntity<OngoingGamesEntity> {
        return try {
            val gameData: Optional<OngoingGamesEntity?> = ongoingGamesRepository?.findById(gameId.toInt()) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
            if (!gameData.isPresent) {
                return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
            }
            val game: OngoingGamesEntity = gameData.get()
            val result = Random().nextInt(2)
            // 1 is heads, away team called tails, they lose
            val coinTossWinner = if (result == 1 && coinTossCall === "tails") {
                game.homeTeam
            } else  {
                game.awayTeam
            }
            game.coinTossWinner = coinTossWinner
            return ResponseEntity(ongoingGamesRepository?.save(game), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * Set the coin toss receive or defer choice
     * @param gameId
     * @param coinTossChoice
     * @return
     */
    @PutMapping("/coin_toss_choice")
    fun updateCoinTossChoice(
        @RequestParam("gameId") gameId: String,
        @RequestParam("coinTossChoice") coinTossChoice: String
    ): ResponseEntity<OngoingGamesEntity> {
        return try {
            val gameData: Optional<OngoingGamesEntity?> = ongoingGamesRepository?.findById(gameId.toInt()) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
            if (!gameData.isPresent) {
                return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
            }
            val game: OngoingGamesEntity = gameData.get()
            game.coinTossChoice = coinTossChoice
            if (game.coinTossWinner == game.homeTeam && coinTossChoice == "receive") {
                game.possession = "home"
            } else if (game.coinTossWinner == game.awayTeam && coinTossChoice == "defer") {
                game.possession = "away"
            }
            return ResponseEntity(ongoingGamesRepository?.save(game), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }

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
    ): ResponseEntity<OngoingGamesEntity> {
        return try {
            val gameData: Optional<OngoingGamesEntity?> = ongoingGamesRepository?.findById(gameId.toInt()) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
            if (!gameData.isPresent) {
                return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
            }
            val game: OngoingGamesEntity = gameData.get()
            game.waitingOn = username

            // Set the DOG timer
            // Get the current date and time
            val now = LocalDateTime.now()

            // Add 24 hours to the current date and time
            val futureTime = now.plusHours(24)

            // Define the desired date and time format
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")

            // Format the result and set it on the game
            val formattedDateTime = futureTime.format(formatter)
            game.gameTimer = formattedDateTime
            return ResponseEntity(ongoingGamesRepository?.save(game), HttpStatus.OK)

        } catch (e: Exception) {
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * Delete an ongoing game
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    fun deleteOngoingGame(
        @PathVariable("id") id: Int
    ): ResponseEntity<HttpStatus> {
        ongoingGamesRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!ongoingGamesRepository?.findById(id)!!.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        ongoingGamesRepository?.deleteById(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
