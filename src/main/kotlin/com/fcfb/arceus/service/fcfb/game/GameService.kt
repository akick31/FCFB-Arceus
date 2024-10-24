package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.CoinTossCall
import com.fcfb.arceus.domain.Game.CoinTossChoice
import com.fcfb.arceus.domain.Game.GameStatus
import com.fcfb.arceus.domain.Game.Platform
import com.fcfb.arceus.domain.Game.PlayType
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.models.requests.StartRequest
import com.fcfb.arceus.repositories.GameRepository
import com.fcfb.arceus.repositories.TeamRepository
import com.fcfb.arceus.service.discord.DiscordService
import com.fcfb.arceus.utils.Logger
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
    private var discordService: DiscordService,
    private var gameStatsService: GameStatsService,
) {
    private var emptyHeaders: HttpHeaders = HttpHeaders()

    /**
     * Get an ongoing game by id
     * @param id
     * @return
     */
    fun getGameById(id: Int): ResponseEntity<Game> {
        val ongoingGameData =
            gameRepository.findByGameId(id) ?: run {
                Logger.error("No game found with id $id")
                return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
            }
        Logger.info("Found game ${ongoingGameData.gameId}")
        return ResponseEntity(ongoingGameData, HttpStatus.OK)
    }

    /**
     * Get an ongoing game by platform id
     * @param channelId
     * @return
     */
    fun getOngoingGameByDiscordChannelId(channelId: String?): ResponseEntity<Game> {
        val ongoingGameData =
            gameRepository.findOngoingGameByHomePlatformId("Discord", channelId)
                ?: gameRepository.findOngoingGameByAwayPlatformId("Discord", channelId)
        return ongoingGameData?.let {
            Logger.info("Found game ${ongoingGameData.gameId} for channel id $channelId")
            ResponseEntity(it, HttpStatus.OK)
        } ?: run {
            Logger.error("No game found for channel id $channelId")
            ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Get an ongoing game by Discord user id
     * @param discordId
     * @return
     */
    fun getOngoingGameByDiscordId(discordId: String?): ResponseEntity<Game> {
        val ongoingGameData =
            gameRepository.findOngoingGameByHomeCoachDiscordId1(discordId)
                ?: gameRepository.findOngoingGameByHomeCoachDiscordId2(discordId)
                ?: gameRepository.findOngoingGameByAwayCoachDiscordId1(discordId)
                ?: gameRepository.findOngoingGameByAwayCoachDiscordId2(discordId)
        return ongoingGameData?.let {
            Logger.info("Found game ${ongoingGameData.gameId} with Discord user id $discordId")
            ResponseEntity(it, HttpStatus.OK)
        } ?: run {
            Logger.error("No game found with Discord user id $discordId")
            ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Start a game
     * @param startRequest
     * @return
     */
    fun startGame(startRequest: StartRequest): ResponseEntity<Game> {
        return try {
            val homeTeamData =
                teamRepository.findByName(startRequest.homeTeam)
                    ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)

            val awayTeamData =
                teamRepository.findByName(startRequest.awayTeam)
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

            // If the second coach username is not null, there is a coordinator
            val homeCoachUsername1: String?
            val homeCoachUsername2: String?
            val homeCoachDiscordId1: String?
            val homeCoachDiscordId2: String?
            val awayCoachUsername1: String?
            val awayCoachUsername2: String?
            val awayCoachDiscordId1: String?
            val awayCoachDiscordId2: String?
            if (homeTeamData.coachUsername2 != null) {
                homeCoachUsername1 = homeTeamData.coachUsername1 ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
                homeCoachUsername2 = homeTeamData.coachUsername2 ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
                homeCoachDiscordId1 = homeTeamData.coachDiscordId1 ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
                homeCoachDiscordId2 = homeTeamData.coachDiscordId2 ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            } else {
                homeCoachUsername1 = homeTeamData.coachUsername1 ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
                homeCoachUsername2 = null
                homeCoachDiscordId1 = homeTeamData.coachDiscordId1 ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
                homeCoachDiscordId2 = null
            }

            if (awayTeamData.coachUsername2 != null) {
                awayCoachUsername1 = awayTeamData.coachUsername1 ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
                awayCoachUsername2 = awayTeamData.coachUsername2 ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
                awayCoachDiscordId1 = awayTeamData.coachDiscordId1 ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
                awayCoachDiscordId2 = awayTeamData.coachDiscordId2 ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            } else {
                awayCoachUsername1 = awayTeamData.coachUsername1 ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
                awayCoachUsername2 = null
                awayCoachDiscordId1 = awayTeamData.coachDiscordId1 ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
                awayCoachDiscordId2 = null
            }

            val homeOffensivePlaybook = homeTeamData.offensivePlaybook ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val awayOffensivePlaybook = awayTeamData.offensivePlaybook ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val homeDefensivePlaybook = homeTeamData.defensivePlaybook ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val awayDefensivePlaybook = awayTeamData.defensivePlaybook ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val subdivision = startRequest.subdivision ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val homePlatform = startRequest.homePlatform ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val awayPlatform = startRequest.awayPlatform ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)

            // Create and save the Game object and Stats object
            val newGame =
                gameRepository.save(
                    Game(
                        homeTeam = homeTeam,
                        awayTeam = awayTeam,
                        homeCoach1 = homeCoachUsername1,
                        homeCoach2 = homeCoachUsername2,
                        awayCoach1 = awayCoachUsername1,
                        awayCoach2 = awayCoachUsername2,
                        homeCoachDiscordId1 = homeCoachDiscordId1,
                        homeCoachDiscordId2 = homeCoachDiscordId2,
                        awayCoachDiscordId1 = awayCoachDiscordId1,
                        awayCoachDiscordId2 = awayCoachDiscordId2,
                        homeOffensivePlaybook = homeOffensivePlaybook,
                        awayOffensivePlaybook = awayOffensivePlaybook,
                        homeDefensivePlaybook = homeDefensivePlaybook,
                        awayDefensivePlaybook = awayDefensivePlaybook,
                        homeScore = 0,
                        awayScore = 0,
                        possession = TeamSide.HOME,
                        quarter = 1,
                        clock = "7:00",
                        ballLocation = 35,
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
                        season = startRequest.season?.toInt(),
                        week = startRequest.week?.toInt(),
                        waitingOn = TeamSide.AWAY,
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
                        gameType = startRequest.gameType,
                        clockStopped = true,
                        gameStatus = GameStatus.PREGAME,
                    ),
                ) ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)

            // Create image names
            val gameId: String = java.lang.String.valueOf(newGame.gameId)
            val scorebugName = "/images/scorebugs/${gameId}_scorebug.png"
            val winprobName = "/images/win_probability/${gameId}_win_probability.png"
            val scoreplotName = "/images/score_plot /${gameId}_score_plot.png"

            // Update the entity with the new image names
            newGame.scorebug = scorebugName
            newGame.winProbabilityPlot = winprobName
            newGame.scorePlot = scoreplotName

            // Create a new Discord thread
            if (newGame.homePlatform == Platform.DISCORD) {
                newGame.homePlatformId = discordService.startGameThread(newGame)
                    ?: run {
                        gameRepository.deleteById(newGame.gameId ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST))
                        return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
                    }
            } else if (newGame.awayPlatform == Platform.DISCORD) {
                newGame.awayPlatformId = discordService.startGameThread(newGame)
                    ?: run {
                        gameRepository.deleteById(newGame.gameId ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST))
                        return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
                    }
            }

            // Save the updated entity and create game stats
            gameStatsService.createGameStats(newGame)
            gameRepository.save(newGame)

            Logger.info("Game started: ${newGame.homeTeam} vs ${newGame.awayTeam}")
            ResponseEntity(newGame, HttpStatus.CREATED)
        } catch (e: Exception) {
            Logger.error("Error starting ${startRequest.homeTeam} vs ${startRequest.awayTeam}: " + e.message!!)
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * Run a coin toss
     * @param gameId
     * @param coinTossCall
     * @return
     */
    fun runCoinToss(
        gameId: String,
        coinTossCall: CoinTossCall,
    ): ResponseEntity<Game> {
        val game: Game =
            gameRepository.findById(gameId.toInt()).orElse(null)
                ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

        return try {
            val result = Random().nextInt(2)
            // 1 is heads, away team called tails, they lose
            val coinTossWinner =
                if (result == 1 && coinTossCall === CoinTossCall.TAILS) {
                    TeamSide.HOME
                } else {
                    TeamSide.AWAY
                }
            game.coinTossWinner = coinTossWinner

            Logger.info("Coin toss finished, the winner was $coinTossWinner")
            return ResponseEntity(gameRepository.save(game), HttpStatus.OK)
        } catch (e: Exception) {
            Logger.error("Error in ${game.gameId}: " + e.message!!)
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * Make a coin toss choice
     * @param gameId
     * @param coinTossChoice
     * @return
     */
    fun makeCoinTossChoice(
        gameId: String,
        coinTossChoice: CoinTossChoice,
    ): ResponseEntity<Game> {
        val game: Game =
            gameRepository.findById(gameId.toInt()).orElse(null)
                ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

        return try {
            game.coinTossChoice = coinTossChoice
            if (game.coinTossWinner == TeamSide.HOME && coinTossChoice == CoinTossChoice.RECEIVE) {
                game.possession = TeamSide.AWAY
                game.waitingOn = TeamSide.HOME
            } else if (game.coinTossWinner == TeamSide.HOME && coinTossChoice == CoinTossChoice.DEFER) {
                game.possession = TeamSide.HOME
                game.waitingOn = TeamSide.AWAY
            } else if (game.coinTossWinner == TeamSide.AWAY && coinTossChoice == CoinTossChoice.RECEIVE) {
                game.possession = TeamSide.HOME
                game.waitingOn = TeamSide.AWAY
            } else if (game.coinTossWinner == TeamSide.AWAY && coinTossChoice == CoinTossChoice.DEFER) {
                game.possession = TeamSide.AWAY
                game.waitingOn = TeamSide.HOME
            }
            game.gameStatus = GameStatus.OPENING_KICKOFF
            Logger.info("Coin toss choice made for ${game.gameId}: $coinTossChoice")
            return ResponseEntity(gameRepository.save(game), HttpStatus.OK)
        } catch (e: Exception) {
            Logger.error("Error in ${game.gameId}: " + e.message!!)
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * Deletes an ongoing game
     * @param id
     * @return
     */
    fun deleteOngoingGame(id: Int): ResponseEntity<HttpStatus> {
        gameRepository.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!gameRepository.findById(id).isPresent) {
            Logger.error("No game found with id $id to delete")
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        gameRepository.deleteById(id)
        Logger.info("Game $id deleted")
        return ResponseEntity(HttpStatus.OK)
    }
}
