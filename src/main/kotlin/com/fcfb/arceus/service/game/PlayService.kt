package com.fcfb.arceus.service.game

import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.domain.Game.RunoffType
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.dto.GameDTO
import com.fcfb.arceus.repositories.GameRepository
import com.fcfb.arceus.repositories.PlayRepository
import com.fcfb.arceus.utils.EncryptionUtils
import com.fcfb.arceus.utils.GameUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class PlayService(
    private var gameRepository: GameRepository,
    private var playRepository: PlayRepository,
    private var gameDTO: GameDTO,
    private var gameUtils: GameUtils,
    private var encryptionUtils: EncryptionUtils,
    private var gamePlaysHandler: GamePlayHandler
) {
    private var emptyHeaders: HttpHeaders = HttpHeaders()

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
    ): ResponseEntity<Play> {
        return try {
            val game = gameRepository.findByGameId(gameId) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

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
            val clock: Int = gameUtils.convertClockToSeconds(game.clock ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST))
            val gamePlay: Play = playRepository.save(
                Play(
                    gameId,
                    game.numPlays?.plus(1) ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST),
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
                    game.homeTeam,
                    game.awayTeam,
                    0,
                    false,
                    game.homeTimeouts ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST),
                    game.awayTimeouts ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST),
                    false
                )
            ) ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)

            game.currentPlayId = gamePlay.playId
            game.waitingOn = if (game.possession == TeamSide.HOME) TeamSide.HOME else TeamSide.AWAY
            gameRepository.save(game)
            ResponseEntity(gamePlay, HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * The offensive number was submitted, run the play
     * @param playId
     * @param offensiveNumber
     * @param playCall
     * @param runoffType
     * @param offensiveTimeoutCalled
     * @param defensiveTimeoutCalled
     * @return
     */
    fun offensiveNumberSubmitted(
        playId: Int,
        offensiveNumber: Int,
        playCall: PlayCall,
        runoffType: RunoffType,
        offensiveTimeoutCalled: Boolean,
        defensiveTimeoutCalled: Boolean
    ): ResponseEntity<Play> {
        return try {
            var gamePlay = playRepository.findByPlayId(playId) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

            val decryptedDefensiveNumber = encryptionUtils.decrypt(gamePlay.defensiveNumber ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST))

            val game = gameRepository.findByGameId(gamePlay.gameId!!) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
            // Optional<GameStatsEntity> statsData = gameStatsRepository.findById(gamePlay.getGameId());

            // if (statsData.isPresent()) {
            // GameStatsEntity stats = statsData.get();

            val clockStopped = game.clockStopped ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            var timeoutCalled = false
            if (offensiveTimeoutCalled || defensiveTimeoutCalled) {
                timeoutCalled = true
            }
            when (playCall) {
                PlayCall.PASS, PlayCall.RUN, PlayCall.SPIKE, PlayCall.KNEEL -> gamePlay = gamePlaysHandler.runNormalPlay(
                    gamePlay,
                    clockStopped,
                    game,
                    playCall,
                    runoffType,
                    timeoutCalled,
                    offensiveNumber.toString(),
                    decryptedDefensiveNumber
                )

                PlayCall.PAT, PlayCall.TWO_POINT -> gamePlay = gamePlaysHandler.runPointAfterPlay(
                    gamePlay,
                    game,
                    playCall,
                    offensiveNumber.toString(),
                    decryptedDefensiveNumber
                )

                PlayCall.KICKOFF_NORMAL, PlayCall.KICKOFF_ONSIDE, PlayCall.KICKOFF_SQUIB -> gamePlay = gamePlaysHandler.runKickoffPlay(
                    gamePlay,
                    game,
                    playCall,
                    offensiveNumber.toString(),
                    decryptedDefensiveNumber
                )

                PlayCall.FIELD_GOAL -> {}
                PlayCall.PUNT -> {}
            }

            val waitingOn = if (game.possession == TeamSide.HOME) TeamSide.AWAY else TeamSide.HOME

            val updated_game = gameDTO.updateGameInformation(
                game,
                gamePlay,
                playCall,
                clockStopped,
                offensiveTimeoutCalled,
                defensiveTimeoutCalled,
                waitingOn
            )
            // stats = gameStats.updateGameStats(stats, gamePlay);

            gameRepository.save(updated_game)
            // gameStatsRepository.save(stats);

            // Mark play as finished, set the timeouts, save the play
            gamePlay.playFinished = true
            playRepository.save(gamePlay)
            return ResponseEntity(gamePlay, HttpStatus.OK)
            // }
        } catch (e: Exception) {
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }
}
