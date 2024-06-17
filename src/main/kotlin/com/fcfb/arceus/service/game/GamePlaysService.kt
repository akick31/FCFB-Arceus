package com.fcfb.arceus.service.game

import com.fcfb.arceus.api.repositories.GamePlaysRepository
import com.fcfb.arceus.api.repositories.OngoingGamesRepository
import com.fcfb.arceus.domain.GamePlaysEntity
import com.fcfb.arceus.domain.OngoingGamesEntity
import com.fcfb.arceus.models.game.Game
import com.fcfb.arceus.dto.GameDTO
import com.fcfb.arceus.utils.GameUtils
import com.fcfb.arceus.utils.EncryptionUtils
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class GamePlaysService(
    private var ongoingGamesRepository: OngoingGamesRepository,
    private var gamePlaysRepository: GamePlaysRepository,
    private var gameDTO: GameDTO,
    private var gameUtils: GameUtils,
    private var encryptionUtils: EncryptionUtils,
    private var gamePlaysHandler: GamePlaysHandler
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
    ): ResponseEntity<GamePlaysEntity> {
        return try {
            val gameData: Optional<OngoingGamesEntity?> = ongoingGamesRepository.findById(gameId) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
            if (!gameData.isPresent) {
                return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
            }
            val offensiveSubmitter: String?
            val defensiveSubmitter: String?
            if (gameData.get().possession == Game.Possession.HOME) {
                offensiveSubmitter = gameData.get().homeCoach
                defensiveSubmitter = gameData.get().awayCoach
            } else {
                offensiveSubmitter = gameData.get().awayCoach
                defensiveSubmitter = gameData.get().homeCoach
            }
            val encryptedDefensiveNumber: String = encryptionUtils.encrypt(defensiveNumber.toString())
            val clock: Int = gameUtils.convertClockToSeconds(gameData.get().clock ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST))
            val gamePlay: GamePlaysEntity = gamePlaysRepository.save(
                GamePlaysEntity(
                    gameId,
                    gameData.get().numPlays?.plus(1) ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST),
                    gameData.get().homeScore,
                    gameData.get().awayScore,
                    gameData.get().quarter,
                    clock,
                    gameData.get().ballLocation,
                    gameData.get().possession,
                    gameData.get().down,
                    gameData.get().yardsToGo,
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
                    gameData.get().winProbability,
                    gameData.get().homeTeam,
                    gameData.get().awayTeam,
                    0,
                    false,
                    gameData.get().homeTimeouts ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST),
                    gameData.get().awayTimeouts ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST),
                    false
                )
            ) ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val ongoingGamesEntity: OngoingGamesEntity = gameData.get()
            ongoingGamesEntity.currentPlayId = gamePlay.playId
            ongoingGamesRepository.save(ongoingGamesEntity)
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
        playCall: Game.Play,
        runoffType: Game.RunoffType,
        offensiveTimeoutCalled: Boolean,
        defensiveTimeoutCalled: Boolean
    ): ResponseEntity<GamePlaysEntity> {
        return try {
            val gamePlayData: Optional<GamePlaysEntity?> = gamePlaysRepository.findById(playId) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
            if (!gamePlayData.isPresent) {
                return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
            }

            val decryptedDefensiveNumber: String = encryptionUtils.decrypt(gamePlayData.get().defensiveNumber ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST))

            var gamePlay: GamePlaysEntity = gamePlayData.get()

            val gameData: Optional<OngoingGamesEntity?> = ongoingGamesRepository.findById(gamePlay.gameId) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
            //Optional<GameStatsEntity> statsData = gameStatsRepository.findById(gamePlay.getGameId());

            //if (statsData.isPresent()) {
            var game: OngoingGamesEntity = gameData.get()
            //GameStatsEntity stats = statsData.get();

            val clockStopped: Boolean = game.clockStopped ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            var timeoutCalled = false
            if (offensiveTimeoutCalled || defensiveTimeoutCalled) {
                timeoutCalled = true
            }
            when (playCall) {
                Game.Play.PASS, Game.Play.RUN, Game.Play.SPIKE, Game.Play.KNEEL -> gamePlay = gamePlaysHandler.runNormalPlay(
                    gamePlay,
                    clockStopped,
                    game,
                    playCall,
                    runoffType,
                    timeoutCalled,
                    offensiveNumber.toString(),
                    decryptedDefensiveNumber
                )

                Game.Play.PAT, Game.Play.TWO_POINT -> gamePlay = gamePlaysHandler.runPointAfterPlay(
                    gamePlay,
                    game,
                    playCall,
                    offensiveNumber.toString(),
                    decryptedDefensiveNumber
                )

                Game.Play.KICKOFF_NORMAL, Game.Play.KICKOFF_ONSIDE, Game.Play.KICKOFF_SQUIB -> gamePlay = gamePlaysHandler.runKickoffPlay(
                    gamePlay,
                    game,
                    playCall,
                    offensiveNumber.toString(),
                    decryptedDefensiveNumber
                )

                Game.Play.FIELD_GOAL -> {}
                Game.Play.PUNT -> {}
            }
            game = gameDTO.updateGameInformation(
                game,
                gamePlay,
                playCall,
                clockStopped,
                offensiveTimeoutCalled,
                defensiveTimeoutCalled
            )
            //stats = gameStats.updateGameStats(stats, gamePlay);

            // Send the defense the request for the number
            ongoingGamesRepository.save(game)
            //gameStatsRepository.save(stats);

            // Mark play as finished, set the timeouts, save the play
            gamePlay.playFinished = true
            gamePlaysRepository.save(gamePlay)
            return ResponseEntity(gamePlay, HttpStatus.OK)
            //}

        } catch (e: Exception) {
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }


}