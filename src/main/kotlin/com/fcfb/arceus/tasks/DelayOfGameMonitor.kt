package com.fcfb.arceus.tasks

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.ActualResult
import com.fcfb.arceus.domain.Game.GameType
import com.fcfb.arceus.domain.Game.Scenario
import com.fcfb.arceus.domain.Game.TeamSide
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
    @Scheduled(fixedRate = 60000) // Runs every minute
    fun checkForDelayOfGame() {
        val warnedGames = gameService.findGamesToWarn()
        warnedGames.forEach { game ->
            discordService.notifyWarning(game)
            gameService.updateGameAsWarned(game.gameId)
            Logger.info("A delay of game warning for game ${game.gameId} has been processed")
        }
        val expiredGames = gameService.findExpiredTimers()
        expiredGames.forEach { game ->
            val updatedGame = applyDelayOfGame(game)
            val isDelayofGameOut = getIsDelayOfGameOut(game)
            if (isDelayofGameOut) {
                gameService.endSingleGame(
                    game.homePlatformId?.toULong() ?: game.awayPlatformId?.toULong() ?: throw Exception("No platform ID found"),
                )
            }
            discordService.notifyDelayOfGame(updatedGame, isDelayofGameOut)
            Logger.info("A delay of game for game ${game.gameId} has been processed")
        }
    }

    private fun getIsDelayOfGameOut(game: Game): Boolean {
        if (game.gameType == GameType.SCRIMMAGE) {
            return false
        }
        if (game.waitingOn == TeamSide.HOME) {
            val instances = playService.getDelayOfGameInstances(game.gameId, TeamSide.AWAY)
            return instances >= 3
        } else {
            val instances = playService.getDelayOfGameInstances(game.gameId, TeamSide.HOME)
            return instances >= 3
        }
    }

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

        val currentPlay = playService.getCurrentPlay(game.gameId)
        if (currentPlay != null) {
            currentPlay.playFinished = true
            currentPlay.defensiveNumber = "0"
            currentPlay.result = Scenario.DELAY_OF_GAME
            currentPlay.actualResult = ActualResult.DELAY_OF_GAME
            playRepository.save(currentPlay)
        } else {
            val play = playService.defensiveNumberSubmitted(game.gameId, "NONE", 0, false)
            play.playFinished = true
            play.defensiveNumber = "0"
            play.result = Scenario.DELAY_OF_GAME
            play.actualResult = ActualResult.DELAY_OF_GAME
            playRepository.save(play)
        }

        gameService.saveGame(game)
        scorebugService.generateScorebug(game)
        return game
    }
}
