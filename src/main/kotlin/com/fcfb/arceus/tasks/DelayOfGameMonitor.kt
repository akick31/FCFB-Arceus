package com.fcfb.arceus.tasks

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.ActualResult
import com.fcfb.arceus.domain.Game.GameType
import com.fcfb.arceus.domain.Game.Scenario
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.repositories.PlayRepository
import com.fcfb.arceus.service.discord.DiscordService
import com.fcfb.arceus.service.fcfb.UserService
import com.fcfb.arceus.service.fcfb.game.GameService
import com.fcfb.arceus.service.fcfb.game.PlayService
import com.fcfb.arceus.service.fcfb.game.ScorebugService
import com.fcfb.arceus.utils.Logger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class DelayOfGameMonitor(
    private val gameService: GameService,
    private val userService: UserService,
    private val playService: PlayService,
    private val discordService: DiscordService,
    private val scorebugService: ScorebugService,
    private val playRepository: PlayRepository,
) {
    /**
     * Checks for delay of game every minute
     */
    @Scheduled(fixedRate = 60000)
    fun checkForDelayOfGame() {
        val warnedGames = gameService.findGamesToWarn()
        warnedGames.forEach { game ->
            discordService.notifyWarning(game)
            gameService.updateGameAsWarned(game.gameId)
            Logger.info("A delay of game warning for game ${game.gameId} has been processed")
        }
        val expiredGames = gameService.findExpiredTimers()
        expiredGames.forEach { game ->
            val updatedGame =
                if (game.gameStatus == Game.GameStatus.PREGAME) {
                    applyPregameDelayOfGame(game)
                } else {
                    applyDelayOfGame(game)
                }
            val delayOfGameInstances = getDelayOfGameInstances(updatedGame)
            val isDelayOfGameOut = delayOfGameInstances.first >= 3 || delayOfGameInstances.second >= 3
            if (isDelayOfGameOut) {
                gameService.endDOGOutGame(updatedGame, delayOfGameInstances)
            }
            discordService.notifyDelayOfGame(updatedGame, isDelayOfGameOut)
            Logger.info("A delay of game for game ${game.gameId} has been processed")
        }
    }

    /**
     * Get the delay of game instances for a given game
     * @return Pair of home and away delay of game instances
     */
    private fun getDelayOfGameInstances(game: Game): Pair<Int, Int> {
        if (game.gameType == GameType.SCRIMMAGE) {
            return Pair(0, 0)
        }
        if (game.waitingOn == TeamSide.HOME) {
            val instances = playService.getHomeDelayOfGameInstances(game.gameId)
            return Pair(instances, 0)
        } else {
            val instances = playService.getAwayDelayOfGameInstances(game.gameId)
            return Pair(0, instances)
        }
    }

    /**
     * Apply a delay of game to a game in pregame status
     * @param game
     */
    private fun applyPregameDelayOfGame(game: Game): Game {
        game.gameTimer = gameService.calculateDelayOfGameTimer()
        if (game.waitingOn == TeamSide.HOME) {
            game.awayScore += 8
            if (game.gameType != GameType.SCRIMMAGE) {
                for (coach in game.homeCoachDiscordIds!!) {
                    val user = userService.getUserByDiscordId(coach)
                    user.delayOfGameInstances += 1
                    userService.saveUser(user)
                }
            }
        } else {
            game.homeScore += 8
            if (game.gameType != GameType.SCRIMMAGE) {
                for (coach in game.awayCoachDiscordIds!!) {
                    val user = userService.getUserByDiscordId(coach)
                    user.delayOfGameInstances += 1
                    userService.saveUser(user)
                }
            }
        }

        val savedPlay = saveDelayOfGameOnOffensePlay(game)
        game.currentPlayId = savedPlay.playId
        gameService.saveGame(game)
        scorebugService.generateScorebug(game)
        return game
    }

    /**
     * Apply a delay of game to a game
     * @param game
     */
    private fun applyDelayOfGame(game: Game): Game {
        game.gameTimer = gameService.calculateDelayOfGameTimer()
        if (game.waitingOn == TeamSide.HOME) {
            game.currentPlayType = Game.PlayType.KICKOFF
            game.possession = TeamSide.AWAY
            game.awayScore += 8

            if (game.gameType != GameType.SCRIMMAGE) {
                for (coach in game.homeCoachDiscordIds!!) {
                    val user = userService.getUserByDiscordId(coach)
                    user.delayOfGameInstances += 1
                    userService.saveUser(user)
                }
            }
        } else {
            game.currentPlayType = Game.PlayType.KICKOFF
            game.possession = TeamSide.HOME
            game.homeScore += 8

            if (game.gameType != GameType.SCRIMMAGE) {
                for (coach in game.awayCoachDiscordIds!!) {
                    val user = userService.getUserByDiscordId(coach)
                    user.delayOfGameInstances += 1
                    userService.saveUser(user)
                }
            }
        }

        val currentPlay =
            try {
                playService.getCurrentPlay(game.gameId)
            } catch (e: Exception) {
                null
            }
            
        val savedPlay =
            if (currentPlay != null) {
                saveDelayOfGameOnDefensePlay(game, currentPlay)
            } else {
                saveDelayOfGameOnOffensePlay(game)
            }

        game.currentPlayId = savedPlay.playId
        game.gameWarned = false
        game.clockStopped = true
        gameService.saveGame(game)
        scorebugService.generateScorebug(game)
        return game
    }

    /**
     * Save a delay of game on defense play, as defense has called a number
     */
    private fun saveDelayOfGameOnDefensePlay(
        game: Game,
        play: Play,
    ): Play {
        play.playFinished = true
        play.offensiveNumber = null
        play.defensiveNumber = null
        play.difference = null
        if (game.waitingOn == TeamSide.HOME) {
            play.result = Scenario.DELAY_OF_GAME_HOME
            play.actualResult = ActualResult.DELAY_OF_GAME
        } else {
            play.result = Scenario.DELAY_OF_GAME_AWAY
            play.actualResult = ActualResult.DELAY_OF_GAME
        }
        return playRepository.save(play)
    }

    /**
     * Save a delay of game on offense play, as defense hasn't called a number
     * @param game
     */
    private fun saveDelayOfGameOnOffensePlay(game: Game): Play {
        val play = playService.defensiveNumberSubmitted(game.gameId, "NONE", 0, false)
        play.playFinished = true
        play.offensiveNumber = null
        play.defensiveNumber = null
        play.difference = null
        if (game.waitingOn == TeamSide.HOME) {
            play.result = Scenario.DELAY_OF_GAME_HOME
            play.actualResult = ActualResult.DELAY_OF_GAME
        } else {
            play.result = Scenario.DELAY_OF_GAME_AWAY
            play.actualResult = ActualResult.DELAY_OF_GAME
        }
        return playRepository.save(play)
    }
}
