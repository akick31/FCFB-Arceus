package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.CoinTossCall
import com.fcfb.arceus.domain.Game.CoinTossChoice
import com.fcfb.arceus.domain.Game.GameMode
import com.fcfb.arceus.domain.Game.GameStatus
import com.fcfb.arceus.domain.Game.GameType
import com.fcfb.arceus.domain.Game.OvertimeCoinTossChoice
import com.fcfb.arceus.domain.Game.Platform
import com.fcfb.arceus.domain.Game.PlayType
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.models.requests.StartRequest
import com.fcfb.arceus.repositories.GameRepository
import com.fcfb.arceus.repositories.PlayRepository
import com.fcfb.arceus.service.discord.DiscordService
import com.fcfb.arceus.service.fcfb.SeasonService
import com.fcfb.arceus.service.fcfb.TeamService
import com.fcfb.arceus.service.fcfb.UserService
import com.fcfb.arceus.utils.Logger
import com.fcfb.arceus.utils.NoCoachDiscordIdsFoundException
import com.fcfb.arceus.utils.NoCoachesFoundException
import com.fcfb.arceus.utils.TeamNotFoundException
import com.fcfb.arceus.utils.UnableToCreateGameThreadException
import com.fcfb.arceus.utils.UnableToDeleteGameException
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Random

@Component
class GameService(
    private val gameRepository: GameRepository,
    private val playRepository: PlayRepository,
    private val teamService: TeamService,
    private val discordService: DiscordService,
    private val userService: UserService,
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

            val homeCoachUsernames = homeTeamData.coachUsernames ?: throw NoCoachesFoundException()
            val awayCoachUsernames = awayTeamData.coachUsernames ?: throw NoCoachesFoundException()
            val homeCoachDiscordIds = homeTeamData.coachDiscordIds ?: throw NoCoachDiscordIdsFoundException()
            val awayCoachDiscordIds = awayTeamData.coachDiscordIds ?: throw NoCoachDiscordIdsFoundException()

            val homeOffensivePlaybook = homeTeamData.offensivePlaybook
            val awayOffensivePlaybook = awayTeamData.offensivePlaybook
            val homeDefensivePlaybook = homeTeamData.defensivePlaybook
            val awayDefensivePlaybook = awayTeamData.defensivePlaybook
            val subdivision = startRequest.subdivision
            val homePlatform = Platform.DISCORD
            val awayPlatform = Platform.DISCORD

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
                        homeCoaches = homeCoachUsernames,
                        awayCoaches = awayCoachUsernames,
                        homeCoachDiscordIds = homeCoachDiscordIds,
                        awayCoachDiscordIds = awayCoachDiscordIds,
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
                        overtimeCoinTossWinner = null,
                        overtimeCoinTossChoice = null,
                        homePlatform = homePlatform,
                        homePlatformId = null,
                        awayPlatform = awayPlatform,
                        awayPlatformId = null,
                        lastMessageTimestamp = null,
                        gameTimer = formattedDateTime,
                        gameWarned = false,
                        currentPlayType = PlayType.KICKOFF,
                        currentPlayId = 0,
                        clockStopped = true,
                        requestMessageId = null,
                        gameType = startRequest.gameType,
                        gameStatus = GameStatus.PREGAME,
                        gameMode = GameMode.NORMAL,
                        overtimeHalf = 0,
                    ),
                )

            // Create a new Discord thread
            val discordData =
                discordService.startGameThread(newGame)
                    ?: run {
                        deleteOngoingGame(
                            newGame.homePlatformId?.toULong() ?: newGame.awayPlatformId?.toULong()
                                ?: throw UnableToDeleteGameException(),
                        )
                        throw UnableToCreateGameThreadException()
                    }

            if (discordData[0] == "null") {
                deleteOngoingGame(
                    newGame.homePlatformId?.toULong() ?: newGame.awayPlatformId?.toULong()
                        ?: throw UnableToDeleteGameException(),
                )
                throw UnableToCreateGameThreadException()
            }

            newGame.homePlatformId = discordData[0]
            newGame.awayPlatformId = discordData[0]
            newGame.requestMessageId = listOf(discordData[1])

            // Save the updated entity and create game stats
            gameRepository.save(newGame)
            gameStatsService.createGameStats(newGame)

            Logger.info("Game started: ${newGame.homeTeam} vs ${newGame.awayTeam}")
            return newGame
        } catch (e: Exception) {
            Logger.error("Error starting ${startRequest.homeTeam} vs ${startRequest.awayTeam}: " + e.message!!)
            throw e
        }
    }

    /**
     * End all ongoing games
     */
    fun endAllGames(): List<Game> {
        val gamesToEnd = getAllOngoingGames()
        val endedGames = mutableListOf<Game>()
        for (game in gamesToEnd) {
            endedGames.add(endGame(game))
        }
        return endedGames
    }

    /**
     * End a single game
     * @param channelId
     * @return
     */
    fun endSingleGame(channelId: ULong): Game {
        val game =
            getGameByPlatformId(channelId) ?: run {
                Logger.error("Game at $channelId not found")
                throw Exception("Game not found")
            }
        return endGame(game)
    }

    /**
     * End a game
     * @param game
     * @return
     */
    private fun endGame(game: Game): Game {
        try {
            game.gameStatus = GameStatus.FINAL
            if (game.gameType != GameType.SCRIMMAGE) {
                teamService.updateTeamWinsAndLosses(game)
                userService.updateUserWinsAndLosses(game)
            }
            if (game.gameType == GameType.NATIONAL_CHAMPIONSHIP) {
                seasonService.endSeason(game)
            }
            saveGame(game)
            Logger.info("Game ${game.gameId} ended")
            return game
        } catch (e: Exception) {
            Logger.error("Error in ${game.gameId}: " + e.message!!)
            throw e
        }
    }

    /**
     * Chew a game
     * @param channelId
     * @return
     */
    fun chewGame(channelId: ULong): Game {
        val game =
            getGameByPlatformId(channelId) ?: run {
                Logger.error("Game at $channelId not found")
                throw Exception("Game not found")
            }

        try {
            game.gameMode = GameMode.CHEW
            saveGame(game)
            Logger.info("Game ${game.gameId} is being chewed")
            return game
        } catch (e: Exception) {
            Logger.error("Error in ${game.gameId}: " + e.message!!)
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
            if (game.gameStatus == GameStatus.PREGAME) {
                game.coinTossWinner = coinTossWinner
            } else if (game.gameStatus == GameStatus.END_OF_REGULATION) {
                game.overtimeCoinTossWinner = coinTossWinner
            }

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
     * Make a coin toss choice
     * @param gameId
     * @param coinTossChoice
     * @return
     */
    fun makeOvertimeCoinTossChoice(
        gameId: String,
        coinTossChoice: OvertimeCoinTossChoice,
    ): Game {
        val game = getGameById(gameId.toInt())

        try {
            game.overtimeCoinTossChoice = coinTossChoice
            if (game.coinTossWinner == TeamSide.HOME && coinTossChoice == OvertimeCoinTossChoice.DEFENSE) {
                game.possession = TeamSide.AWAY
                game.waitingOn = TeamSide.HOME
            } else if (game.coinTossWinner == TeamSide.HOME && coinTossChoice == OvertimeCoinTossChoice.OFFENSE) {
                game.possession = TeamSide.HOME
                game.waitingOn = TeamSide.AWAY
            } else if (game.coinTossWinner == TeamSide.AWAY && coinTossChoice == OvertimeCoinTossChoice.DEFENSE) {
                game.possession = TeamSide.HOME
                game.waitingOn = TeamSide.AWAY
            } else if (game.coinTossWinner == TeamSide.AWAY && coinTossChoice == OvertimeCoinTossChoice.OFFENSE) {
                game.possession = TeamSide.AWAY
                game.waitingOn = TeamSide.HOME
            }
            game.gameStatus = GameStatus.OVERTIME
            Logger.info("Coin toss choice made for ${game.gameId}: $coinTossChoice")
            return saveGame(game)
        } catch (e: Exception) {
            Logger.error("Error in ${game.gameId}: " + e.message!!)
            throw e
        }
    }

    /**
     * Deletes an ongoing game
     * @param channelId
     * @return
     */
    fun deleteOngoingGame(channelId: ULong): Boolean {
        val game =
            getGameByPlatformId(channelId) ?: run {
                Logger.error("Game at $channelId not found")
                return false
            }
        val id = game.gameId
        gameRepository.deleteById(id)
        gameStatsService.deleteByGameId(id)
        playRepository.deleteAllPlaysByGameId(id)
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
        val futureTime = now.plusHours(18)

        // Define the desired date and time format
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")

        // Format the result and set it on the game
        return futureTime.format(formatter)
    }

    /**
     * Update the request message id
     * @param gameId
     * @param requestMessageId
     */
    fun updateRequestMessageId(
        gameId: Int,
        requestMessageId: String,
    ) {
        val game = getGameById(gameId)

        val requestMessageIdList =
            if (requestMessageId.contains(",")) {
                listOf(requestMessageId.split(",")[0], requestMessageId.split(",")[1])
            } else {
                listOf(requestMessageId)
            }
        game.requestMessageId = requestMessageIdList
        saveGame(game)
    }

    /**
     * Update the last message timestamp
     * @param gameId
     * @param timestamp
     */
    fun updateLastMessageTimestamp(gameId: Int) {
        val game = getGameById(gameId)
        val timestamp = Timestamp.from(Instant.now()).toString()
        game.lastMessageTimestamp = timestamp
        saveGame(game)
    }

    /**
     * Get the game by request message id
     * @param requestMessageId
     */
    fun getGameByRequestMessageId(requestMessageId: String) = gameRepository.getGameByRequestMessageId(requestMessageId)

    /**
     * Get the game by platform id
     * @param platformId
     */
    fun getGameByPlatformId(platformId: ULong) =
        gameRepository.getGameByHomePlatformId(platformId) ?: gameRepository.getGameByAwayPlatformId(platformId)

    /**
     * Sub a coach in for a team
     * @param gameId
     */
    fun subCoach(
        gameId: Int,
        team: String,
        coachId: String,
    ): Game {
        val game = getGameById(gameId)
        val userData = userService.getUserDTOByDiscordId(coachId)
        val coach = userData.coachName
        val updatedTeam = team.replace("_", " ")

        when (updatedTeam) {
            game.homeTeam -> {
                game.homeCoaches = listOf(coach)
                game.homeCoachDiscordIds = listOf(userData.discordId ?: throw NoCoachDiscordIdsFoundException())
            }
            game.awayTeam -> {
                game.awayCoaches = listOf(coach)
                game.awayCoachDiscordIds = listOf(userData.discordId ?: throw NoCoachDiscordIdsFoundException())
            }
            else -> {
                throw TeamNotFoundException()
            }
        }
        saveGame(game)
        return game
    }

    /**
     * Get all past games
     */
    fun getAllPastGames() = gameRepository.getAllPastGames()

    /**
     * Get all past scrimmage games
     */
    fun getAllPastScrimmageGames() = gameRepository.getAllPastScrimmageGames()

    /**
     * Get all ongoing scrimmage games
     */
    fun getAllOngoingScrimmageGames() = gameRepository.getAllOngoingScrimmageGames()

    /**
     * Find expired timers
     */
    fun findExpiredTimers() = gameRepository.findExpiredTimers()

    /**
     * Find games to warn
     */
    fun findGamesToWarn() = gameRepository.findGamesToWarn()

    /**
     * Update a game as warned
     * @param gameId
     */
    fun updateGameAsWarned(gameId: Int) = gameRepository.updateGameAsWarned(gameId)

    /**
     * Get all ongoing games
     */
    fun getAllOngoingGames() = gameRepository.getAllOngoingGames()
}
