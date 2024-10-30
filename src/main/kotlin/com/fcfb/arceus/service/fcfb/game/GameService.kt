package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.CoinTossCall
import com.fcfb.arceus.domain.Game.CoinTossChoice
import com.fcfb.arceus.domain.Game.GameStatus
import com.fcfb.arceus.domain.Game.GameType
import com.fcfb.arceus.domain.Game.Platform
import com.fcfb.arceus.domain.Game.PlayType
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.models.requests.StartRequest
import com.fcfb.arceus.repositories.GameRepository
import com.fcfb.arceus.service.discord.DiscordService
import com.fcfb.arceus.service.fcfb.SeasonService
import com.fcfb.arceus.service.fcfb.TeamService
import com.fcfb.arceus.utils.Logger
import com.fcfb.arceus.utils.UnableToCreateGameThreadException
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Random

@Component
class GameService(
    private val gameRepository: GameRepository,
    private val teamService: TeamService,
    private val discordService: DiscordService,
    private val gameStatsService: GameStatsService,
    private val seasonService: SeasonService,
) {
    /**
     * Get an ongoing game by id
     * @param id
     * @return
     */
    fun getGameById(id: Int) = gameRepository.getGameById(id)

    /**
     * Save a game state
     */
    fun saveGame(game: Game) = gameRepository.save(game)

    /**
     * Get an ongoing game by platform id
     * @param channelId
     * @return
     */
    fun getOngoingGameByDiscordChannelId(channelId: String?) =
        gameRepository.getOngoingGameByHomePlatformId(Platform.DISCORD.description, channelId)
            ?: gameRepository.getOngoingGameByAwayPlatformId(Platform.DISCORD.description, channelId)

    /**
     * Get an ongoing game by Discord user id
     * @param discordId
     * @return
     */
    fun getOngoingGameByDiscordId(discordId: String?) =
        gameRepository.getOngoingGameByHomeCoachDiscordId1(discordId)
            ?: gameRepository.getOngoingGameByHomeCoachDiscordId2(discordId)
            ?: gameRepository.getOngoingGameByAwayCoachDiscordId1(discordId)
            ?: gameRepository.getOngoingGameByAwayCoachDiscordId2(discordId)

    /**
     * Start a game
     * @param startRequest
     * @return
     */
    fun startGame(startRequest: StartRequest): Game {
        try {
            val homeTeamData = teamService.getTeamByName(startRequest.homeTeam)
            val awayTeamData = teamService.getTeamByName(startRequest.awayTeam)

            val formattedDateTime = calculateDelayOfGameTimer()

            // Validate request fields
            val homeTeam = startRequest.homeTeam
            val awayTeam = startRequest.awayTeam

            // If the second coach username is not null, there is a coordinator
            val homeCoachUsername1: String
            val homeCoachDiscordId1: String
            val awayCoachUsername1: String
            val awayCoachDiscordId1: String
            val homeCoachDiscordId2: String?
            val homeCoachUsername2: String?
            val awayCoachUsername2: String?
            val awayCoachDiscordId2: String?
            if (homeTeamData.coachUsername2 != null) {
                homeCoachUsername1 = homeTeamData.coachUsername1
                homeCoachUsername2 = homeTeamData.coachUsername2
                homeCoachDiscordId1 = homeTeamData.coachDiscordId1
                homeCoachDiscordId2 = homeTeamData.coachDiscordId2
            } else {
                homeCoachUsername1 = homeTeamData.coachUsername1
                homeCoachUsername2 = null
                homeCoachDiscordId1 = homeTeamData.coachDiscordId1
                homeCoachDiscordId2 = null
            }

            if (awayTeamData.coachUsername2 != null) {
                awayCoachUsername1 = awayTeamData.coachUsername1
                awayCoachUsername2 = awayTeamData.coachUsername2
                awayCoachDiscordId1 = awayTeamData.coachDiscordId1
                awayCoachDiscordId2 = awayTeamData.coachDiscordId2
            } else {
                awayCoachUsername1 = awayTeamData.coachUsername1
                awayCoachUsername2 = null
                awayCoachDiscordId1 = awayTeamData.coachDiscordId1
                awayCoachDiscordId2 = null
            }

            val homeOffensivePlaybook = homeTeamData.offensivePlaybook
            val awayOffensivePlaybook = awayTeamData.offensivePlaybook
            val homeDefensivePlaybook = homeTeamData.defensivePlaybook
            val awayDefensivePlaybook = awayTeamData.defensivePlaybook
            val subdivision = startRequest.subdivision
            val homePlatform = startRequest.homePlatform
            val awayPlatform = startRequest.awayPlatform

            val (season, week) =
                if (startRequest.gameType != GameType.SCRIMMAGE) {
                    seasonService.getCurrentSeason().seasonNumber to seasonService.getCurrentSeason().currentWeek
                } else {
                    null to null
                }

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
                        subdivision = subdivision,
                        timestamp = LocalDateTime.now().toString(),
                        winProbability = 0.0,
                        season = season,
                        week = week,
                        waitingOn = TeamSide.AWAY,
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
                )

            // Create a new Discord thread
            if (newGame.homePlatform == Platform.DISCORD) {
                newGame.homePlatformId = discordService.startGameThread(newGame)
                    ?: run {
                        deleteOngoingGame(newGame.gameId)
                        throw UnableToCreateGameThreadException()
                    }
            } else if (newGame.awayPlatform == Platform.DISCORD) {
                newGame.awayPlatformId = discordService.startGameThread(newGame)
                    ?: run {
                        deleteOngoingGame(newGame.gameId)
                        throw UnableToCreateGameThreadException()
                    }
            }

            // Save the updated entity and create game stats
            gameStatsService.createGameStats(newGame)
            gameRepository.save(newGame)

            Logger.info("Game started: ${newGame.homeTeam} vs ${newGame.awayTeam}")
            return newGame
        } catch (e: Exception) {
            Logger.error("Error starting ${startRequest.homeTeam} vs ${startRequest.awayTeam}: " + e.message!!)
            throw e
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
    ): Game {
        val game = getGameById(gameId.toInt())

        try {
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
            saveGame(game)
            return game
        } catch (e: Exception) {
            Logger.error("Error in ${game.gameId}: " + e.message!!)
            throw e
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
    ): Game {
        val game = getGameById(gameId.toInt())

        try {
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
            return saveGame(game)
        } catch (e: Exception) {
            Logger.error("Error in ${game.gameId}: " + e.message!!)
            throw e
        }
    }

    /**
     * Deletes an ongoing game
     * @param id
     * @return
     */
    fun deleteOngoingGame(id: Int): Boolean {
        gameRepository.findById(id) ?: return false
        if (!gameRepository.findById(id).isPresent) {
            Logger.error("No game found with id $id to delete")
            return false
        }
        gameRepository.deleteById(id)
        Logger.info("Game $id deleted")
        return true
    }

    /**
     * Calculate the delay of game timer
     */
    fun calculateDelayOfGameTimer(): String? {
        // Set the DOG timer
        // Get the current date and time
        val now = ZonedDateTime.now(ZoneId.of("America/New_York"))

        // Add 24 hours to the current date and time
        val futureTime = now.plusHours(24)

        // Define the desired date and time format
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")

        // Format the result and set it on the game
        return futureTime.format(formatter)
    }
}
