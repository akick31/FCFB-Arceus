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
    private var gameRepository: GameRepository,
    private var playRepository: PlayRepository,
    private var playHandler: PlayHandler,
    private var gameHandler: GameHandler,
    private var encryptionUtils: EncryptionUtils,
    private val scorebugService: ScorebugService,
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
        timeoutCalled: Boolean?,
    ): ResponseEntity<Any> {
        return try {
            val game =
                gameRepository.findByGameId(gameId)
                    ?: run {
                        Logger.error("Could not find game with id $gameId")
                        return ResponseEntity(
                            headers.add(
                                "Error-Message",
                                "Could not find game with game ID",
                            ),
                            HttpStatus.NOT_FOUND,
                        )
                    }

            val encryptedDefensiveNumber: String = encryptionUtils.encrypt(defensiveNumber.toString())
            val clock: Int =
                gameHandler.convertClockToSeconds(
                    game.clock
                        ?: run {
                            Logger.error("Could not find clock for game with id $gameId")
                            return ResponseEntity(
                                headers.add(
                                    "Error-Message",
                                    "Could not find clock for game",
                                ),
                                HttpStatus.BAD_REQUEST,
                            )
                        },
                )
            val gamePlay: Play =
                playRepository.save(
                    Play(
                        gameId,
                        game.numPlays?.plus(1)
                            ?: run {
                                Logger.error("Could not get the number of plays for game with id $gameId")
                                return ResponseEntity(
                                    headers.add(
                                        "Error-Message",
                                        "Could not get the number of plays for the game",
                                    ),
                                    HttpStatus.BAD_REQUEST,
                                )
                            },
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
                        game.homeTimeouts
                            ?: run {
                                Logger.error("Could not get the number of home timeouts for game with id $gameId")
                                return ResponseEntity(
                                    headers.add(
                                        "Error-Message",
                                        "Could not get the number of home timeouts",
                                    ),
                                    HttpStatus.BAD_REQUEST,
                                )
                            },
                        game.awayTimeouts
                            ?: run {
                                Logger.error("Could not get the number of away timeouts for game with id $gameId")
                                return ResponseEntity(
                                    headers.add(
                                        "Error-Message",
                                        "Could not get the number of away timeouts",
                                    ),
                                    HttpStatus.BAD_REQUEST,
                                )
                            },
                        false,
                        null,
                        null,
                    ),
                ) ?: run {
                    Logger.error("Could not save play for game with id $gameId")
                    return ResponseEntity(
                        headers.add(
                            "Error-Message",
                            "There was an issue saving the play",
                        ),
                        HttpStatus.BAD_REQUEST,
                    )
                }

            game.currentPlayId = gamePlay.playId
            game.waitingOn = game.possession
            gameRepository.save(game)
            ResponseEntity(gamePlay, HttpStatus.CREATED)
        } catch (e: Exception) {
            Logger.error("There was an error submitting the defensive number for game $gameId: " + e.message)
            ResponseEntity(
                headers.add(
                    "Error-Message",
                    e.message ?: "Unknown error",
                ),
                HttpStatus.BAD_REQUEST,
            )
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
    ): ResponseEntity<Any> {
        return try {
            val game =
                gameRepository.findByGameId(gameId)
                    ?: run {
                        Logger.error("Could not find game with id $gameId")
                        return ResponseEntity(
                            headers.add(
                                "Error-Message",
                                "There was an issue finding the game",
                            ),
                            HttpStatus.NOT_FOUND,
                        )
                    }
            var gamePlay =
                playRepository.findByPlayId(game.currentPlayId!!)
                    ?: run {
                        Logger.error("Could not find play with id ${game.currentPlayId}")
                        return ResponseEntity(
                            headers.add(
                                "Error-Message",
                                "There was an issue finding the play",
                            ),
                            HttpStatus.NOT_FOUND,
                        )
                    }
            gamePlay.offensiveSubmitter = offensiveSubmitter

            val decryptedDefensiveNumber =
                encryptionUtils.decrypt(
                    gamePlay.defensiveNumber
                        ?: run {
                            Logger.error("There was an issue decrypting the defensive number for game $gameId")
                            return ResponseEntity(
                                headers.add(
                                    "Error-Message",
                                    "There was an issue decrypting the defensive number",
                                ),
                                HttpStatus.BAD_REQUEST,
                            )
                        },
                )

            when (playCall) {
                PlayCall.PASS, PlayCall.RUN, PlayCall.SPIKE, PlayCall.KNEEL ->
                    gamePlay = playHandler.runNormalPlay(
                        gamePlay,
                        game,
                        playCall,
                        runoffType,
                        offensiveTimeoutCalled,
                        offensiveNumber.toString(),
                        decryptedDefensiveNumber,
                    ) ?: run {
                        Logger.error("There was an issue running a normal play for game $gameId")
                        return ResponseEntity(
                            headers.add(
                                "Error-Message",
                                "There was an issue running a normal play",
                            ),
                            HttpStatus.BAD_REQUEST,
                        )
                    }

                PlayCall.PAT, PlayCall.TWO_POINT ->
                    gamePlay = playHandler.runPointAfterPlay(
                        gamePlay,
                        game,
                        playCall,
                        offensiveNumber.toString(),
                        decryptedDefensiveNumber,
                    ) ?: run {
                        Logger.error("There was an issue running the PAT play for game $gameId")
                        return ResponseEntity(
                            headers.add(
                                "Error-Message",
                                "There was an issue running the PAT play",
                            ),
                            HttpStatus.BAD_REQUEST,
                        )
                    }

                PlayCall.KICKOFF_NORMAL, PlayCall.KICKOFF_ONSIDE, PlayCall.KICKOFF_SQUIB ->
                    gamePlay = playHandler.runKickoffPlay(
                        gamePlay,
                        game,
                        playCall,
                        offensiveNumber.toString(),
                        decryptedDefensiveNumber,
                    ) ?: run {
                        Logger.error("There was an issue running the kickoff play for game $gameId")
                        return ResponseEntity(
                            headers.add(
                                "Error-Message",
                                "There was an issue running the kickoff play",
                            ),
                            HttpStatus.BAD_REQUEST,
                        )
                    }

                PlayCall.FIELD_GOAL ->
                    gamePlay = playHandler.runFieldGoalPlay(
                        gamePlay,
                        game,
                        playCall,
                        runoffType,
                        offensiveTimeoutCalled,
                        offensiveNumber.toString(),
                        decryptedDefensiveNumber,
                    ) ?: run {
                        Logger.error("There was an issue running the field goal play for game $gameId")
                        return ResponseEntity(
                            headers.add(
                                "Error-Message",
                                "There was an issue running the field goal play",
                            ),
                            HttpStatus.BAD_REQUEST,
                        )
                    }

                PlayCall.PUNT ->
                    gamePlay = playHandler.runPuntPlay(
                        gamePlay,
                        game,
                        playCall,
                        runoffType,
                        offensiveTimeoutCalled,
                        offensiveNumber.toString(),
                        decryptedDefensiveNumber,
                    ) ?: run {
                        Logger.error("There was an issue running the punt play for game $gameId")
                        return ResponseEntity(
                            headers.add(
                                "Error-Message",
                                "There was an issue running the punt play",
                            ),
                            HttpStatus.BAD_REQUEST,
                        )
                    }
            }

            return ResponseEntity(gamePlay, HttpStatus.OK)
        } catch (e: Exception) {
            Logger.error("There was an error submitting the offensive number for game $gameId: " + e.message)
            ResponseEntity(
                headers.add(
                    "Error-Message",
                    e.message ?: "Unknown error",
                ),
                HttpStatus.BAD_REQUEST,
            )
        }
    }

    /**
     * Get a play by its id
     * @param playId
     * @return
     */
    fun getPlayById(playId: Int): ResponseEntity<Any> {
        return try {
            val play =
                playRepository.findByPlayId(playId)
                    ?: run {
                        Logger.error("Could not find play with id $playId")
                        return ResponseEntity(
                            headers.add(
                                "Error-Message",
                                "Could not find play with play ID",
                            ),
                            HttpStatus.NOT_FOUND,
                        )
                    }
            ResponseEntity(play, HttpStatus.OK)
        } catch (e: Exception) {
            Logger.error("There was an error getting the play with id $playId: " + e.message)
            ResponseEntity(
                headers.add(
                    "Error-Message",
                    e.message ?: "Unknown error",
                ),
                HttpStatus.BAD_REQUEST,
            )
        }
    }
}
