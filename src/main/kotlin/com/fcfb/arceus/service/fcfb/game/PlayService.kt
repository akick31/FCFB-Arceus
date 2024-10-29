package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.RunoffType
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.handlers.game.GameHandler
import com.fcfb.arceus.handlers.game.PlayHandler
import com.fcfb.arceus.repositories.GameRepository
import com.fcfb.arceus.repositories.PlayRepository
import com.fcfb.arceus.utils.EncryptionUtils
import com.fcfb.arceus.utils.Logger
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class PlayService(
    private val playRepository: PlayRepository,
    private val playHandler: PlayHandler,
    private val gameHandler: GameHandler,
    private val encryptionUtils: EncryptionUtils,
    private val gameService: GameService,
) {
    private var headers: HttpHeaders = HttpHeaders()

    // TODO: Add response speed

    /**
     * Start a new play, the defensive number was submitted. The defensive number is encrypted
     * @param gameId
     * @param defensiveSubmitter
     * @param defensiveNumber
     * @param timeoutCalled
     * @return
     */
    fun defensiveNumberSubmitted(
        gameId: Int,
        defensiveSubmitter: String,
        defensiveNumber: Int,
        timeoutCalled: Boolean = false,
    ): Play {
        try {
            val game = gameService.getGameById(gameId)

            val encryptedDefensiveNumber: String = encryptionUtils.encrypt(defensiveNumber.toString())
            val clock = gameHandler.convertClockToSeconds(game.clock)
            val gamePlay =
                playRepository.save(
                    Play(
                        gameId,
                        game.numPlays.plus(1),
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
                        null,
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
                        game.homeTimeouts,
                        game.awayTimeouts,
                        false,
                        null,
                        null,
                    ),
                )

            game.currentPlayId = gamePlay.playId
            game.waitingOn = game.possession
            gameService.saveGame(game)
            return gamePlay
        } catch (e: Exception) {
            Logger.error("There was an error submitting the defensive number for game $gameId: " + e.message)
            throw e
        }
    }

    /**
     * The offensive number was submitted, run the play
     * @param gameId
     * @param offensiveSubmitter
     * @param offensiveNumber
     * @param playCall
     * @param runoffType
     * @param offensiveTimeoutCalled
     * @return
     */
    fun offensiveNumberSubmitted(
        gameId: Int,
        offensiveSubmitter: String,
        offensiveNumber: Int,
        playCall: PlayCall,
        runoffType: RunoffType,
        offensiveTimeoutCalled: Boolean,
    ): Play {
        try {
            val game = gameService.getGameById(gameId)
            val allPlays = getAllPlaysByGameId(gameId)
            var gamePlay = getPlayById(game.currentPlayId!!)

            gamePlay.offensiveSubmitter = offensiveSubmitter

            val decryptedDefensiveNumber = encryptionUtils.decrypt(gamePlay.defensiveNumber)


            when (playCall) {
                PlayCall.PASS, PlayCall.RUN, PlayCall.SPIKE, PlayCall.KNEEL ->
                    gamePlay = playHandler.runNormalPlay(
                        gamePlay,
                        allPlays,
                        game,
                        playCall,
                        runoffType,
                        offensiveTimeoutCalled,
                        offensiveNumber.toString(),
                        decryptedDefensiveNumber,
                    )

                PlayCall.PAT, PlayCall.TWO_POINT ->
                    gamePlay = playHandler.runPointAfterPlay(
                        gamePlay,
                        allPlays,
                        game,
                        playCall,
                        offensiveNumber.toString(),
                        decryptedDefensiveNumber,
                    )

                PlayCall.KICKOFF_NORMAL, PlayCall.KICKOFF_ONSIDE, PlayCall.KICKOFF_SQUIB ->
                    gamePlay = playHandler.runKickoffPlay(
                        gamePlay,
                        allPlays,
                        game,
                        playCall,
                        offensiveNumber.toString(),
                        decryptedDefensiveNumber,
                    )

                PlayCall.FIELD_GOAL ->
                    gamePlay = playHandler.runFieldGoalPlay(
                        gamePlay,
                        allPlays,
                        game,
                        playCall,
                        runoffType,
                        offensiveTimeoutCalled,
                        offensiveNumber.toString(),
                        decryptedDefensiveNumber,
                    )

                PlayCall.PUNT ->
                    gamePlay = playHandler.runPuntPlay(
                        gamePlay,
                        allPlays,
                        game,
                        playCall,
                        runoffType,
                        offensiveTimeoutCalled,
                        offensiveNumber.toString(),
                        decryptedDefensiveNumber,
                    )
            }

            return gamePlay
        } catch (e: Exception) {
            Logger.error("There was an error submitting the offensive number for game $gameId: " + e.message)
            throw e
        }
    }

    /**
     * Get a play by its id
     * @param playId
     * @return
     */
    fun getPlayById(playId: Int) = playRepository.getPlayById(playId)

    /**
     * Get all plays for a game
     */
    fun getAllPlaysByGameId(gameId: Int) = playRepository.getAllPlaysByGameId(gameId)

}
