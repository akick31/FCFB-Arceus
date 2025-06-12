package com.fcfb.arceus.controllers

import com.fcfb.arceus.controllers.SeasonController
import com.fcfb.arceus.domain.Season
import com.fcfb.arceus.service.fcfb.SeasonService
import com.fcfb.arceus.utils.GlobalExceptionHandler
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class SeasonControllerTest {

    private lateinit var mockMvc: MockMvc
    private val seasonService: SeasonService = mockk()
    private lateinit var seasonController: SeasonController

    @BeforeEach
    fun setup() {
        seasonController = SeasonController(seasonService)
        mockMvc = MockMvcBuilders.standaloneSetup(seasonController)
            .setControllerAdvice(GlobalExceptionHandler()) // Register the exception handler
            .build()
    }

    @Test
    fun `should start season successfully`() {
        val previousSeason = Season(
            seasonNumber = 10,
            startDate = "01/01/2023 00:00:00",
            endDate = "12/31/2023 23:59:59",
            nationalChampionshipWinningTeam = "Team A",
            nationalChampionshipLosingTeam = "Team B",
            nationalChampionshipWinningCoach = "Coach A",
            nationalChampionshipLosingCoach = "Coach B",
            currentWeek = 10,
            currentSeason = false
        )

        val newSeason = Season(
            seasonNumber = previousSeason.seasonNumber + 1,
            startDate = ZonedDateTime.now(ZoneId.of("America/New_York")).format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")),
            endDate = null,
            nationalChampionshipWinningTeam = null,
            nationalChampionshipLosingTeam = null,
            nationalChampionshipWinningCoach = null,
            nationalChampionshipLosingCoach = null,
            currentWeek = 1,
            currentSeason = true
        )

        every { seasonService.startSeason() } returns newSeason

        mockMvc.perform(post("/seasons").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.seasonNumber").value(newSeason.seasonNumber))
            .andExpect(jsonPath("$.startDate").value(newSeason.startDate))
            .andExpect(jsonPath("$.endDate").isEmpty)
            .andExpect(jsonPath("$.nationalChampionshipWinningTeam").isEmpty)
            .andExpect(jsonPath("$.nationalChampionshipLosingTeam").isEmpty)
            .andExpect(jsonPath("$.nationalChampionshipWinningCoach").isEmpty)
            .andExpect(jsonPath("$.nationalChampionshipLosingCoach").isEmpty)
            .andExpect(jsonPath("$.currentWeek").value(newSeason.currentWeek))
            .andExpect(jsonPath("$.currentSeason").value(newSeason.currentSeason))
    }

    @Test
    fun `should get current season successfully`() {
        val season = Season(
            seasonNumber = 1,
            startDate = ZonedDateTime.now(ZoneId.of("America/New_York")).format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")),
            endDate = null,
            nationalChampionshipWinningTeam = null,
            nationalChampionshipLosingTeam = null,
            nationalChampionshipWinningCoach = null,
            nationalChampionshipLosingCoach = null,
            currentWeek = 1,
            currentSeason = true
        )
        every { seasonService.getCurrentSeason() } returns season

        mockMvc.perform(get("/seasons/current").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.seasonNumber").value(season.seasonNumber))
            .andExpect(jsonPath("$.startDate").value(season.startDate))
            .andExpect(jsonPath("$.endDate").isEmpty)
            .andExpect(jsonPath("$.nationalChampionshipWinningTeam").isEmpty)
            .andExpect(jsonPath("$.nationalChampionshipLosingTeam").isEmpty)
            .andExpect(jsonPath("$.nationalChampionshipWinningCoach").isEmpty)
            .andExpect(jsonPath("$.nationalChampionshipLosingCoach").isEmpty)
            .andExpect(jsonPath("$.currentWeek").value(season.currentWeek))
            .andExpect(jsonPath("$.currentSeason").value(season.currentSeason))
    }

    @Test
    fun `should get current week successfully`() {
        val currentWeek = 5
        every { seasonService.getCurrentWeek() } returns currentWeek

        mockMvc.perform(get("/seasons/current/week").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").value(currentWeek))
    }

    @Test
    fun `should handle error when starting season`() {
        every { seasonService.startSeason() } throws RuntimeException("Failed to start season")

        mockMvc.perform(post("/seasons").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.error").value("Failed to start season"))
    }

    @Test
    fun `should handle error when getting current season`() {
        every { seasonService.getCurrentSeason() } throws RuntimeException("Current season not found")

        mockMvc.perform(get("/seasons/current").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.error").value("Current season not found"))
    }

    @Test
    fun `should handle error when getting current week`() {
        every { seasonService.getCurrentWeek() } throws RuntimeException("Current week not found")

        mockMvc.perform(get("/seasons/current/week").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.error").value("Current week not found"))
    }
}