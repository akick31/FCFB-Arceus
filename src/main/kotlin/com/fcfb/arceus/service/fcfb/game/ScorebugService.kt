package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.GameStatus
import com.fcfb.arceus.domain.Game.TeamSide
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
import java.awt.LinearGradientPaint
import java.awt.RenderingHints
import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.net.URL
import javax.imageio.ImageIO

@Component
class ScorebugService(
    private val teamService: TeamService,
    private val gameService: GameService,
) {
    @Value("\${images.path}")
    private val imagePath: String? = null

    fun getScorebugByGameId(gameId: Int): ResponseEntity<ByteArray> {
        val game = gameService.getGameById(gameId)

        try {
            val scorebug = File("$imagePath/scorebugs/${game.gameId}_scorebug.png").readBytes()

            // Set the response headers
            val headers =
                HttpHeaders().apply {
                    contentType = MediaType.IMAGE_PNG
                    contentLength = scorebug.size.toLong()
                }

            // Return the image in the response
            return ResponseEntity(scorebug, headers, HttpStatus.OK)
        } catch (e: Exception) {
            Logger.error("Error fetching scorebug image: ${e.message}")
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    fun generateScorebug(gameId: Int): String {
        val game = gameService.getGameById(gameId)
        generateScorebug(game)

        // Return the image in the response
        Logger.info("Scorebug generated for $gameId")
        return "Scorebug generated for $gameId"
    }

    fun generateScorebug(game: Game): BufferedImage {
        val homeTeam = teamService.getTeamByName(game.homeTeam)
        val awayTeam = teamService.getTeamByName(game.awayTeam)

        val width = 360
        val height = 400
        val rowHeight = 70
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g: Graphics2D = image.createGraphics()

        // Line stroke width
        g.stroke = BasicStroke(2f)

        // Enable anti-aliasing for smoother text
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // Transparent background
        g.composite = AlphaComposite.Clear
        g.fillRect(0, 0, width, height)
        g.composite = AlphaComposite.Src

        // Adjusted row height for team name section (5 pixels shorter) and team score section (5 pixels taller)
        val adjustedRowHeightForTeamName = rowHeight - 5
        val adjustedRowHeightForTeamScore = rowHeight + 5

        // Draw the away team score section with adjusted height
        drawTeamScoreSection(g, game, awayTeam, 65, adjustedRowHeightForTeamScore)
        // Draw the home team score section with adjusted height
        drawTeamScoreSection(g, game, homeTeam, 205, adjustedRowHeightForTeamScore)

        // Draw the away team name section with adjusted height
        drawTeamNameSection(g, game, awayTeam, 0, adjustedRowHeightForTeamName)
        // Draw the home team name section with adjusted height
        drawTeamNameSection(g, game, homeTeam, 140, adjustedRowHeightForTeamName)

        drawClockInformationSection(g, rowHeight - 10, game, homeTeam, awayTeam)
        drawDownAndDistanceSection(g, rowHeight - 10, game)

        // Draw a border around the entire scorebug
        drawBorder(g, width, height)

        // Dispose graphics context
        g.dispose()

        // Draw the image
        g.drawImage(image, 0, 0, width, height, null)

        // Save image to file
        val outputfile = File("$imagePath/scorebugs/${game.gameId}_scorebug.png")

        // Create directory if it does not exist
        val directory = File("$imagePath/scorebugs")
        if (!directory.exists()) {
            // Create the directory and any necessary parent directories
            if (directory.mkdirs()) {
                Logger.info("Directory created: ${directory.absolutePath}")
            } else {
                Logger.error("Failed to create directory: ${directory.absolutePath}")
            }
        }

        ImageIO.write(image, "png", outputfile)
        return image
    }

    /**
     * Draws the team name section of the scorebug
     * @param g
     * @param team
     * @param yPos
     * @param rowHeight
     */
    private fun drawTeamNameSection(
        g: Graphics2D,
        game: Game,
        team: Team,
        yPos: Int,
        rowHeight: Int,
    ) {
        drawTeamSection(g, Color.decode(team.primaryColor), yPos, rowHeight)

        // Calculate the width of the text
        if (team.coachesPollRanking == 0) {
            var teamName = "${team.name}"
            g.font = Font("Helvetica", Font.PLAIN, 37)
            val textWidth = g.fontMetrics.stringWidth(teamName)
            if (textWidth > 245) {
                teamName = "${team.abbreviation}"
            }
            g.color = Color(255, 250, 220)
            g.font = Font("Helvetica", Font.PLAIN, 37)
            g.drawString(teamName, 10, yPos + rowHeight / 2 + 5)
        } else {
            val ranking = "${team.coachesPollRanking ?: ""}"
            var teamName = "${team.name}"
            g.font = Font("Helvetica", Font.PLAIN, 37) // Get the text width as if it was all the same size
            val textWidth = g.fontMetrics.stringWidth(ranking + teamName + 20)
            if (textWidth > 245) {
                teamName = "${team.abbreviation}"
            }
            // Reduce the font size for the ranking
            g.color = Color(255, 250, 220)
            g.font = Font("Helvetica", Font.PLAIN, 32)
            val rankingWidth = g.fontMetrics.stringWidth(ranking)
            g.drawString(ranking, 10, yPos + rowHeight / 2 + 5)

            g.color = Color(255, 250, 220)
            g.font = Font("Helvetica", Font.PLAIN, 37)
            g.drawString(teamName, rankingWidth + 20, yPos + rowHeight / 2 + 5)
        }

        // Draw the timeout boxes
        drawTimeoutBoxes(g, yPos + 5, rowHeight, if (team.name == game.homeTeam) game.homeTimeouts else game.awayTimeouts)

        // Draw the team record
        val record = "${team.currentWins}-${team.currentLosses}"
        g.font = Font("Helvetica", Font.PLAIN, 32)
        g.color = Color(255, 250, 220)
        g.drawString(record, 360 - 5 - g.fontMetrics.stringWidth(record), yPos + rowHeight / 2 + 10)

        // Horizontal line to span the entire width between teams
        g.color = Color.GRAY
        g.drawLine(0, 140, 360, 140)
    }

    private fun drawTimeoutBoxes(
        g: Graphics2D,
        yPos: Int,
        rowHeight: Int,
        timeouts: Int,
    ) {
        val boxWidth = 38
        val boxHeight = 7
        val boxSpacing = 7

        // Draw 3 boxes, left-aligned, with spacing
        for (i in 0 until 3) {
            val xPos = 10 + (i * (boxWidth + boxSpacing))

            // Set color based on remaining timeouts
            if (i < timeouts) {
                g.color = Color(255, 255, 80)
            } else {
                g.color = Color(211, 211, 211, 100)
            }

            // Draw the rectangle for the timeout box
            g.fillRect(xPos, yPos + rowHeight - boxHeight - 10, boxWidth, boxHeight)
        }
    }

    /**
     * Draws the team score section of the scorebug
     * @param g
     * @param team
     * @param yPos
     * @param rowHeight
     */
    private fun drawTeamScoreSection(
        g: Graphics2D,
        game: Game,
        team: Team,
        yPos: Int,
        rowHeight: Int,
    ) {
        drawTeamSection(g, Color.decode(team.primaryColor), yPos, rowHeight)

        // Draw the team logo
        val logoUrl = team.logo // Assume team.logoUrl is the URL of the team logo
        val logoWidth = 130
        val logoHeight = 130

        // Create a gradient for the shadow (dark color, fading to transparent)
        val shadowX = 245
        val shadowY = yPos + (rowHeight - 100)
        val shadowColor = Color(0, 0, 0, 100)
        val shadowGradient =
            LinearGradientPaint(
                Point2D.Float(shadowX.toFloat(), shadowY.toFloat()),
                Point2D.Float(285.toFloat(), shadowY.toFloat()),
                floatArrayOf(0f, 0.25f, 1f),
                arrayOf(shadowColor, shadowColor, Color(0, 0, 0, 0)),
            )
        g.composite = AlphaComposite.SrcOver
        g.paint = shadowGradient
        g.fillRect(shadowX, shadowY, logoWidth, logoHeight)

        if (logoUrl != null) {
            try {
                // Download the logo from the URL
                val logoImage = ImageIO.read(URL(logoUrl))

                // Calculate position: right-aligned in the 140px width
                val logoX = 245 + (115 - logoWidth) / 2
                val logoY = yPos + (rowHeight - 100)

                // Draw the logo image and overlay the box back over the logo if it spills over
                g.drawImage(logoImage, logoX, logoY, logoWidth, logoHeight, null)
                paintGradient(g, Color.decode(team.primaryColor), yPos, rowHeight)
                g.fillRect(0, yPos, 245, 75)
            } catch (e: IOException) {
                // Handle error if the logo cannot be loaded (e.g., invalid URL)
                Logger.error("Error loading logo for ${team.name}: ${e.message}")
            }
        }

        // Draw the team score and possession arrow
        val score = if (team.name == game.homeTeam) game.homeScore.toString() else game.awayScore.toString()

        g.color = Color(255, 250, 220)
        if (game.possession == TeamSide.AWAY && team.name == game.awayTeam && game.gameStatus != GameStatus.FINAL) {
            val unicodeChar = "\u25C0"
            val charHeight = g.fontMetrics.ascent

            g.font = Font("Helvetica", Font.BOLD, 75)
            var ascent = g.fontMetrics.ascent
            val width = g.fontMetrics.stringWidth(score)
            g.drawString(score, 10, (yPos - 2) + rowHeight / 2 + ascent / 2)

            // Reduce the font size for the Unicode character
            g.font = Font("Helvetica", Font.PLAIN, 35)
            ascent = g.fontMetrics.ascent
            g.drawString(
                unicodeChar,
                width + 10,
                (yPos + 10) + rowHeight / 2 + ascent / 2 - (charHeight / 2),
            )
        } else if (game.possession == TeamSide.HOME && team.name == game.homeTeam && game.gameStatus != GameStatus.FINAL) {
            val unicodeChar = "\u25C0"
            val charHeight = g.fontMetrics.ascent

            g.font = Font("Helvetica", Font.BOLD, 75)
            var ascent = g.fontMetrics.ascent
            val width = g.fontMetrics.stringWidth(score)
            g.drawString(score, 10, (yPos - 2) + rowHeight / 2 + ascent / 2)

            // Reduce the font size for the Unicode character
            g.font = Font("Helvetica", Font.PLAIN, 35)
            ascent = g.fontMetrics.ascent
            g.drawString(
                unicodeChar,
                width + 10,
                (yPos + 30) + rowHeight / 2 + ascent / 2 - (charHeight / 2),
            )
        } else {
            g.font = Font("Helvetica", Font.BOLD, 75)
            val ascent = g.fontMetrics.ascent
            g.drawString(score, 10, (yPos - 2) + rowHeight / 2 + ascent / 2)
        }

        // Draw the light gray line to separate the two sections
        g.color = Color.GRAY
        g.drawLine(245, yPos, 245, yPos + rowHeight)
    }

    /**
     * Draws the team section of the scorebug
     * @param g
     * @param color
     * @param yPos
     * @param rowHeight
     */
    private fun drawTeamSection(
        g: Graphics2D,
        color: Color,
        yPos: Int,
        rowHeight: Int,
    ) {
        paintGradient(g, color, yPos, rowHeight)
        // Draw the right section box for the row
        g.fillRect(240, yPos, 120, rowHeight)
        g.drawRect(240, yPos, 120, rowHeight)
        // Draw the left section box for the row
        g.fillRect(0, yPos, 260, rowHeight)
        g.drawRect(0, yPos, 260, rowHeight)
    }

    /**
     * Draws a border around the entire scorebug
     * @param g
     * @param width
     * @param height
     */
    private fun drawBorder(
        g: Graphics2D,
        width: Int,
        height: Int,
    ) {
        g.color = Color.GRAY
        g.drawLine(0, 0, width, 0) // Top border
        g.drawLine(0, 0, 0, height) // Left border
        g.drawLine(width - 1, 0, width - 1, height) // Right border
        g.drawLine(0, height - 1, width, height - 1) // Bottom border
    }

    /**
     * Paints a gradient background for the section of the scorebug
     * @param g
     * @param color
     * @param yPos
     * @param rowHeight
     */
    private fun paintGradient(
        g: Graphics2D,
        color: Color,
        yPos: Int,
        rowHeight: Int,
    ) {
        // Create a gradient background from the team's primary color to a slightly darker color
        val startColor = color
        val endColor = startColor.darker()

        // Define the start and end points for the gradient (top to bottom)
        val gradient =
            LinearGradientPaint(
                Point2D.Float(0f, yPos.toFloat()),
                Point2D.Float(0f, (yPos + rowHeight).toFloat()),
                floatArrayOf(0f, 1f),
                arrayOf(startColor, endColor),
            )
        g.paint = gradient
    }

    /**
     * Draws the clock information section of the scorebug
     * @param g
     * @param rowHeight
     */
    private fun drawClockInformationSection(
        g: Graphics2D,
        rowHeight: Int,
        game: Game,
        homeTeam: Team,
        awayTeam: Team,
    ) {
        val rowY = 280

        // Draw Quarter Section
        var xPos = 0
        g.color = Color(255, 250, 220)
        g.fillRect(xPos, rowY, 126, rowHeight)
        g.color = Color.GRAY

        // Draw Quarter text
        val quarterText = getQuarterText(game.quarter)
        g.font = Font("Helvetica", Font.PLAIN, 40)
        val quarterTextAscent = g.fontMetrics.ascent
        g.color = Color.BLACK
        g.drawString(
            quarterText,
            xPos + (115 - g.fontMetrics.stringWidth(quarterText)) / 2,
            rowY + rowHeight / 2 + quarterTextAscent / 2,
        )

        // Draw Clock Section
        xPos = 115
        g.color = Color(255, 250, 220)
        g.fillRect(xPos, rowY, 130, rowHeight)
        g.color = Color.GRAY

        // Draw Clock text
        val clockText = getClockText(game.quarter, game.clock)
        g.font = Font("Helvetica", Font.PLAIN, 40)
        val clockTextAscent = g.fontMetrics.ascent
        g.color = Color.BLACK
        g.drawString(
            clockText,
            xPos + (130 - g.fontMetrics.stringWidth(clockText)) / 2,
            rowY + rowHeight / 2 + clockTextAscent / 2,
        )

        // Draw Ball Location Section
        xPos = 245
        g.color = Color(255, 250, 220)
        g.fillRect(xPos, rowY, 125, rowHeight)

        // Draw Ball Location text
        val ballLocationText =
            getBallLocationText(
                homeTeam.name ?: "",
                awayTeam.name ?: "",
                homeTeam.abbreviation,
                awayTeam.abbreviation,
                game.ballLocation,
                game.possession,
            )
        var fontSize = 40
        g.font = Font("Helvetica", Font.PLAIN, fontSize)

        // Calculate the width of the text
        var textWidth = g.fontMetrics.stringWidth(ballLocationText)
        val ballLocationTextAscent = g.fontMetrics.ascent

        // Decrease font size if text overflows
        while (textWidth > 110 && fontSize > 10) {
            fontSize -= 2
            g.font = Font("Helvetica", Font.PLAIN, fontSize)
            textWidth = g.fontMetrics.stringWidth(ballLocationText)
        }

        g.color = Color.BLACK
        g.drawString(ballLocationText, xPos + (115 - textWidth) / 2, rowY + (rowHeight - 5) / 2 + ballLocationTextAscent / 2)

        // Vertical line to separate Quarter and Clock sections
        val verticalLineX = 115
        g.color = Color.GRAY
        g.drawLine(verticalLineX, rowY + rowHeight, verticalLineX, rowY + (rowHeight / 2))

        // Vertical line to separate Clock and Ball Location sections
        val verticalLineX2 = 240
        g.color = Color.GRAY
        g.drawLine(verticalLineX2, rowY + rowHeight, verticalLineX2, rowY + (rowHeight / 2))

        // Top horizontal line to span the entire width of the section
        g.color = Color.GRAY
        g.drawLine(0, rowY, 360, rowY)

        // Bottom horizontal line to span the entire width of the section
        g.color = Color.GRAY
        g.drawLine(0, rowY + rowHeight, 360, rowY + rowHeight)
    }

    /**
     * Draws the down and distance section of the scorebug
     * @param g
     * @param rowHeight
     */
    private fun drawDownAndDistanceSection(
        g: Graphics2D,
        rowHeight: Int,
        game: Game,
    ) {
        // Draw Down & Distance Section
        val rowY = 340
        g.color = Color(255, 250, 220)
        g.fillRect(0, rowY, 360, rowHeight)
        g.color = Color.GRAY
        g.drawRect(0, rowY, 360, rowHeight)

        // Draw Down & Distance text
        if (game.gameStatus != GameStatus.FINAL) {
            val downDistanceText = getDownDistanceText(game.down, game.yardsToGo, game.ballLocation)
            g.font = Font("Helvetica", Font.BOLD, 43)
            val ascent = g.fontMetrics.ascent
            g.color = Color.BLACK
            g.drawString(downDistanceText, 10, rowY + rowHeight / 2 + ascent / 2)
        } else {
            g.font = Font("Helvetica", Font.BOLD, 43)
            val ascent = g.fontMetrics.ascent
            val textWidth = g.fontMetrics.stringWidth("Final")
            g.color = Color.BLACK
            g.drawString("Final", 165 - textWidth / 2, rowY + rowHeight / 2 + ascent / 2)
        }
    }

    /**
     * Gets the quarter text for the scorebug
     * @param quarter
     */
    private fun getQuarterText(quarter: Int): String {
        var quarterText =
            when (quarter) {
                5 -> "OT"
                4 -> "4th"
                3 -> "3rd"
                2 -> "2nd"
                1 -> "1st"
                else -> "Unknown"
            }
        quarterText =
            if (quarter >= 6) {
                "${quarter - 4} OT"
            } else {
                quarterText
            }
        return quarterText
    }

    /**
     * Gets the clock text for the scorebug
     * @param quarter
     * @param clock
     */
    private fun getClockText(
        quarter: Int,
        clock: String,
    ): String {
        return if (quarter >= 5) {
            ""
        } else {
            clock
        }
    }

    /**
     * Gets the down and distance text for the scorebug
     * @param down
     * @param yardsToGo
     * @param ballLocation
     */
    private fun getDownDistanceText(
        down: Int,
        yardsToGo: Int,
        ballLocation: Int,
    ): String {
        return when (down) {
            1 -> "1st"
            2 -> "2nd"
            3 -> "3rd"
            4 -> "4th"
            else -> down.toString()
        } + " & " +
            when {
                (ballLocation.plus(yardsToGo)) >= 100 -> "Goal"
                else -> yardsToGo.toString()
            }
    }

    /**
     * Gets the ball location text for the scorebug
     * @param homeTeamName
     * @param awayTeamName
     * @param homeTeamAbbreviation
     * @param awayTeamAbbreviation
     * @param ballLocation
     * @param possession
     */
    private fun getBallLocationText(
        homeTeamName: String,
        awayTeamName: String,
        homeTeamAbbreviation: String?,
        awayTeamAbbreviation: String?,
        ballLocation: Int,
        possession: TeamSide,
    ): String {
        return when {
            ballLocation == 50 -> "50 yard line"
            ballLocation < 50 &&
                possession == TeamSide.HOME ->
                if (homeTeamAbbreviation != awayTeamAbbreviation) {
                    "${homeTeamAbbreviation ?: homeTeamName.uppercase()} $ballLocation"
                } else {
                    "${homeTeamName.uppercase()} $ballLocation"
                }
            ballLocation < 50 &&
                possession == TeamSide.AWAY ->
                if (homeTeamAbbreviation != awayTeamAbbreviation) {
                    "${awayTeamAbbreviation ?: awayTeamName.uppercase()} $ballLocation"
                } else {
                    "${awayTeamName.uppercase()} $ballLocation"
                }
            ballLocation > 50 &&
                possession == TeamSide.HOME ->
                if (homeTeamAbbreviation != awayTeamAbbreviation) {
                    "${awayTeamAbbreviation ?: awayTeamName.uppercase()} ${100 - ballLocation}"
                } else {
                    "${awayTeamName.uppercase()} $ballLocation"
                }
            ballLocation > 50 &&
                possession == TeamSide.AWAY ->
                if (homeTeamAbbreviation != awayTeamAbbreviation) {
                    "${homeTeamAbbreviation ?: homeTeamName.uppercase()} ${100 - ballLocation}"
                } else {
                    "${homeTeamName.uppercase()} $ballLocation"
                }
            else -> "Unknown Location"
        }
    }
}
