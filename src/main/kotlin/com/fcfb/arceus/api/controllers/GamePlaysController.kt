package com.fcfb.arceus.api.controllers

import com.fcfb.arceus.domain.GamePlaysEntity
import com.fcfb.arceus.domain.OngoingGamesEntity
import com.fcfb.arceus.api.repositories.GamePlaysRepository
import com.fcfb.arceus.api.repositories.GameStatsRepository
import com.fcfb.arceus.api.repositories.OngoingGamesRepository
import com.fcfb.arceus.service.game.GameInformation
import com.fcfb.arceus.service.game.GameStats
import com.fcfb.arceus.service.game.GameUtils
import com.fcfb.arceus.service.game.PlayLogic
import com.fcfb.arceus.utils.EncryptionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/arceus/game_plays")
class GamePlaysController(
    private var playLogic: PlayLogic,
    private var gameInformation: GameInformation,
    private var gameStats: GameStats,
    private var encryptionUtils: EncryptionUtils,
    private var gameUtils: GameUtils
) {

    @Autowired
    var gamePlaysRepository: GamePlaysRepository? = null

    @Autowired
    var ongoingGamesRepository: OngoingGamesRepository? = null

    @Autowired
    var gameStatsRepository: GameStatsRepository? = null

    init {
        this.playLogic = playLogic
        this.gameInformation = gameInformation
        this.gameStats = gameStats
        this.encryptionUtils = encryptionUtils
        this.gameUtils = gameUtils
    }

    /**
     * Start a new play, the defensive number was submitted. The defensive number is encrypted
     * @param gameId
     * @param defensiveNumber
     * @return
     */
    @PostMapping("/defense_submitted")
    fun defensiveNumberSubmitted(
        @RequestParam("gameId") gameId: Int,
        @RequestParam("defensiveNumber") defensiveNumber: Int,
        @RequestParam("timeoutCalled") timeoutCalled: Boolean?
    ): ResponseEntity<GamePlaysEntity> {
        return try {
            val gameData: Optional<OngoingGamesEntity?> = ongoingGamesRepository?.findById(gameId) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
            if (!gameData.isPresent) {
                return ResponseEntity(null, HttpStatus.NOT_FOUND)
            }
            val offensiveSubmitter: String?
            val defensiveSubmitter: String?
            if (gameData.get().possession == "home") {
                offensiveSubmitter = gameData.get().homeCoach
                defensiveSubmitter = gameData.get().awayCoach
            } else {
                offensiveSubmitter = gameData.get().awayCoach
                defensiveSubmitter = gameData.get().homeCoach
            }
            val encryptedDefensiveNumber: String = encryptionUtils.encrypt(defensiveNumber.toString())
            val clock: Int = gameUtils.convertClockToSeconds(gameData.get().clock ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST))
            val gamePlay: GamePlaysEntity = gamePlaysRepository?.save(
                GamePlaysEntity(
                    gameId,
                    gameData.get().numPlays?.plus(1) ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST),
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
                    "None",
                    "None",
                    "None",
                    0,
                    0,
                    0,
                    gameData.get().winProbability,
                    gameData.get().homeTeam,
                    gameData.get().awayTeam,
                    0,
                    false,
                    gameData.get().homeTimeouts ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST),
                    gameData.get().awayTimeouts ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST),
                    false
                )
            ) ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST)
            val ongoingGamesEntity: OngoingGamesEntity = gameData.get()
            ongoingGamesEntity.currentPlayId = gamePlay.playId
            ongoingGamesRepository?.save(ongoingGamesEntity)
            ResponseEntity(gamePlay, HttpStatus.CREATED)
            
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.BAD_REQUEST)
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
    @PutMapping("/offense_submitted")
    fun offensiveNumberSubmitted(
        @RequestParam("playId") playId: Int,
        @RequestParam("offensiveNumber") offensiveNumber: Int,
        @RequestParam("playCall") playCall: String,
        @RequestParam("runoffType") runoffType: String,
        @RequestParam("offensiveTimeoutCalled") offensiveTimeoutCalled: Boolean,
        @RequestParam("defensiveTimeoutCalled") defensiveTimeoutCalled: Boolean
    ): ResponseEntity<GamePlaysEntity> {
        return try {
            val gamePlayData: Optional<GamePlaysEntity?> = gamePlaysRepository?.findById(playId) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
            if (!gamePlayData.isPresent) {
                return ResponseEntity(null, HttpStatus.NOT_FOUND)
            }
            val playCall = playCall.lowercase()

            val decryptedDefensiveNumber: String = encryptionUtils.decrypt(gamePlayData.get().defensiveNumber ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST))

            var gamePlay: GamePlaysEntity = gamePlayData.get()

            val gameData: Optional<OngoingGamesEntity?> = ongoingGamesRepository?.findById(gamePlay.gameId) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
            //Optional<GameStatsEntity> statsData = gameStatsRepository.findById(gamePlay.getGameId());

            //if (statsData.isPresent()) {
            var game: OngoingGamesEntity = gameData.get()
            //GameStatsEntity stats = statsData.get();

            val clockStopped: Boolean = game.clockStopped ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST)
            var timeoutCalled = false
            if (offensiveTimeoutCalled || defensiveTimeoutCalled) {
                timeoutCalled = true
            }
            when (playCall) {
                "pass", "run", "spike", "kneel" -> gamePlay = playLogic.runNormalPlay(
                    gamePlay,
                    clockStopped,
                    game,
                    playCall,
                    runoffType,
                    timeoutCalled,
                    offensiveNumber.toString(),
                    decryptedDefensiveNumber
                )

                "pat", "two point" -> gamePlay = playLogic.runPointAfterPlay(
                    gamePlay,
                    game,
                    playCall,
                    offensiveNumber.toString(),
                    decryptedDefensiveNumber
                )

                "kickoff normal", "kickoff onside", "kickoff squib" -> gamePlay = playLogic.runKickoffPlay(
                    gamePlay,
                    game,
                    playCall,
                    offensiveNumber.toString(),
                    decryptedDefensiveNumber
                )

                "field goal" -> {}
                "punt" -> {}
            }
            game = gameInformation.updateGameInformation(
                game,
                gamePlay,
                playCall,
                clockStopped,
                offensiveTimeoutCalled,
                defensiveTimeoutCalled
            )
            //stats = gameStats.updateGameStats(stats, gamePlay);

            // Send the defense the request for the number
            ongoingGamesRepository?.save(game)
            //gameStatsRepository.save(stats);

            // Mark play as finished, set the timeouts, save the play
            gamePlay.playFinished = true
            gamePlaysRepository?.save(gamePlay)
            return ResponseEntity(gamePlay, HttpStatus.OK)
            //}

        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
    }
}
