package com.fcfb.arceus.api.controllers

import com.fcfb.arceus.domain.OngoingGamesEntity
import com.fcfb.arceus.domain.TeamsEntity
import com.fcfb.arceus.api.repositories.OngoingGamesRepository
import com.fcfb.arceus.api.repositories.TeamsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/arceus/ongoing_games")
class OngoingGamesController {
    @Autowired
    var ongoingGamesRepository: OngoingGamesRepository? = null

    @Autowired
    var teamsRepository: TeamsRepository? = null

    /**
     * Get a ongoing game by id
     * @param id
     * @return
     */
    @GetMapping("/id")
    fun getOngoingGameById(
        @RequestParam("id") id: Int
    ): ResponseEntity<OngoingGamesEntity> {
        val ongoingGameData: Optional<OngoingGamesEntity?> = ongoingGamesRepository?.findByGameId(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!ongoingGameData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(ongoingGameData.get(), HttpStatus.OK)
    }

    /**
     * Get a ongoing game by platform id
     * @param channelId
     * @return
     */
    @GetMapping("/discord")
    fun getOngoingGameByDiscordChannelId(
        @RequestParam("channelId") channelId: String?
    ): ResponseEntity<OngoingGamesEntity> {
        val ongoingGameData: Optional<OngoingGamesEntity?> = ongoingGamesRepository?.findByHomePlatformId("Discord", channelId)
            ?: ongoingGamesRepository?.findByAwayPlatformId("Discord", channelId) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!ongoingGameData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(ongoingGameData.get(), HttpStatus.OK)
    }

    /**
     * Start a game
     * @param season
     * @param subdivision
     * @param homeTeam
     * @param awayTeam
     * @param tvChannel
     * @param startTime
     * @param location
     * @return
     */
    @PostMapping("/start")
    fun startGame(
        @RequestParam("homePlatform") homePlatform: String?,
        @RequestParam("homePlatformId") homePlatformId: String?,
        @RequestParam("awayPlatform") awayPlatform: String?,
        @RequestParam("awayPlatformId") awayPlatformId: String?,
        @RequestParam("season") season: String,
        @RequestParam("week") week: String,
        @RequestParam("subdivision") subdivision: String?,
        @RequestParam("homeTeam") homeTeam: String,
        @RequestParam("awayTeam") awayTeam: String,
        @RequestParam("tvChannel") tvChannel: String?,
        @RequestParam("startTime") startTime: String?,
        @RequestParam("location") location: String?,
        @RequestParam("isScrimmage") isScrimmage: Boolean?
    ): ResponseEntity<OngoingGamesEntity> {
        return try {
            val homeTeamData: Optional<TeamsEntity?> = teamsRepository?.findByName(homeTeam) ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST)
            if (!homeTeamData.isPresent) {
                return ResponseEntity(null, HttpStatus.BAD_REQUEST)
            }
            val awayTeamData: Optional<TeamsEntity?> = teamsRepository?.findByName(awayTeam) ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST)
            if (!awayTeamData.isPresent) {
                return ResponseEntity(null, HttpStatus.BAD_REQUEST)
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
                    homeTeam,
                    awayTeam,
                    homeTeamData.get().coach ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST),
                    awayTeamData.get().coach ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST),
                    homeTeamData.get().offensivePlaybook?.lowercase() ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST),
                    awayTeamData.get().offensivePlaybook?.lowercase() ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST),
                    homeTeamData.get().defensivePlaybook?.lowercase() ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST),
                    awayTeamData.get().defensivePlaybook?.lowercase() ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST),
                    0,
                    0,
                    "home",
                    1,
                    "7:00",
                    0,
                    1,
                    10,
                    tvChannel,
                    startTime,
                    location,
                    homeTeamData.get().currentWins,
                    homeTeamData.get().currentLosses,
                    awayTeamData.get().currentWins,
                    awayTeamData.get().currentLosses,
                    "none_scorebug.png",
                    subdivision,
                    LocalDateTime.now(),
                    0.0,
                    java.lang.Boolean.FALSE,
                    java.lang.Boolean.FALSE, season.toInt(), week.toInt(),
                    awayTeamData.get().coach ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST),
                    "none_winprob.png",
                    "none_scoreplot.png",
                    0,
                    3,
                    3,
                    "None",
                    "None",
                    homePlatform,
                    homePlatformId,
                    awayPlatform,
                    awayPlatformId,
                    formattedDateTime,
                    "KICKOFF",
                    0,
                    isScrimmage,
                    true
                )
            ) ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST)

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
            ResponseEntity(newGame, HttpStatus.CREATED)
            
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.BAD_REQUEST)
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
            val gameData: Optional<OngoingGamesEntity?> = ongoingGamesRepository?.findById(gameId.toInt()) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
            if (!gameData.isPresent) {
                return ResponseEntity(null, HttpStatus.NOT_FOUND)
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
            ResponseEntity(null, HttpStatus.BAD_REQUEST)
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
            val gameData: Optional<OngoingGamesEntity?> = ongoingGamesRepository?.findById(gameId.toInt()) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
            if (!gameData.isPresent) {
                return ResponseEntity(null, HttpStatus.NOT_FOUND)
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
            ResponseEntity(null, HttpStatus.BAD_REQUEST)
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
            val gameData: Optional<OngoingGamesEntity?> = ongoingGamesRepository?.findById(gameId.toInt()) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
            if (!gameData.isPresent) {
                return ResponseEntity(null, HttpStatus.NOT_FOUND)
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
            ResponseEntity(null, HttpStatus.BAD_REQUEST)
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
        ongoingGamesRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!ongoingGamesRepository?.findById(id)!!.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        ongoingGamesRepository?.deleteById(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
