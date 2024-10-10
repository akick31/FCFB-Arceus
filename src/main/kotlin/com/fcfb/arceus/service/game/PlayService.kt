package com.fcfb.arceus.service.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.domain.Game.RunoffType
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.handlers.game.GameHandler
import com.fcfb.arceus.handlers.game.StatsHandler
import com.fcfb.arceus.handlers.game.PlayHandler
import com.fcfb.arceus.service.game.ScorebugService
import com.fcfb.arceus.repositories.GameRepository
import com.fcfb.arceus.repositories.PlayRepository
import com.fcfb.arceus.utils.EncryptionUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class PlayService(
    private var gameRepository: GameRepository,
    private var playRepository: PlayRepository,
    private var playHandler: PlayHandler,
    private var gameHandler: GameHandler,
    private var statsHandler: StatsHandler,
    private var encryptionUtils: EncryptionUtils,
) {
    private var headers: HttpHeaders = HttpHeaders()

    /**
     * Start a new play, the defensive number was submitted. The defensive number is encrypted
     * @param gameId
     * @param defensiveNumber
     * @return
     */
    fun defensiveNumberSubmitted(
        gameId: Int,
        defensiveNumber: Int,
        timeoutCalled: Boolean?
    ): ResponseEntity<Any> {
        return try {
            val game = gameRepository.findByGameId(gameId) ?: return ResponseEntity(headers.add("Error-Message", "Could not find game with game ID"), HttpStatus.NOT_FOUND)

            val offensiveSubmitter: String?
            val defensiveSubmitter: String?
            if (game.possession == TeamSide.HOME) {
                offensiveSubmitter = game.homeCoach
                defensiveSubmitter = game.awayCoach
            } else {
                offensiveSubmitter = game.awayCoach
                defensiveSubmitter = game.homeCoach
            }
            val encryptedDefensiveNumber: String = encryptionUtils.encrypt(defensiveNumber.toString())
            val clock: Int = gameHandler.convertClockToSeconds(game.clock ?: return ResponseEntity(headers.add("Error-Message", "Could not find clock for game"), HttpStatus.BAD_REQUEST))
            val gamePlay: Play = playRepository.save(
                Play(
                    gameId,
                    game.numPlays?.plus(1) ?: return ResponseEntity(headers.add("Error-Message", "Could not get the number of plays for the game"), HttpStatus.BAD_REQUEST),
                    game.homeScore,
                    game.awayScore,
                    game.quarter,
                    clock,
                    game.ballLocation,
                    game.possession,
                    game.down,
                    game.yardsToGo,
                    encryptedDefensiveNumber,
                    "0",
                    offensiveSubmitter,
                    defensiveSubmitter,
                    null,
                    null,
                    null,
                    0,
                    0,
                    0,
                    game.winProbability,
                    0.0,
                    game.homeTeam,
                    game.awayTeam,
                    0,
                    timeoutCalled,
                    false,
                    timeoutCalled,
                    game.homeTimeouts ?: return ResponseEntity(headers.add("Error-Message", "Could not get the number of home timeouts"), HttpStatus.BAD_REQUEST),
                    game.awayTimeouts ?: return ResponseEntity(headers.add("Error-Message", "Could not get the number of away timeouts"), HttpStatus.BAD_REQUEST),
                    false,
                    null, //TODO: Add response speed
                    null //TODO: Add response speed
                )
            ) ?: return ResponseEntity(headers.add("Error-Message", "There was an issue saving the play"), HttpStatus.BAD_REQUEST)

            game.currentPlayId = gamePlay.playId
            game.waitingOn = game.possession
            gameRepository.save(game)
            ResponseEntity(gamePlay, HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(headers.add("Error-Message", e.message ?: "Unknown error"), HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * The offensive number was submitted, run the play
     * @param playId
     * @param offensiveNumber
     * @param playCall
     * @param runoffType
     * @param timeoutCalled
     * @return
     */
    fun offensiveNumberSubmitted(
        gameId: Int,
        offensiveNumber: Int,
        playCall: PlayCall,
        runoffType: RunoffType,
        offensiveTimeoutCalled: Boolean,
    ): ResponseEntity<Any> {
        return try {
            val game = gameRepository.findByGameId(gameId) ?: return ResponseEntity(headers.add("Error-Message", "There was an issue finding the game"), HttpStatus.NOT_FOUND)
            var gamePlay = playRepository.findByPlayId(game.currentPlayId!!) ?: return ResponseEntity(headers.add("Error-Message", "There was an issue finding the play"), HttpStatus.NOT_FOUND)

            val decryptedDefensiveNumber = encryptionUtils.decrypt(gamePlay.defensiveNumber ?: return ResponseEntity(headers.add("Error-Message", "There was an issue decrypting the defensive number"), HttpStatus.BAD_REQUEST))

            val clockStopped = game.clockStopped ?: return ResponseEntity(headers.add("Error-Message", "There was an issue getting if the clock was stopped"), HttpStatus.BAD_REQUEST)

            val defensiveTimeoutCalled = gamePlay.timeoutUsed ?: return ResponseEntity(headers.add("Error-Message", "There was an issue getting if the timeout was used"), HttpStatus.BAD_REQUEST)
            var timeoutUsed = false
            var homeTimeoutCalled = false
            var awayTimeoutcalled = false
            if (offensiveTimeoutCalled && gamePlay.possession == TeamSide.HOME && game.homeTimeouts!! > 0 && !clockStopped) {
                timeoutUsed = true
                homeTimeoutCalled = true
            } else if (offensiveTimeoutCalled && gamePlay.possession == TeamSide.AWAY && game.awayTimeouts!! > 0 && !clockStopped) {
                timeoutUsed = true
                awayTimeoutcalled = true
            } else if (defensiveTimeoutCalled && gamePlay.possession == TeamSide.HOME && game.awayTimeouts!! > 0 && !clockStopped) {
                timeoutUsed = true
                awayTimeoutcalled = true
            } else if (defensiveTimeoutCalled && gamePlay.possession == TeamSide.AWAY && game.homeTimeouts!! > 0 && !clockStopped) {
                timeoutUsed = true
                homeTimeoutCalled = true
            }

            when (playCall) {
                PlayCall.PASS, PlayCall.RUN, PlayCall.SPIKE, PlayCall.KNEEL -> gamePlay = playHandler.runNormalPlay(
                    gamePlay,
                    clockStopped,
                    game,
                    playCall,
                    runoffType,
                    timeoutUsed,
                    offensiveNumber.toString(),
                    decryptedDefensiveNumber
                ) ?: return ResponseEntity(headers.add("Error-Message", "There was an issue running a normal play"), HttpStatus.BAD_REQUEST)

                PlayCall.PAT, PlayCall.TWO_POINT -> gamePlay = playHandler.runPointAfterPlay(
                    gamePlay,
                    game,
                    playCall,
                    offensiveNumber.toString(),
                    decryptedDefensiveNumber
                ) ?: return ResponseEntity(headers.add("Error-Message", "There was an issue running the PAT play"), HttpStatus.BAD_REQUEST)

                PlayCall.KICKOFF_NORMAL, PlayCall.KICKOFF_ONSIDE, PlayCall.KICKOFF_SQUIB -> gamePlay = playHandler.runKickoffPlay(
                    gamePlay,
                    game,
                    playCall,
                    offensiveNumber.toString(),
                    decryptedDefensiveNumber
                ) ?: return ResponseEntity(headers.add("Error-Message", "There was an issue running the kickoff play"), HttpStatus.BAD_REQUEST)

                PlayCall.FIELD_GOAL -> gamePlay = playHandler.runFieldGoalPlay(
                    gamePlay,
                    game,
                    playCall,
                    offensiveNumber.toString(),
                    decryptedDefensiveNumber
                ) ?: return ResponseEntity(headers.add("Error-Message", "There was an issue running the field goal play"), HttpStatus.BAD_REQUEST)

                PlayCall.PUNT -> gamePlay = playHandler.runPuntPlay(
                    gamePlay,
                    clockStopped,
                    game,
                    playCall,
                    runoffType,
                    timeoutUsed,
                    offensiveNumber.toString(),
                    decryptedDefensiveNumber
                ) ?: return ResponseEntity(headers.add("Error-Message", "There was an issue running the punt play"), HttpStatus.BAD_REQUEST)
            }

            gameHandler.updateGameInformation(
                game,
                gamePlay,
                playCall,
                clockStopped,
                homeTimeoutCalled,
                awayTimeoutcalled
            )

            val gameStats = statsHandler.updateGameStats(
                game,
                gamePlay,
                playCall
            )

            // Mark play as finished, set the timeouts, save the play
            gamePlay.playFinished = true
            gamePlay.offensiveTimeoutCalled = offensiveTimeoutCalled
            gamePlay.defensiveTimeoutCalled = defensiveTimeoutCalled
            playRepository.save(gamePlay)
            return ResponseEntity(gamePlay, HttpStatus.OK)
            // }
        } catch (e: Exception) {
            ResponseEntity(headers.add("Error-Message", e.message ?: "Unknown error"), HttpStatus.BAD_REQUEST)
        }
    }
}
