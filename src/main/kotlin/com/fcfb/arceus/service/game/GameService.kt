package com.fcfb.arceus.service.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.CoinTossCall
import com.fcfb.arceus.domain.Game.CoinTossChoice
import com.fcfb.arceus.domain.Game.CoinTossWinner
import com.fcfb.arceus.domain.Game.Platform
import com.fcfb.arceus.domain.Game.PlayType
import com.fcfb.arceus.domain.Game.Possession
import com.fcfb.arceus.models.requests.StartRequest
import com.fcfb.arceus.repositories.GameRepository
import com.fcfb.arceus.repositories.TeamRepository
import com.fcfb.arceus.service.discord.DiscordService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Random

@Component
class GameService(
    private var gameRepository: GameRepository,
    private var teamRepository: TeamRepository,
    private var discordService: DiscordService
) {
    private var emptyHeaders: HttpHeaders = HttpHeaders()

    fun getGameById(
        id: Int
    ): ResponseEntity<Game> {
        val ongoingGameData = gameRepository.findByGameId(id)
        return ResponseEntity(ongoingGameData, HttpStatus.OK)
    }

    fun getOngoingGameByDiscordChannelId(
        channelId: String?
    ): ResponseEntity<Game> {
        val ongoingGameData = gameRepository.findByHomePlatformId("Discord", channelId) ?:
            gameRepository.findByAwayPlatformId("Discord", channelId)
        return ongoingGameData?.let {
            ResponseEntity(it, HttpStatus.OK)
        } ?: ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
    }

    fun startGame(
        startRequest: StartRequest
    ): ResponseEntity<Game> {
        println("Deserialized StartRequest: $startRequest")
        return try {
            val homeTeamData = teamRepository.findByName(startRequest.homeTeam)
                ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)

            val awayTeamData = teamRepository.findByName(startRequest.awayTeam)
                ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)

            // Set the DOG timer
            // Get the current date and time
            val now = LocalDateTime.now()

            // Add 24 hours to the current date and time
            val futureTime = now.plusHours(24)

            // Define the desired date and time format
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")

            // Format the result and set it on the game
            val formattedDateTime = futureTime.format(formatter)

            // Validate request fields
            val homeTeam = startRequest.homeTeam ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val awayTeam = startRequest.awayTeam ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val homeCoachUsername = homeTeamData.coachUsername ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val awayCoachUsername = awayTeamData.coachUsername ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val homeCoachDiscordId = homeTeamData.coachDiscordId ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val awayCoachDiscordId = awayTeamData.coachDiscordId ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val homeOffensivePlaybook = homeTeamData.offensivePlaybook ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val awayOffensivePlaybook = awayTeamData.offensivePlaybook ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val homeDefensivePlaybook = homeTeamData.defensivePlaybook ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val awayDefensivePlaybook = awayTeamData.defensivePlaybook ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val subdivision = startRequest.subdivision ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val homePlatform = startRequest.homePlatform ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val awayPlatform = startRequest.awayPlatform ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)

            // Create and save the Game object
            val newGame = gameRepository.save(
                Game(
                    homeTeam = homeTeam,
                    awayTeam = awayTeam,
                    homeCoach = homeCoachUsername,
                    awayCoach = awayCoachUsername,
                    homeCoachDiscordId = homeCoachDiscordId,
                    awayCoachDiscordId = awayCoachDiscordId,
                    homeOffensivePlaybook = homeOffensivePlaybook,
                    awayOffensivePlaybook = awayOffensivePlaybook,
                    homeDefensivePlaybook = homeDefensivePlaybook,
                    awayDefensivePlaybook = awayDefensivePlaybook,
                    homeScore = 0,
                    awayScore = 0,
                    possession = Possession.HOME,
                    quarter = 1,
                    clock = "7:00",
                    ballLocation = 0,
                    down = 1,
                    yardsToGo = 10,
                    tvChannel = startRequest.tvChannel,
                    startTime = startRequest.startTime,
                    location = startRequest.location,
                    homeWins = homeTeamData.currentWins,
                    homeLosses = homeTeamData.currentLosses,
                    awayWins = awayTeamData.currentWins,
                    awayLosses = awayTeamData.currentLosses,
                    scorebug = "none_scorebug.png",
                    subdivision = subdivision,
                    timestamp = LocalDateTime.now().toString(),
                    winProbability = 0.0,
                    final = false,
                    ot = false,
                    season = startRequest.season?.toInt(),
                    week = startRequest.week?.toInt(),
                    waitingOn = awayCoachUsername,
                    winProbabilityPlot = "none_winprob.png",
                    scorePlot = "none_scoreplot.png",
                    numPlays = 0,
                    homeTimeouts = 3,
                    awayTimeouts = 3,
                    coinTossWinner = null,
                    coinTossChoice = null,
                    homePlatform = homePlatform,
                    homePlatformId = null,
                    awayPlatform = awayPlatform,
                    awayPlatformId = null,
                    gameTimer = formattedDateTime,
                    currentPlayType = PlayType.KICKOFF,
                    currentPlayId = 0,
                    scrimmage = startRequest.scrimmage,
                    clockStopped = true
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

            // Create a new Discord thread
            if (newGame.homePlatform == Platform.DISCORD) {
                newGame.homePlatformId = discordService.startGameThread(newGame)
            }
            else if (newGame.awayPlatform == Platform.DISCORD) {
                newGame.awayPlatformId = discordService.startGameThread(newGame)
            }

            // Save the updated entity
            gameRepository.save(newGame)
            ResponseEntity(newGame, HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }

    fun runCoinToss(
        gameId: String,
        coinTossCall: CoinTossCall
    ): ResponseEntity<Game> {
        return try {
            val game: Game = gameRepository.findById(gameId.toInt()).orElse(null)
                ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

            val result = Random().nextInt(2)
            // 1 is heads, away team called tails, they lose
            val coinTossWinner = if (result == 1 && coinTossCall === CoinTossCall.TAILS) {
                CoinTossWinner.HOME
            } else {
                CoinTossWinner.AWAY
            }
            game.coinTossWinner = coinTossWinner
            return ResponseEntity(gameRepository.save(game), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }

    fun updateCoinTossChoice(
        gameId: String,
        coinTossChoice: CoinTossChoice
    ): ResponseEntity<Game> {
        return try {
            val game: Game = gameRepository.findById(gameId.toInt()).orElse(null)
                ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

            game.coinTossChoice = coinTossChoice
            if (game.coinTossWinner == CoinTossWinner.HOME && coinTossChoice == CoinTossChoice.RECEIVE) {
                game.possession = Possession.HOME
            } else if (game.coinTossWinner == CoinTossWinner.AWAY && coinTossChoice == CoinTossChoice.DEFER) {
                game.possession = Possession.AWAY
            }
            return ResponseEntity(gameRepository.save(game), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }

    fun updateWaitingOn(
        gameId: String,
        username: String
    ): ResponseEntity<Game> {
        return try {
            val game: Game = gameRepository.findById(gameId.toInt()).orElse(null)
                ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

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
            return ResponseEntity(gameRepository.save(game), HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }

    fun deleteOngoingGame(
        id: Int
    ): ResponseEntity<HttpStatus> {
        gameRepository.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!gameRepository.findById(id).isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        gameRepository.deleteById(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
