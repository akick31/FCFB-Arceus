package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.domain.Drive
import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.GameStatus
import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.Scenario
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.domain.Team
import com.fcfb.arceus.service.fcfb.TeamService
import com.fcfb.arceus.utils.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.awt.AlphaComposite
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.properties.Delegates

@Component
class DriveSummaryGraphicService(
    private val teamService: TeamService,
    private val gameService: GameService,
    private val driveService: DriveService,
) {
    @Value("../app/images")
    private val imagePath: String? = null

    private val folderName = "${imagePath}/driveSummaryGraphics"

    private val fileSuffix = "png"

    private fun fileName(gameId: Int) = "${folderName}/${gameId}_drive_summary.${fileSuffix}"

    fun getDriveSummaryGraphicByGameId(gameId: Int): ResponseEntity<ByteArray> {
        val fileName = fileName(gameId)
        val driveSummaryGraphic = File(fileName).readBytes()

        // Set the response headers
        val headers =
            HttpHeaders().apply {
                contentType = MediaType.IMAGE_PNG
                contentLength = driveSummaryGraphic.size.toLong()
            }

        // Return the image in the response
        return ResponseEntity(driveSummaryGraphic, headers, HttpStatus.OK)
    }

    fun generateDriveSummaryGraphic(gameId: Int): String {
        val game = gameService.getGameById(gameId)

        generateDriveSummaryGraphic(game)

        // Return the image in the response
        Logger.info("Drive summary graphic generated for $gameId")
        return "Drive summary graphic generated for $gameId"
    }

    private fun generateDriveSummaryGraphic(game: Game): BufferedImage {
        val drive = driveService.getCurrentDrive(game.gameId)
        val plays = driveService.getAllPlaysByDriveId(drive.driveId)

        val homeTeam = teamService.getTeamByName(game.homeTeam)
        val awayTeam = teamService.getTeamByName(game.awayTeam)

        // Football field is 120 yds x 53.3 yds, scale by 3x on width, make height taller to allow more plays
        val endzoneWidth = 10
        val fieldWidth = 100 + (2 * endzoneWidth)
        val fieldHeight = 60
        val fieldScale = 3 // Change this if wanting bigger or smaller image
        val imageWidth = fieldWidth * fieldScale
        val imageHeight = fieldHeight * fieldScale

        val image = BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB)
        val g: Graphics2D = image.createGraphics()

        // Enable anti-aliasing for smoother text
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // Transparent background
        g.composite = AlphaComposite.Clear
        g.fillRect(0, 0, imageWidth, imageHeight)
        g.composite = AlphaComposite.Src

        // Draw the base field
        val homeEndzoneColor = Color.decode(homeTeam.primaryColor)
        val awayEndzoneColor = Color.decode(awayTeam.primaryColor)
        val fieldColor = Color.GREEN.darker()
        drawBaseField(g, fieldWidth, fieldHeight, fieldScale, homeEndzoneColor, awayEndzoneColor, fieldColor)

        // Draw the yard lines
        val yardLineColor = Color.WHITE
        val goalLineColor = Color.BLACK
        val yardLineInterval = 5  // Draw yard lines every 5 yards
        drawYardLines(g, fieldWidth, fieldHeight, endzoneWidth, fieldScale, yardLineInterval, yardLineColor, goalLineColor)

        // Draw the play lines
        val playLineSpacing = 2 // Every other horizontal line is a play line
        for ((playNumber, play) in plays.withIndex()) {
            drawPlayLine(play, playNumber + 1, playLineSpacing, g, endzoneWidth, fieldScale) // Need to add 1 to playNumber to give top space
        }

        // Create a new BufferedImage for the smaller version
        val scaledWidth = (imageWidth * 0.65).toInt() // 35% smaller
        val scaledHeight = (imageHeight * 0.65).toInt() // 35% smaller
        val scaledImage = BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB)
        val gScaled: Graphics2D = scaledImage.createGraphics()

        // Enable anti-aliasing for smoother scaling
        gScaled.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        gScaled.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)

        // Draw the original image onto the scaled image
        gScaled.drawImage(image, 0, 0, scaledWidth, scaledHeight, null)

        // Dispose graphics context
        g.dispose()
        gScaled.dispose()

        // Save image to file
        val fileName = fileName(game.gameId)
        val outputfile = File(fileName)

        // Create directory if it does not exist
        val directory = File(folderName)
        if (!directory.exists()) {
            // Create the directory and any necessary parent directories
            if (directory.mkdirs()) {
                // Logger.info("Directory created: ${directory.absolutePath}")
            } else {
                // Logger.error("Failed to create directory: ${directory.absolutePath}")
            }
        }

        ImageIO.write(scaledImage, fileSuffix, outputfile)
        return scaledImage
    }

    private fun drawBaseField(
        g: Graphics2D,
        fieldWidth: Int,  // Always 120
        fieldHeight: Int, // Always 60
        fieldScale: Int,
        homeEndzoneColor: Color,
        awayEndzoneColor: Color,
        fieldColor: Color,
    ) {
        val leftEndzoneEnd = 10
        val rightEndzoneStart = fieldWidth - 10

        // Draw home endzone (home is always left, moving towards right to score)
        g.color = homeEndzoneColor
        g.fillRect(0, 0, leftEndzoneEnd * fieldScale, fieldHeight * fieldScale)

        // Draw away endzone (away is always right, moving towards left to score)
        g.color = awayEndzoneColor
        g.fillRect(rightEndzoneStart * fieldScale, 0, fieldWidth * fieldScale, fieldHeight * fieldScale)

        // Draw the field between the endzones
        g.color = fieldColor
        g.fillRect(leftEndzoneEnd * fieldScale, 0, (rightEndzoneStart - leftEndzoneEnd) * fieldScale, fieldHeight * fieldScale)
    }

    private fun drawYardLines(
        g: Graphics2D,
        fieldWidth: Int,
        fieldHeight: Int,
        endzoneWidth: Int,
        fieldScale: Int,
        interval: Int,
        yardLineColor: Color,
        goalLineColor: Color,
    ) {
        val fieldStart = endzoneWidth
        val fieldEnd = fieldWidth - endzoneWidth
        val fieldLength = fieldEnd - fieldStart
        val lineWidth = BasicStroke(fieldScale.toFloat())
        val lineHeight = fieldHeight * fieldScale

        // Draw the goal lines
        g.color = goalLineColor
        g.stroke = lineWidth
        val leftGoalLineX = fieldStart * fieldScale
        val rightGoalLineX = fieldEnd * fieldScale
        g.drawLine(leftGoalLineX, 0, leftGoalLineX, lineHeight) // Left goal line
        g.drawLine(rightGoalLineX, 0, rightGoalLineX, lineHeight) // Right goal line

        // Draw the yard lines at the specified interval (skipping the left and right endzones)
        g.color = yardLineColor
        for (i in 0 until fieldLength step interval) {
            if (i == 0 || i == fieldLength) {
                continue // Skip the left and right goal lines
            }
            val xCoordinate = (fieldStart + i) * fieldScale
            g.drawLine(xCoordinate, 0, xCoordinate, lineHeight)
        }
    }

    private fun drawPlayLine(
        play: Play,
        playNumber: Int,
        playLineSpacing: Int,
        g: Graphics2D,
        endzoneWidth: Int,
        fieldScale: Int,
    ) {
        val playSummary = getPlaySummary(play)
        val playColor = playSummary.getPlayColor()
        val lineWidth = BasicStroke(fieldScale.toFloat())
        val playY = playNumber * playLineSpacing * fieldScale
        val playStartX = playSummary.getPlayStartingAbsoluteYardage(endzoneWidth) * fieldScale
        val playEndX = playSummary.getPlayEndingAbsoluteYardage(endzoneWidth) * fieldScale

        // Draw the play line
        g.color = playColor
        g.stroke = lineWidth
        g.drawLine(playStartX, playY, playEndX, playY)
    }

    private enum class PlayDirection {
        HOME_TEAM_ON_OFFENSE, // left endzone is home, moving towards right endzone
        AWAY_TEAM_ON_OFFENSE, // right endzone is away, moving towards left endzone
    }

    private enum class PlayDistanceType {
        GAIN,
        NO_GAIN,
        LOSS,
        TURNOVER,
        ENDZONE, // Scored a TD
        OTHER_ENDZONE, // Turnover TD
        FIELD_GOAL, // Field goal
    }

    private class PlaySummary {
        var startingYardsFromOppositeEndzone: Int
        var yardageChange: Int
        var playType: PlayCall?
        var playResultScenario: Scenario?
        var playResult: Game.ActualResult?
        var offense: TeamSide

        constructor(
            play: Play,
        ) {
            this.startingYardsFromOppositeEndzone = play.ballLocation
            this.yardageChange = play.yards
            this.playType = play.playCall
            this.playResultScenario = play.result
            this.playResult = play.actualResult
            this.offense = play.possession
        }

        fun isMadeFieldGoal(): Boolean {
            return playType == PlayCall.FIELD_GOAL && playResult == Game.ActualResult.GOOD
        }

        fun isMissedFieldGoal(): Boolean {
            return playType == PlayCall.FIELD_GOAL && playResult in listOf(Game.ActualResult.NO_GOOD, Game.ActualResult.BLOCKED)
        }

        fun isKickSixFieldGoal(): Boolean {
            return playType == PlayCall.FIELD_GOAL && playResult == Game.ActualResult.KICK_SIX
        }

        fun isPunt(): Boolean {
            return playType == PlayCall.PUNT
        }

        fun isStandardPunt(): Boolean {
            return isPunt() &&
                    !isBlockedPunt() &&
                    !isTouchbackPunt() &&
                    !isReturnTouchdownPunt() &&
                    !isMuffedPunt() &&
                    !isTouchdownPunt()
        }

        fun isBlockedPunt(): Boolean {
            return playType == PlayCall.PUNT && playResult == Game.ActualResult.BLOCKED
        }

        fun isTouchbackPunt(): Boolean {
            // Punt into endzone
            return playType == PlayCall.PUNT && playResultScenario == Scenario.TOUCHBACK
        }

        fun isReturnTouchdownPunt(): Boolean {
            // Punt, return team scores
            return playType == PlayCall.PUNT && playResult == Game.ActualResult.PUNT_RETURN_TOUCHDOWN
        }

        fun isMuffedPunt(): Boolean {
            // Punt, return team muffed, offense recovers
            return playType == PlayCall.PUNT && playResult == Game.ActualResult.MUFFED_PUNT
        }

        fun isTouchdownPunt(): Boolean {
            // Punt, return team muffed, offense recovers to score (it does happen)
            return playType == PlayCall.PUNT && playResult == Game.ActualResult.PUNT_TEAM_TOUCHDOWN
        }

        fun isPointAfter(): Boolean {
            return playType == PlayCall.PAT
        }

        fun isMadePointAfter(): Boolean {
            return playType == PlayCall.PAT && playResult == Game.ActualResult.GOOD
        }

        fun isMissedPointAfter(): Boolean {
            return playType == PlayCall.PAT && playResult == Game.ActualResult.NO_GOOD
        }

        fun isDefensivePointAfter(): Boolean {
            // Will this ever happen?
            return playType == PlayCall.PAT && playResult == Game.ActualResult.DEFENSE_TWO_POINT
        }

        fun isTwoPointAttempt(): Boolean {
            return playType == PlayCall.TWO_POINT
        }

        fun isSuccessfulTwoPointAttempt(): Boolean {
            return playType == PlayCall.TWO_POINT && playResult == Game.ActualResult.SUCCESS
        }

        fun isFailedTwoPointAttempt(): Boolean {
            return playType == PlayCall.TWO_POINT && playResult == Game.ActualResult.FAILED
        }

        fun isTurnoverTwoPointAttempt(): Boolean {
            return playType == PlayCall.TWO_POINT && playResult == Game.ActualResult.DEFENSE_TWO_POINT
        }

        fun isSpike(): Boolean {
            return playType == PlayCall.SPIKE
        }

        fun isKneel(): Boolean {
            return playType == PlayCall.KNEEL
        }

        fun isSafety(): Boolean {
            return playResultScenario == Scenario.SAFETY
        }

        fun isFumble(): Boolean {
            return playResultScenario == Scenario.FUMBLE
        }

        fun isTouchdown(): Boolean {
            return playResult == Game.ActualResult.TOUCHDOWN
        }

        fun isTurnoverTouchdown(): Boolean {
            return playResult == Game.ActualResult.TURNOVER_TOUCHDOWN
        }

        fun isCompletedPass(): Boolean {
            return playType == PlayCall.PASS && !isCompletedPassNoGain()
        }

        fun isCompletedPassNoGain(): Boolean {
            return playType == PlayCall.PASS && playResultScenario == Game.Scenario.NO_GAIN
        }

        fun isIncompletePass(): Boolean {
            return playType == PlayCall.PASS && playResultScenario == Game.Scenario.INCOMPLETE
        }

        fun isTouchdownPass(): Boolean {
            return playType == PlayCall.PASS && playResult == Game.ActualResult.TOUCHDOWN
        }

        fun isFumblePass(): Boolean {
            // Does this happen?
            return playType == PlayCall.PASS && playResultScenario == Game.Scenario.FUMBLE
        }

        fun isInterception(): Boolean {
            return playType == PlayCall.PASS && playResult == Game.ActualResult.TURNOVER
        }

        fun isPickSix(): Boolean {
            // Could technically be a fumble post-catch, but we'll assume it's a catch interception
            return playType == PlayCall.PASS && playResult == Game.ActualResult.TURNOVER_TOUCHDOWN
        }

        fun isRun(): Boolean {
            return playType == PlayCall.RUN
        }

        fun isRunNoGain(): Boolean {
            return playType == PlayCall.RUN && playResultScenario == Game.Scenario.NO_GAIN
        }

        fun isTouchdownRun(): Boolean {
            return playType == PlayCall.RUN && playResult == Game.ActualResult.TOUCHDOWN
        }

        fun isFumbleRun(): Boolean {
            return playType == PlayCall.RUN && playResultScenario == Game.Scenario.FUMBLE
        }

        fun isScoopAndScore(): Boolean {
            return playType == PlayCall.RUN && playResult == Game.ActualResult.TURNOVER_TOUCHDOWN
        }

        fun isSafetyRun(): Boolean {
            return playType == PlayCall.RUN && playResultScenario == Game.Scenario.SAFETY
        }

        fun getPlayDirection(): PlayDirection {
            return when (offense) {
                TeamSide.HOME -> PlayDirection.HOME_TEAM_ON_OFFENSE
                TeamSide.AWAY -> PlayDirection.AWAY_TEAM_ON_OFFENSE
            }
        }

        fun getPlayColor(): Color {
            return when (playType) {
                PlayCall.PASS -> Color.RED // Red for pass
                PlayCall.RUN -> Color.BLUE // Blue for run
                PlayCall.PUNT -> Color.BLACK // Black for punt (kicks to other team in general)
                PlayCall.SPIKE -> Color.MAGENTA // Magenta for spike
                PlayCall.KNEEL -> Color.CYAN // Cyan for kneel
                PlayCall.FIELD_GOAL -> when {
                    isMadeFieldGoal() -> Color.YELLOW // Yellow for made field goal
                    else -> Color.BLACK // Black for missed field goal
                }
                PlayCall.PAT -> when {
                    isMadePointAfter() -> Color.YELLOW // Yellow for made point after
                    else -> Color.BLACK // Black for missed point after
                }
                PlayCall.TWO_POINT -> when {
                    isSuccessfulTwoPointAttempt() -> Color.ORANGE // Orange for successful two-point attempt
                    else -> Color.BLACK // Black for failed two-point attempt
                }
                else -> Color.BLACK // Black for any other play type (shouldn't happen, because there aren't any other play types; ignoring kickoffs)
            }
        }

        fun getPlayDistanceType(): PlayDistanceType {
            return when {
                // Field goals
                isMadeFieldGoal() -> PlayDistanceType.FIELD_GOAL
                isMissedFieldGoal() -> PlayDistanceType.FIELD_GOAL
                isKickSixFieldGoal() -> PlayDistanceType.OTHER_ENDZONE
                // Punts
                isStandardPunt() -> PlayDistanceType.GAIN
                isBlockedPunt() -> PlayDistanceType.NO_GAIN
                isTouchbackPunt() -> PlayDistanceType.ENDZONE
                isReturnTouchdownPunt() -> PlayDistanceType.OTHER_ENDZONE
                isMuffedPunt() -> PlayDistanceType.GAIN
                isTouchdownPunt() -> PlayDistanceType.ENDZONE
                // PATs
                isMadePointAfter() -> PlayDistanceType.FIELD_GOAL
                isMissedPointAfter() -> PlayDistanceType.FIELD_GOAL
                isDefensivePointAfter() -> PlayDistanceType.OTHER_ENDZONE
                // Two-point attempts
                isSuccessfulTwoPointAttempt() -> PlayDistanceType.ENDZONE
                isFailedTwoPointAttempt() -> PlayDistanceType.ENDZONE
                isTurnoverTwoPointAttempt() -> PlayDistanceType.OTHER_ENDZONE
                // Touchdowns
                isTouchdown() -> PlayDistanceType.ENDZONE
                isTurnoverTouchdown() -> PlayDistanceType.OTHER_ENDZONE
                // Turnovers (non-scoring)
                isFumble() -> PlayDistanceType.TURNOVER // Could be net +/- yards
                isInterception() -> PlayDistanceType.TURNOVER // Could be net +/- yards
                // Special plays
                isSpike() -> PlayDistanceType.NO_GAIN
                isKneel() -> PlayDistanceType.NO_GAIN
                isSafety() -> PlayDistanceType.OTHER_ENDZONE
                // Passes (non-scoring)
                isCompletedPass() -> PlayDistanceType.GAIN
                isCompletedPassNoGain() -> PlayDistanceType.NO_GAIN
                isIncompletePass() -> PlayDistanceType.NO_GAIN
                // Runs (non-scoring)
                isRun() -> PlayDistanceType.GAIN
                isRunNoGain() -> PlayDistanceType.NO_GAIN
                // Default to no gain
                else -> PlayDistanceType.NO_GAIN
            }
        }

        /**
         * Get the yards from the back of the left endzone
         */
        fun getAbsoluteYardage(yardsFromOpposingEndzone: Int, endzoneWidth: Int): Int {
            // Add endzone width to the left side
            return when (getPlayDirection()) {
                PlayDirection.HOME_TEAM_ON_OFFENSE -> yardsFromOpposingEndzone + endzoneWidth  // Moving towards right endzone. If the team has gone 80 yards (at opposing 20), they are 80 + endzoneWidth from the back of the left endzone
                PlayDirection.AWAY_TEAM_ON_OFFENSE -> endzoneWidth + (100 - yardsFromOpposingEndzone)  // Moving towards left endzone. If the team has gone 80 yards (at opposing 20), they are endzoneWidth + (100 - 80) from the back of the left endzone
            }
        }

        fun getPlayStartingAbsoluteYardage(endzoneWidth: Int): Int {
            return getAbsoluteYardage(startingYardsFromOppositeEndzone, endzoneWidth)
        }

        fun getPlayEndingAbsoluteYardage(endzoneWidth: Int): Int {
            val distanceType = getPlayDistanceType()
            val yardageChange = when (distanceType) {
                PlayDistanceType.GAIN -> startingYardsFromOppositeEndzone + yardageChange
                PlayDistanceType.NO_GAIN -> startingYardsFromOppositeEndzone
                PlayDistanceType.LOSS -> startingYardsFromOppositeEndzone + yardageChange // Already negative
                PlayDistanceType.TURNOVER -> startingYardsFromOppositeEndzone + yardageChange // TODO: Audit this behavior
                PlayDistanceType.ENDZONE -> 100 // Take them to the endzone line
                PlayDistanceType.OTHER_ENDZONE -> 0 // Take them to the other endzone line
                PlayDistanceType.FIELD_GOAL -> 100 + endzoneWidth // Take them to the back of the endzone
            }
            return getAbsoluteYardage(yardageChange, endzoneWidth)
        }
    }

    private fun getPlaySummary(play: Play): PlaySummary {
        return PlaySummary(play)
    }
}
