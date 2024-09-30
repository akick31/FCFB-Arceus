package com.fcfb.arceus.service.game

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.TeamSide
import com.fcfb.arceus.repositories.GameRepository
import com.fcfb.arceus.repositories.TeamRepository
import com.fcfb.arceus.utils.Logger
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

@Component
class ScorebugService(
    private val teamRepository: TeamRepository,
    private val gameRepository: GameRepository
) {
    fun getScorebugByGameId(
        gameId: Int
    ) {
        val game = gameRepository.findByGameId(gameId) ?: return
        generateScorebug(game)
    }

    fun generateScorebug(game: Game) {
        val homeTeam = teamRepository.findByName(game.homeTeam) ?: return
        val awayTeam = teamRepository.findByName(game.awayTeam) ?: return

        val width = 500 // Width of the image
        val height = 400 // Height to accommodate additional boxes
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g: Graphics2D = image.createGraphics()

        // Enable anti-aliasing for smoother text
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        // Transparent background
        g.composite = AlphaComposite.Clear
        g.fillRect(0, 0, width, height)
        g.composite = AlphaComposite.Src

        // Box dimensions
        val teamBoxWidth = 250
        val infoBoxHeight = 75 // Height for team, score, and clock info boxes
        val scoreBoxWidth = 80
        val clockInfoBoxWidth = scoreBoxWidth + 50 // Width for the clock info box
        val bottomBoxHeight = 50 // Height for the bottom info box
        val homeTeamY = 50 // Y position for the home team box row
        val awayTeamY = homeTeamY + infoBoxHeight  // Y position for the away team box row
        val bottomBoxY = awayTeamY + infoBoxHeight // Y position for the down & distance box
        val teamNameX = 50 // X position for the team name
        val scoreX = 50 + teamBoxWidth - 25 // X position for the score
        val clockInfoBoxX = scoreX + scoreBoxWidth // X position for the clock info box

        // Draw Home Team Box
        g.color = Color.decode(homeTeam.primaryColor)
        g.fillRect(teamNameX, homeTeamY, teamBoxWidth, infoBoxHeight) // Home team box

        // Draw Home Score Box
        g.color = Color.decode(homeTeam.primaryColor).darker()
        g.fillRect(scoreX, homeTeamY, scoreBoxWidth, infoBoxHeight) // Home score box

        // Draw Away Team Box
        g.color = Color.decode(awayTeam.primaryColor)
        g.fillRect(teamNameX, awayTeamY, teamBoxWidth, infoBoxHeight) // Away team box

        // Draw Away Score Box
        g.color = Color.decode(awayTeam.primaryColor).darker()
        g.fillRect(scoreX, awayTeamY, scoreBoxWidth, infoBoxHeight) // Away score box

        // Draw Possession Indicators as a white circle
        if (game.possession == TeamSide.HOME) {
            drawCircle(g, teamBoxWidth, homeTeamY + infoBoxHeight / 2) // Home possession
        } else if (game.possession == TeamSide.AWAY) {
            drawCircle(g, teamBoxWidth, awayTeamY + infoBoxHeight / 2) // Away possession
        }

        // Font setup for team names and scores
        // Load the custom font
        val customFontPath = "/Proxima Nova Bold.otf" // Path relative to the resources folder
        val fontInputStream = this.javaClass.getResourceAsStream(customFontPath)

        try {
            g.font = Font.createFont(Font.TRUETYPE_FONT, fontInputStream).deriveFont(35f)
        } catch (e: Exception) {
            Logger.info("Error loading custom font: ${e.message}")
            g.font = Font("Arial", Font.BOLD, 35)
        }

        // Draw Home Team Name
        g.color = Color.WHITE
        drawCenteredText(g, game.homeTeam.toString(), teamNameX, homeTeamY - 10, teamBoxWidth - 30, infoBoxHeight)

        // Draw Home Team Score
        g.color = Color.WHITE
        drawCenteredText(g, game.homeScore.toString(), scoreX, homeTeamY, scoreBoxWidth, infoBoxHeight)

        // Draw Away Team Name
        g.color = Color.WHITE
        drawCenteredText(g, game.awayTeam.toString(), teamNameX, awayTeamY - 10, teamBoxWidth - 30, infoBoxHeight)

        // Draw Away Team Score
        g.color = Color.WHITE
        drawCenteredText(g, game.awayScore.toString(), scoreX, awayTeamY, scoreBoxWidth, infoBoxHeight)

        // Draw Quarter Box
        val quarterText = when (game.quarter) {
            5 -> "OT" // Overtime
            4 -> "4th"
            3 -> "3rd"
            2 -> "2nd"
            1 -> "1st"
            else -> "Unknown"
        }
        g.color = Color.DARK_GRAY.darker()
        g.fillRect(clockInfoBoxX, homeTeamY, clockInfoBoxWidth, infoBoxHeight) // Quarter box
        g.color = Color.WHITE
        drawCenteredText(g, quarterText, clockInfoBoxX, homeTeamY, clockInfoBoxWidth, infoBoxHeight) // Draw clock text

        // Draw Clock Box
        g.color = Color.DARK_GRAY
        g.fillRect(clockInfoBoxX, awayTeamY, clockInfoBoxWidth, infoBoxHeight) // Clock box
        g.color = Color.WHITE
        drawCenteredText(g, game.clock.toString(), clockInfoBoxX, awayTeamY, clockInfoBoxWidth, infoBoxHeight) // Draw clock text

        // Draw Down & Distance Box
        try {
            g.font = Font.createFont(Font.TRUETYPE_FONT, fontInputStream).deriveFont(30f)
        } catch (e: Exception) {
            Logger.info("Error loading custom font: ${e.message}")
            g.font = Font("Arial", Font.BOLD, 30)
        }
        val downDistanceText = when (game.down) {
            1 -> "1st"
            2 -> "2nd"
            3 -> "3rd"
            4 -> "4th"
            else -> game.down.toString()
        } + " & " + when {
            (game.ballLocation?.plus(game.yardsToGo ?: 0) ?: 0) >= 100 -> "Goal"
            else -> game.yardsToGo.toString()
        }

        g.color = Color.DARK_GRAY.darker()
        g.fillRect(teamNameX, bottomBoxY, teamBoxWidth + scoreBoxWidth, bottomBoxHeight) // Down & distance box
        g.color = Color.WHITE
        drawCenteredText(g, downDistanceText, teamNameX, bottomBoxY, teamBoxWidth + scoreBoxWidth - 30, bottomBoxHeight) // Center the down and distance text

        // Draw Ball Location Box
        g.color = Color.DARK_GRAY.darker()
        g.fillRect(clockInfoBoxX, bottomBoxY, clockInfoBoxWidth, bottomBoxHeight) // Ball location box

        // Determine ball location text
        try {
            g.font = Font.createFont(Font.TRUETYPE_FONT, fontInputStream).deriveFont(20f)
        } catch (e: Exception) {
            g.font = Font("Arial", Font.BOLD, 20)
        }
        val ballLocationText = when {
            game.ballLocation == 50 -> "50 yard line"
            game.ballLocation != null && game.ballLocation!! < 50 && game.possession == TeamSide.HOME -> "${homeTeam.abbreviation ?: homeTeam.name} ${game.ballLocation}"
            game.ballLocation != null && game.ballLocation!! < 50 && game.possession == TeamSide.AWAY -> "${awayTeam.abbreviation ?: awayTeam.name} ${game.ballLocation}"
            game.ballLocation != null && game.ballLocation!! > 50 && game.possession == TeamSide.AWAY -> "${awayTeam.abbreviation ?: awayTeam.name} ${100 - game.ballLocation!!}"
            game.ballLocation != null && game.ballLocation!! > 50 && game.possession == TeamSide.HOME -> "${homeTeam.abbreviation ?: homeTeam.name} ${100 - game.ballLocation!!}"
            else -> "Unknown Location"
        }

        g.color = Color.WHITE
        drawCenteredText(g, ballLocationText, clockInfoBoxX, bottomBoxY, clockInfoBoxWidth, bottomBoxHeight) // Center the ball location text

        // Draw Timeout Boxes for Home Team
        drawTimeoutBoxes(g, homeTeamY + infoBoxHeight - 10, game.homeTimeouts!!)

        // Draw Timeout Boxes for Away Team
        drawTimeoutBoxes(g, awayTeamY + infoBoxHeight - 10, game.awayTimeouts!!)

        // Draw border below team scores
        g.color = Color.GRAY
        g.stroke = BasicStroke(3f)
        g.drawLine(teamNameX + 1, bottomBoxY, clockInfoBoxX + clockInfoBoxWidth - 2, bottomBoxY)

        // Dispose graphics context
        g.dispose()

        // Save image to file
        ImageIO.write(image, "png", File("scorebug.png"))
    }

    /**
     * Draws timeout boxes for the given team.
     */
    private fun drawTimeoutBoxes(g: Graphics2D, timeoutY: Int, timeouts: Int) {
        g.color = Color.WHITE // Change to desired color for timeouts
        val boxWidth = 35 // Adjust width as needed
        val boxHeight = 5 // Adjust height as needed

        for (i in 0 until timeouts) {
            // Calculate the x position for each timeout box
            val xPos = (235 - (i * (boxWidth + 5))) // Adjust spacing as needed
            // Draw each timeout box
            g.fillRect(xPos, timeoutY, boxWidth, boxHeight) // Adjust y position as needed
        }
    }

    private fun drawCircle(g: Graphics2D, x: Int, y: Int) {
        val radius = 5 // Radius of the circle
        g.color = Color.WHITE // Circle color for possession
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2) // Draw circle
    }

    fun drawCenteredText(g: Graphics2D, text: String, x: Int, y: Int, boxWidth: Int, boxHeight: Int) {
        val metrics = g.fontMetrics
        val textWidth = metrics.stringWidth(text)
        val textHeight = metrics.height

        // Scale the font if the text is too wide for the box
        if (textWidth > boxWidth) {
            g.font = g.font.deriveFont((g.font.size * boxWidth.toFloat() / textWidth))
        }

        // Recalculate after scaling (if applied)
        val updatedMetrics = g.fontMetrics
        val updatedTextWidth = updatedMetrics.stringWidth(text)
        val updatedTextHeight = updatedMetrics.ascent  // For vertical centering

        val centerX = x + (boxWidth - updatedTextWidth) / 2
        val centerY = y + (boxHeight - updatedTextHeight) / 2 + updatedTextHeight // Adjust for vertical centering
        g.drawString(text, centerX, centerY)
    }
}