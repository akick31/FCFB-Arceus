package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.ActualResult
import com.fcfb.arceus.domain.Game.CoinTossCall
import com.fcfb.arceus.domain.Game.CoinTossChoice
import com.fcfb.arceus.domain.Game.GameMode
import com.fcfb.arceus.domain.Game.GameStatus
import com.fcfb.arceus.domain.Game.GameType
import com.fcfb.arceus.domain.Game.OvertimeCoinTossChoice
import com.fcfb.arceus.domain.Game.Platform
import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.PlayType
import com.fcfb.arceus.domain.Game.Scenario
import com.fcfb.arceus.domain.Game.Subdivision
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.domain.Team
import com.fcfb.arceus.models.requests.StartRequest
import com.fcfb.arceus.repositories.GameRepository
import com.fcfb.arceus.repositories.PlayRepository
import com.fcfb.arceus.service.GameSpecificationService
import com.fcfb.arceus.service.GameSpecificationService.GameCategory
import com.fcfb.arceus.service.GameSpecificationService.GameFilter
import com.fcfb.arceus.service.GameSpecificationService.GameSort
import com.fcfb.arceus.service.discord.DiscordService
import com.fcfb.arceus.service.fcfb.SeasonService
import com.fcfb.arceus.service.fcfb.TeamService
import com.fcfb.arceus.service.fcfb.UserService
import com.fcfb.arceus.utils.GameNotFoundException
import com.fcfb.arceus.utils.InvalidHalfTimePossessionChangeException
import com.fcfb.arceus.utils.Logger
import com.fcfb.arceus.utils.NoCoachDiscordIdsFoundException
import com.fcfb.arceus.utils.NoCoachesFoundException
import com.fcfb.arceus.utils.NoGameFoundException
import com.fcfb.arceus.utils.TeamNotFoundException
import com.fcfb.arceus.utils.UnableToCreateGameThreadException
import com.fcfb.arceus.utils.UnableToDeleteGameException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import java.lang.Thread.sleep
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Random
import kotlin.math.abs

@Component
class GameService(
    private val gameRepository: GameRepository,
    private val playRepository: PlayRepository,
    private val teamService: TeamService,
    private val discordService: DiscordService,
    private val userService: UserService,
    private val gameStatsService: GameStatsService,
    private val seasonService: SeasonService,
    private val scheduleService: ScheduleService,
    private val gameSpecificationService: GameSpecificationService,
) {
    /**
     * Save a game state
     */
    fun saveGame(game: Game): Game = gameRepository.save(game)

    fun startSingleGame(
        startRequest: StartRequest,
        week: Int?,
    ): Game {
        val game = startGame(startRequest, week)
        if (startRequest.gameType != GameType.SCRIMMAGE) {
            scheduleService.markManuallyStartedGameAsStarted(game)
        }
        return game
    }

    /**
     * Start a game
     * @param startRequest
     * @return
     */
    private fun startGame(
        startRequest: StartRequest,
        week: Int?,
    ): Game {
        try {
            val homeTeamData = teamService.getTeamByName(startRequest.homeTeam)
            val awayTeamData = teamService.getTeamByName(startRequest.awayTeam)

            val formattedDateTime = calculateDelayOfGameTimer()

            // Validate request fields
            val homeTeam = homeTeamData.name ?: throw TeamNotFoundException("Home team not found")
            val awayTeam = awayTeamData.name ?: throw TeamNotFoundException("Away team not found")

            val homeCoachUsernames = homeTeamData.coachUsernames ?: throw NoCoachesFoundException()
            val awayCoachUsernames = awayTeamData.coachUsernames ?: throw NoCoachesFoundException()
            val homeCoachDiscordIds = homeTeamData.coachDiscordIds ?: throw NoCoachDiscordIdsFoundException()
            val awayCoachDiscordIds = awayTeamData.coachDiscordIds ?: throw NoCoachDiscordIdsFoundException()

            if (homeCoachUsernames.isEmpty() || awayCoachUsernames.isEmpty()
            ) {
                throw NoCoachesFoundException()
            }

            if (homeCoachDiscordIds.isEmpty() || awayCoachDiscordIds.isEmpty()
            ) {
                throw NoCoachDiscordIdsFoundException()
            }

            val homeOffensivePlaybook = homeTeamData.offensivePlaybook
            val awayOffensivePlaybook = awayTeamData.offensivePlaybook
            val homeDefensivePlaybook = homeTeamData.defensivePlaybook
            val awayDefensivePlaybook = awayTeamData.defensivePlaybook
            val subdivision = startRequest.subdivision
            val homePlatform = Platform.DISCORD
            val awayPlatform = Platform.DISCORD

            var (season, currentWeek) =
                if (startRequest.gameType != GameType.SCRIMMAGE) {
                    seasonService.getCurrentSeason().seasonNumber to seasonService.getCurrentSeason().currentWeek
                } else {
                    null to null
                }

            if (week != null) {
                currentWeek = week
            }

            val homeTeamRank =
                if (homeTeamData.playoffCommitteeRanking != null && homeTeamData.playoffCommitteeRanking != 0) {
                    homeTeamData.playoffCommitteeRanking
                } else {
                    homeTeamData.coachesPollRanking ?: 0
                }

            val awayTeamRank =
                if (awayTeamData.playoffCommitteeRanking != null && awayTeamData.playoffCommitteeRanking != 0) {
                    awayTeamData.playoffCommitteeRanking
                } else {
                    awayTeamData.coachesPollRanking ?: 0
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
                        homeTeamRank = homeTeamRank,
                        homeWins = homeTeamData.currentWins,
                        homeLosses = homeTeamData.currentLosses,
                        awayTeamRank = awayTeamRank,
                        awayWins = awayTeamData.currentWins,
                        awayLosses = awayTeamData.currentLosses,
                        subdivision = subdivision,
                        timestamp = LocalDateTime.now().toString(),
                        winProbability = 0.0,
                        season = season,
                        week = currentWeek,
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
                        closeGame = false,
                        closeGamePinged = false,
                        upsetAlert = false,
                        upsetAlertPinged = false,
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
     * Update game information based on the result of a play
     * @param game
     * @param play
     * @param homeScore
     * @param awayScore
     * @param possession
     * @param quarter
     * @param clock
     * @param ballLocation
     * @param down
     * @param yardsToGo
     * @param homeTimeoutCalled
     * @param awayTimeoutCalled
     * @param timeoutUsed
     */
    fun updateGameInformation(
        game: Game,
        play: Play,
        homeScore: Int,
        awayScore: Int,
        possession: TeamSide,
        quarter: Int,
        clock: Int,
        ballLocation: Int,
        down: Int,
        yardsToGo: Int,
        homeTimeoutCalled: Boolean,
        awayTimeoutCalled: Boolean,
        timeoutUsed: Boolean,
    ): Game {
        val waitingOn = updateWaitingOn(possession)
        updateClockStopped(game, play, clock)
        updateTimeouts(game, homeTimeoutCalled, awayTimeoutCalled, timeoutUsed)
        updatePlayType(game, play)
        updateCloseGame(game, play)
        updateUpsetAlert(game, play)

        // Update the quarter/overtime stuff
        if (quarter == 0) {
            updateEndOfRegulationGameValues(game)
        } else if (game.gameStatus == GameStatus.OVERTIME) {
            updateOvertimeValues(game, play, possession, waitingOn, ballLocation, down, yardsToGo, homeScore, awayScore)
        } else if (game.gameStatus == GameStatus.OPENING_KICKOFF) {
            updateStartGameValues(game, clock, possession, quarter, ballLocation, down, yardsToGo, waitingOn)
        } else if (quarter == 3 && clock == 420 && game.gameStatus != GameStatus.HALFTIME) {
            updateHalftimeValues(game, possession, waitingOn, clock)
        } else if (game.gameStatus == GameStatus.HALFTIME) {
            updateStartOfHalfValues(game, clock, possession, quarter, ballLocation, down, yardsToGo, waitingOn)
        } else if (quarter >= 5 && game.gameStatus == GameStatus.IN_PROGRESS) {
            updateStartOfOvertimeValues(game, quarter)
        } else {
            updateNormalPlayValues(game, clock, possession, quarter, ballLocation, down, yardsToGo, waitingOn)
        }

        // Update everything else
        game.homeScore = homeScore
        game.awayScore = awayScore
        game.winProbability = play.winProbability
        game.numPlays = play.playNumber
        game.gameTimer = calculateDelayOfGameTimer()
        game.gameWarned = false

        gameRepository.save(game)

        if (game.gameStatus == GameStatus.FINAL) {
            endGame(game)
        }

        return game
    }

    /**
     * Update the game information based on a defensive number submission
     * @param game
     * @param gamePlay
     */
    fun updateWithDefensiveNumberSubmission(
        game: Game,
        gamePlay: Play,
    ) {
        game.currentPlayId = gamePlay.playId
        game.waitingOn = game.possession
        game.gameTimer = calculateDelayOfGameTimer()
        gameRepository.save(game)
    }

    /**
     * Rollback the play to the previous play
     * @param game
     * @param previousPlay
     * @param gamePlay
     */
    fun rollbackPlay(
        game: Game,
        previousPlay: Play,
        gamePlay: Play,
    ) {
        try {
            if (gamePlay.actualResult == ActualResult.DELAY_OF_GAME) {
                if (game.waitingOn == TeamSide.HOME) {
                    game.awayScore -= 8
                    if (game.gameType != Game.GameType.SCRIMMAGE) {
                        val user = userService.getUserByDiscordId(game.homeCoachDiscordIds?.get(0) ?: "")
                        user.delayOfGameInstances -= 1
                        userService.saveUser(user)
                    }
                } else {
                    game.homeScore -= 8
                    if (game.gameType != Game.GameType.SCRIMMAGE) {
                        val user = userService.getUserByDiscordId(game.awayCoachDiscordIds?.get(0) ?: "")
                        user.delayOfGameInstances -= 1
                        userService.saveUser(user)
                    }
                }
                when (previousPlay.playCall) {
                    PlayCall.KICKOFF_NORMAL, PlayCall.KICKOFF_ONSIDE, PlayCall.KICKOFF_SQUIB -> {
                        game.currentPlayType = PlayType.KICKOFF
                    }
                    PlayCall.PAT, PlayCall.TWO_POINT -> {
                        game.currentPlayType = PlayType.PAT
                    }
                    else -> {
                        game.currentPlayType = PlayType.NORMAL
                    }
                }
            }
            if (isTouchdownPlay(gamePlay.actualResult)) {
                if (gamePlay.possession == TeamSide.HOME) {
                    game.homeScore -= 6
                } else {
                    game.awayScore -= 6
                }
                game.currentPlayType = PlayType.NORMAL
            }
            if (gamePlay.actualResult == ActualResult.GOOD) {
                if (gamePlay.playCall == PlayCall.PAT) {
                    if (gamePlay.possession == TeamSide.HOME) {
                        game.homeScore -= 1
                    } else {
                        game.awayScore -= 1
                    }
                    game.currentPlayType = PlayType.PAT
                } else if (gamePlay.playCall == PlayCall.FIELD_GOAL) {
                    if (gamePlay.possession == TeamSide.HOME) {
                        game.homeScore -= 3
                    } else {
                        game.awayScore -= 3
                    }
                    game.currentPlayType = PlayType.NORMAL
                }
            }
            if (gamePlay.actualResult == ActualResult.SUCCESS) {
                if (gamePlay.possession == TeamSide.HOME) {
                    game.homeScore -= 2
                } else {
                    game.awayScore -= 2
                }
                game.currentPlayType = PlayType.PAT
            }
            if (gamePlay.actualResult == ActualResult.DEFENSE_TWO_POINT) {
                if (gamePlay.possession == TeamSide.HOME) {
                    game.awayScore -= 2
                } else {
                    game.homeScore -= 2
                }
                game.currentPlayType = PlayType.PAT
            }
            if (gamePlay.actualResult == ActualResult.SAFETY) {
                if (gamePlay.possession == TeamSide.HOME) {
                    game.awayScore -= 2
                } else {
                    game.homeScore -= 2
                }
                game.currentPlayType = PlayType.NORMAL
            }
            if (previousPlay.playCall == PlayCall.KICKOFF_NORMAL ||
                previousPlay.playCall == PlayCall.KICKOFF_ONSIDE ||
                previousPlay.playCall == PlayCall.KICKOFF_SQUIB
            ) {
                game.currentPlayType = PlayType.KICKOFF
            }
            if (gamePlay.offensiveTimeoutCalled) {
                if (gamePlay.possession == TeamSide.HOME) {
                    game.homeTimeouts--
                } else {
                    game.awayTimeouts--
                }
            }
            if (game.gameStatus == GameStatus.FINAL) {
                if (game.quarter <= 4) {
                    game.gameStatus = GameStatus.IN_PROGRESS
                } else {
                    game.gameStatus = GameStatus.OVERTIME
                }
            }

            game.possession = previousPlay.possession
            game.quarter = previousPlay.quarter
            game.clock = convertClockToString(previousPlay.clock)
            game.ballLocation = previousPlay.ballLocation
            game.down = previousPlay.down
            game.yardsToGo = previousPlay.yardsToGo
            game.currentPlayId = previousPlay.playId
            game.waitingOn = if (previousPlay.possession == TeamSide.HOME) TeamSide.AWAY else TeamSide.HOME
            game.gameTimer = calculateDelayOfGameTimer()
            gameStatsService.generateGameStats(game.gameId)
            saveGame(game)
        } catch (e: Exception) {
            Logger.error("There was an error rolling back the play for game ${game.gameId}: " + e.message)
            throw e
        }
    }

    /**
     * Start all games for the given week
     * @param season
     * @param week
     * @return
     */
    fun startWeek(
        season: Int,
        week: Int,
    ): List<Game> {
        val gamesToStart =
            scheduleService.getGamesToStartBySeasonAndWeek(season, week) ?: run {
                Logger.error("No games found for season $season week $week")
                throw NoGameFoundException()
            }
        val startedGames = mutableListOf<Game>()
        var count = 0
        for (game in gamesToStart) {
            try {
                if (count >= 30) {
                    sleep(300000)
                    count = 0
                    Logger.info("Block of 30 games started, sleeping for 5 minutes")
                }
                val startedGame =
                    startGame(
                        StartRequest(
                            Platform.DISCORD,
                            Platform.DISCORD,
                            game.subdivision,
                            game.homeTeam,
                            game.awayTeam,
                            game.tvChannel,
                            game.gameType,
                        ),
                        week,
                    )
                startedGames.add(startedGame)
                scheduleService.markGameAsStarted(game)
                count += 1
            } catch (e: Exception) {
                Logger.error("Error starting ${game.homeTeam} vs ${game.awayTeam}: " + e.message!!)
                continue
            }
        }
        return startedGames
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
        val game = getGameByPlatformId(channelId)
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

                val homeUsers = userService.getUsersByTeam(game.homeTeam)
                val awayUsers = userService.getUsersByTeam(game.awayTeam)
                for (user in homeUsers + awayUsers) {
                    val responseTime =
                        playRepository.getUserAverageResponseTime(
                            user.discordTag,
                            seasonService.getCurrentSeason().seasonNumber,
                        ) ?: throw Exception("Could not get average response time for user ${user.username}")
                    userService.updateUserAverageResponseTime(user.id, responseTime)
                }

                scheduleService.markGameAsFinished(game)
            }
            if (game.gameType == GameType.NATIONAL_CHAMPIONSHIP) {
                seasonService.endSeason(game)
            }
            saveGame(game)

            val homeStats = gameStatsService.getGameStatsByIdAndTeam(game.gameId, game.homeTeam)
            val awayStats = gameStatsService.getGameStatsByIdAndTeam(game.gameId, game.awayTeam)
            homeStats.gameStatus = GameStatus.FINAL
            awayStats.gameStatus = GameStatus.FINAL
            gameStatsService.saveGameStats(homeStats)
            gameStatsService.saveGameStats(awayStats)
            Logger.info("Game ${game.gameId} ended")
            return game
        } catch (e: Exception) {
            Logger.error("Error in ${game.gameId}: " + e.message!!)
            throw e
        }
    }

    /**
     * End the overtime period and advance to the next one
     * @param game
     * @param possession
     */
    private fun endOvertimePeriod(
        game: Game,
        possession: TeamSide,
    ) {
        game.overtimeHalf = 1
        game.possession =
            if (game.possession == TeamSide.HOME) {
                TeamSide.HOME
            } else {
                TeamSide.AWAY
            }
        game.ballLocation = 75
        game.down = 1
        game.yardsToGo = 10
        game.quarter += 1
        game.homeTimeouts = 1
        game.awayTimeouts = 1
        game.waitingOn = if (possession == TeamSide.HOME) TeamSide.AWAY else TeamSide.HOME
    }

    /**
     * Chew a game
     * @param channelId
     * @return
     */
    fun chewGame(channelId: ULong): Game {
        val game = getGameByPlatformId(channelId)

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
            val coinTossWinner =
                if (
                    (result == 1 && coinTossCall == CoinTossCall.HEADS) ||
                    (result == 0 && coinTossCall == CoinTossCall.TAILS)
                ) {
                    TeamSide.AWAY
                } else {
                    TeamSide.HOME
                }
            if (game.gameStatus == GameStatus.PREGAME) {
                game.coinTossWinner = coinTossWinner
            } else if (game.gameStatus == GameStatus.END_OF_REGULATION) {
                game.overtimeCoinTossWinner = coinTossWinner
            }
            game.gameTimer = calculateDelayOfGameTimer()
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
            game.gameTimer = calculateDelayOfGameTimer()
            Logger.info("Coin toss choice made for ${game.gameId}: $coinTossChoice")
            return saveGame(game)
        } catch (e: Exception) {
            Logger.error("Error in ${game.gameId}: " + e.message!!)
            throw e
        }
    }

    /**
     * Make an overtime coin toss choice
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
     * Restart a game
     * @param channelId
     * @return
     */
    fun restartGame(channelId: ULong): Game {
        val game = getGameByPlatformId(channelId)
        deleteOngoingGame(channelId)
        val startRequest =
            StartRequest(
                Platform.DISCORD,
                Platform.DISCORD,
                game.subdivision ?: Subdivision.FCFB,
                game.homeTeam,
                game.awayTeam,
                game.tvChannel,
                game.gameType ?: GameType.SCRIMMAGE,
            )
        return startGame(startRequest, game.week)
    }

    /**
     * Deletes an ongoing game
     * @param channelId
     * @return
     */
    fun deleteOngoingGame(channelId: ULong): Boolean {
        val game = getGameByPlatformId(channelId)
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
     */
    fun updateLastMessageTimestamp(gameId: Int) {
        val game = getGameById(gameId)
        val timestamp = Timestamp.from(Instant.now()).toString()
        game.lastMessageTimestamp = timestamp
        saveGame(game)
    }

    /**
     * Update the timeouts for a game
     */
    private fun updateTimeouts(
        game: Game,
        homeTimeoutCalled: Boolean,
        awayTimeoutCalled: Boolean,
        timeoutUsed: Boolean,
    ) {
        when {
            homeTimeoutCalled && timeoutUsed -> game.homeTimeouts -= 1
            awayTimeoutCalled && timeoutUsed -> game.awayTimeouts -= 1
        }
    }

    /**
     * Update the team the game is waiting on
     */
    private fun updateWaitingOn(possession: TeamSide): TeamSide {
        return if (possession == TeamSide.HOME) {
            TeamSide.AWAY
        } else {
            TeamSide.HOME
        }
    }

    /**
     * Update the play type for a game
     */
    private fun updatePlayType(
        game: Game,
        play: Play,
    ) {
        if (isTouchdownPlay(play.actualResult)) {
            game.currentPlayType = PlayType.PAT
        } else if (play.actualResult == ActualResult.SAFETY ||
            (play.playCall == PlayCall.FIELD_GOAL && play.result == Scenario.GOOD) ||
            play.playCall == PlayCall.PAT ||
            play.playCall == PlayCall.TWO_POINT
        ) {
            if (game.gameStatus == GameStatus.OVERTIME) {
                game.currentPlayType = PlayType.NORMAL
            } else {
                game.currentPlayType = PlayType.KICKOFF
            }
        } else {
            game.currentPlayType = PlayType.NORMAL
        }
    }

    /**
     * Update the values to start a game
     * @param game
     * @param clock
     * @param possession
     * @param quarter
     * @param ballLocation
     * @param down
     * @param yardsToGo
     * @param waitingOn
     */
    private fun updateStartGameValues(
        game: Game,
        clock: Int,
        possession: TeamSide,
        quarter: Int,
        ballLocation: Int,
        down: Int,
        yardsToGo: Int,
        waitingOn: TeamSide,
    ) {
        game.gameStatus = GameStatus.IN_PROGRESS
        game.clock = convertClockToString(clock)
        game.possession = possession
        game.quarter = quarter
        game.ballLocation = ballLocation
        game.down = down
        game.yardsToGo = yardsToGo
        game.waitingOn = waitingOn
    }

    /**
     * Update the values for when a game is going into halftime
     * @param game
     * @param possession
     * @param waitingOn
     * @param clock
     */
    private fun updateHalftimeValues(
        game: Game,
        possession: TeamSide,
        waitingOn: TeamSide,
        clock: Int,
    ) {
        game.homeTimeouts = 3
        game.awayTimeouts = 3
        game.gameStatus = GameStatus.HALFTIME
        game.currentPlayType = PlayType.KICKOFF
        game.ballLocation = 35
        game.clock = convertClockToString(clock)
        game.possession = possession
        game.quarter = 3
        game.down = 1
        game.yardsToGo = 10
        game.waitingOn = waitingOn
    }

    /**
     * Update the values for the start of a half
     * @param game
     * @param clock
     * @param possession
     * @param quarter
     * @param ballLocation
     * @param down
     * @param yardsToGo
     * @param waitingOn
     */
    private fun updateStartOfHalfValues(
        game: Game,
        clock: Int,
        possession: TeamSide,
        quarter: Int,
        ballLocation: Int,
        down: Int,
        yardsToGo: Int,
        waitingOn: TeamSide,
    ) {
        game.gameStatus = GameStatus.IN_PROGRESS
        game.clock = convertClockToString(clock)
        game.possession = possession
        game.quarter = quarter
        game.ballLocation = ballLocation
        game.down = down
        game.yardsToGo = yardsToGo
        game.waitingOn = waitingOn
    }

    /**
     * Update the end of game values for regulation games
     * @param game
     */
    private fun updateEndOfRegulationGameValues(game: Game) {
        game.quarter = 4
        game.clock = "0:00"
        game.clockStopped = true
        game.gameStatus = GameStatus.FINAL
    }

    /**
     * Update the values for a normal play
     * @param game
     * @param clock
     * @param possession
     * @param quarter
     * @param ballLocation
     * @param down
     * @param yardsToGo
     * @param waitingOn
     */
    private fun updateNormalPlayValues(
        game: Game,
        clock: Int,
        possession: TeamSide,
        quarter: Int,
        ballLocation: Int,
        down: Int,
        yardsToGo: Int,
        waitingOn: TeamSide,
    ) {
        game.clock = convertClockToString(clock)
        game.possession = possession
        game.quarter = quarter
        game.ballLocation = ballLocation
        game.down = down
        game.yardsToGo = yardsToGo
        game.waitingOn = waitingOn
    }

    /**
     * Update the values for the start of overtime
     * @param game
     * @param quarter
     */
    private fun updateStartOfOvertimeValues(
        game: Game,
        quarter: Int,
    ) {
        game.clock = "0:00"
        game.quarter = quarter
        game.gameStatus = GameStatus.END_OF_REGULATION
        game.currentPlayType = PlayType.NORMAL
        game.ballLocation = 75
        game.down = 1
        game.yardsToGo = 10
        game.overtimeHalf = 1
        game.homeTimeouts = 1
        game.awayTimeouts = 1
    }

    /**
     * Update overtime values for a game
     * @param game
     * @param play
     * @param possession
     * @param waitingOn
     * @param ballLocation
     * @param down
     * @param yardsToGo
     * @param homeScore
     * @param awayScore
     */
    private fun updateOvertimeValues(
        game: Game,
        play: Play,
        possession: TeamSide,
        waitingOn: TeamSide,
        ballLocation: Int,
        down: Int,
        yardsToGo: Int,
        homeScore: Int,
        awayScore: Int,
    ) {
        game.clock = "0:00"
        if (isEndOfOvertimeHalf(play)) {
            // Handle the end of each half of overtime
            if (game.overtimeHalf == 1) {
                if (play.actualResult == ActualResult.TOUCHDOWN ||
                    play.actualResult == ActualResult.TURNOVER_TOUCHDOWN ||
                    play.actualResult == ActualResult.KICK_SIX
                ) {
                    game.possession = possession
                    game.waitingOn = waitingOn
                    game.ballLocation = ballLocation
                    game.down = down
                    game.yardsToGo = yardsToGo
                } else {
                    game.overtimeHalf = 2
                    game.possession =
                        if (game.possession == TeamSide.HOME) {
                            TeamSide.AWAY
                        } else {
                            TeamSide.HOME
                        }
                    game.ballLocation = 75
                    game.down = 1
                    game.yardsToGo = 10
                    game.waitingOn = if (game.possession == TeamSide.HOME) TeamSide.AWAY else TeamSide.HOME
                }
            } else {
                if (homeScore != awayScore) {
                    // End of game, one team has won
                    // If the game is within 2 points, kick the PAT
                    if (isGameMathmaticallyOver(play, homeScore, awayScore)) {
                        game.possession = possession
                        game.waitingOn = waitingOn
                        game.ballLocation = ballLocation
                        game.down = down
                        game.yardsToGo = yardsToGo
                    } else {
                        game.gameStatus = GameStatus.FINAL
                    }
                } else {
                    endOvertimePeriod(game, possession)
                }
            }
        } else {
            game.possession = possession
            game.waitingOn = waitingOn
            game.ballLocation = ballLocation
            game.down = down
            game.yardsToGo = yardsToGo
        }
    }

    /**
     * Sub a coach in for a team
     * @param gameId
     */
    fun subCoachIntoGame(
        gameId: Int,
        team: String,
        discordId: String,
    ): Game {
        val game = getGameById(gameId)
        val userData = userService.getUserDTOByDiscordId(discordId)
        val coach = userData.discordTag

        when (team.lowercase()) {
            game.homeTeam.lowercase() -> {
                game.homeCoaches = listOf(coach)
                game.homeCoachDiscordIds = listOf(userData.discordId ?: throw NoCoachDiscordIdsFoundException())
            }
            game.awayTeam.lowercase() -> {
                game.awayCoaches = listOf(coach)
                game.awayCoachDiscordIds = listOf(userData.discordId ?: throw NoCoachDiscordIdsFoundException())
            }
            else -> {
                throw TeamNotFoundException("$team not found in game $gameId")
            }
        }
        saveGame(game)
        return game
    }

    /**
     * Get the game by request message id
     * @param requestMessageId
     */
    fun getGameByRequestMessageId(requestMessageId: String) =
        gameRepository.getGameByRequestMessageId(requestMessageId)
            ?: throw GameNotFoundException("Game not found for Request Message ID: $requestMessageId")

    /**
     * Get the game by platform id
     * @param platformId
     */
    fun getGameByPlatformId(platformId: ULong) =
        gameRepository.getGameByPlatformId(platformId)
            ?: throw GameNotFoundException("Game not found for Platform ID: $platformId")

    /**
     * Get an ongoing game by id
     * @param id
     * @return
     */
    fun getGameById(id: Int) = gameRepository.getGameById(id) ?: throw GameNotFoundException("No game found with ID: $id")

    /**
     * Get filtered games
     * @param filters
     * @param sort
     * @param conference
     * @param season
     * @param week
     * @param pageable
     */
    fun getFilteredGames(
        filters: List<GameFilter>,
        category: GameCategory?,
        conference: String?,
        season: Int?,
        week: Int?,
        sort: GameSort,
        pageable: Pageable,
    ): Page<Game> {
        val filterSpec = gameSpecificationService.createSpecification(filters, category, conference, season, week)
        val sortOrders = gameSpecificationService.createSort(sort)
        val sortedPageable =
            PageRequest.of(
                pageable.pageNumber,
                pageable.pageSize,
                Sort.by(sortOrders),
            )

        return gameRepository.findAll(filterSpec, sortedPageable)
            ?: throw GameNotFoundException(
                "No games found for the following filters: " +
                    "filters = $filters, " +
                    "category = $category, " +
                    "conference = $conference, " +
                    "season = $season, " +
                    "week = $week",
            )
    }

    /**
     * Find expired timers
     */
    fun findExpiredTimers() =
        gameRepository.findExpiredTimers().ifEmpty {
            Logger.info("No games found with expired timers")
            emptyList()
        }

    /**
     * Find games to warn
     */
    fun findGamesToWarn() =
        gameRepository.findGamesToWarn().ifEmpty {
            Logger.info("No games found to warn")
            emptyList()
        }

    /**
     * Update a game as warned
     * @param gameId
     */
    fun updateGameAsWarned(gameId: Int) = gameRepository.updateGameAsWarned(gameId)

    /**
     * Mark a game as close game pinged
     * @param gameId
     */
    fun markCloseGamePinged(gameId: Int) = gameRepository.markCloseGamePinged(gameId)

    /**
     * Mark a game as upset alert pinged
     * @param gameId
     */
    fun markUpsetAlertPinged(gameId: Int) = gameRepository.markUpsetAlertPinged(gameId)

    /**
     * Get all games
     */
    fun getAllGames() =
        gameRepository.getAllGames().ifEmpty {
            throw GameNotFoundException("No games found when getting all games")
        }

    /**
     * Get all ongoing games
     */
    private fun getAllOngoingGames() =
        gameRepository.getAllOngoingGames().ifEmpty {
            throw GameNotFoundException("No ongoing games found")
        }

    /**
     * Get all games with the teams in it for the requested week
     * @param teams
     * @param season
     * @param week
     */
    fun getGamesWithTeams(
        teams: List<Team>,
        season: Int,
        week: Int,
    ): List<Game> {
        val games = mutableListOf<Game>()
        for (team in teams) {
            val game = gameRepository.getGamesByTeamSeasonAndWeek(team.name ?: "", season, week)
            if (game != null) {
                games.add(game)
            } else {
                throw GameNotFoundException("No games found for ${team.name} in season $season week $week")
            }
        }
        return games
    }

    /**
     * Handle the halftime possession change
     * @param game
     * @return
     */
    fun handleHalfTimePossessionChange(game: Game): TeamSide {
        return if (game.coinTossWinner == TeamSide.HOME && game.coinTossChoice == CoinTossChoice.DEFER) {
            TeamSide.AWAY
        } else if (game.coinTossWinner == TeamSide.HOME && game.coinTossChoice == CoinTossChoice.RECEIVE) {
            TeamSide.HOME
        } else if (game.coinTossWinner == TeamSide.AWAY && game.coinTossChoice == CoinTossChoice.DEFER) {
            TeamSide.HOME
        } else if (game.coinTossWinner == TeamSide.AWAY && game.coinTossChoice == CoinTossChoice.RECEIVE) {
            TeamSide.AWAY
        } else {
            throw InvalidHalfTimePossessionChangeException()
        }
    }

    /**
     * Determines if the game is close
     * @param game the game
     * @param play the play
     */
    private fun updateCloseGame(
        game: Game,
        play: Play,
    ) {
        game.closeGame = abs(game.homeScore - game.awayScore) <= 8 &&
            play.quarter >= 4 &&
            play.clock <= 210
    }

    /**
     * Determine if there is an upset alert. A game is an upset alert
     * if at least one of the teams is ranked and the game is close
     * @param game the game
     * @param play the play
     */
    private fun updateUpsetAlert(
        game: Game,
        play: Play,
    ) {
        val homeTeam = teamService.getTeamByName(game.homeTeam)
        val awayTeam = teamService.getTeamByName(game.awayTeam)
        var homeTeamRanking = if (homeTeam.playoffCommitteeRanking == 0) homeTeam.coachesPollRanking else homeTeam.playoffCommitteeRanking
        var awayTeamRanking = if (awayTeam.playoffCommitteeRanking == 0) awayTeam.coachesPollRanking else awayTeam.playoffCommitteeRanking

        homeTeamRanking = if (homeTeamRanking == 0 || homeTeamRanking == null) 100 else homeTeamRanking
        awayTeamRanking = if (awayTeamRanking == 0 || awayTeamRanking == null) 100 else awayTeamRanking

        if ((
                (game.homeScore <= game.awayScore && homeTeamRanking < awayTeamRanking) ||
                    (game.awayScore <= game.homeScore && awayTeamRanking < homeTeamRanking)
            ) &&
            game.quarter >= 4 &&
            play.clock <= 210
        ) {
            game.upsetAlert = true
        }
        if ((
                (abs(game.homeScore - game.awayScore) <= 8 && homeTeamRanking < awayTeamRanking) ||
                    (abs(game.awayScore - game.homeScore) <= 8 && awayTeamRanking < homeTeamRanking)
            ) &&
            game.quarter >= 4 &&
            play.clock <= 210
        ) {
            game.upsetAlert = true
        }
        game.upsetAlert = false
    }

    /**
     * Returns the difference between the offensive and defensive numbers.
     * @param offensiveNumber
     * @param defesiveNumber
     * @return
     */
    fun getDifference(
        offensiveNumber: Int,
        defesiveNumber: Int,
    ): Int {
        var difference = abs(defesiveNumber - offensiveNumber)
        if (difference > 750) {
            difference = 1500 - difference
        }
        return difference
    }

    /**
     * Returns the number of seconds from the clock.
     * @param clock
     * @return
     */
    fun convertClockToSeconds(clock: String): Int {
        val clockArray = clock.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val minutes = clockArray[0].toInt()
        val seconds = clockArray[1].toInt()
        return minutes * 60 + seconds
    }

    /**
     * Returns the clock from the number of seconds.
     * @param seconds
     * @return
     */
    private fun convertClockToString(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%d:%02d", minutes, remainingSeconds)
    }

    /**
     * Check if a play is a scoring play
     * @param actualResult
     * @return
     */
    fun isTouchdownPlay(actualResult: ActualResult?): Boolean {
        return actualResult == ActualResult.TOUCHDOWN ||
            actualResult == ActualResult.TURNOVER_TOUCHDOWN ||
            actualResult == ActualResult.RETURN_TOUCHDOWN ||
            actualResult == ActualResult.KICKING_TEAM_TOUCHDOWN ||
            actualResult == ActualResult.PUNT_RETURN_TOUCHDOWN ||
            actualResult == ActualResult.PUNT_TEAM_TOUCHDOWN ||
            actualResult == ActualResult.KICK_SIX
    }

    /**
     * Determine if the clock is stopped
     */
    private fun updateClockStopped(
        game: Game,
        play: Play,
        clock: Int,
    ) {
        game.clockStopped = play.playCall == PlayCall.SPIKE || play.result == Scenario.INCOMPLETE ||
            play.actualResult == ActualResult.TURNOVER_ON_DOWNS ||
            play.actualResult == ActualResult.TOUCHDOWN || play.playCall == PlayCall.FIELD_GOAL ||
            play.playCall == PlayCall.PAT || play.playCall == PlayCall.KICKOFF_NORMAL ||
            play.playCall == PlayCall.KICKOFF_ONSIDE || play.playCall == PlayCall.KICKOFF_SQUIB ||
            play.playCall == PlayCall.PUNT || play.actualResult == ActualResult.TURNOVER ||
            play.actualResult == ActualResult.TURNOVER_TOUCHDOWN || play.actualResult == ActualResult.SAFETY ||
            game.gameStatus == GameStatus.OVERTIME || game.gameStatus == GameStatus.HALFTIME

        if (clock == 420) {
            game.clockStopped = true
        }
    }

    private fun isEndOfOvertimeHalf(play: Play) =
        play.actualResult == ActualResult.GOOD ||
            play.actualResult == ActualResult.NO_GOOD ||
            play.actualResult == ActualResult.BLOCKED ||
            play.actualResult == ActualResult.SUCCESS ||
            play.actualResult == ActualResult.FAILED ||
            play.actualResult == ActualResult.DEFENSE_TWO_POINT ||
            play.actualResult == ActualResult.TURNOVER_ON_DOWNS ||
            play.actualResult == ActualResult.TURNOVER ||
            play.actualResult == ActualResult.TOUCHDOWN ||
            play.actualResult == ActualResult.TURNOVER_TOUCHDOWN ||
            play.actualResult == ActualResult.KICK_SIX

    private fun isGameMathmaticallyOver(
        play: Play,
        homeScore: Int,
        awayScore: Int,
    ) = (
        play.actualResult == ActualResult.TOUCHDOWN ||
            play.actualResult == ActualResult.TURNOVER_TOUCHDOWN ||
            play.actualResult == ActualResult.KICK_SIX
    ) && (abs(homeScore - awayScore) <= 2 || abs(awayScore - homeScore) <= 2)
}
