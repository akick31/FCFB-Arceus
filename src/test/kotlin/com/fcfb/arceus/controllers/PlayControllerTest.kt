package com.fcfb.arceus.controllers

import com.fcfb.arceus.domain.enums.ActualResult
import com.fcfb.arceus.domain.enums.PlayCall
import com.fcfb.arceus.domain.enums.RunoffType
import com.fcfb.arceus.domain.enums.Scenario
import com.fcfb.arceus.domain.enums.TeamSide
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.service.fcfb.PlayService
import com.fcfb.arceus.utils.GlobalExceptionHandler
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class PlayControllerTest {
    private lateinit var mockMvc: MockMvc
    private val playService: PlayService = mockk()
    private lateinit var playController: PlayController

    @BeforeEach
    fun setup() {
        playController = PlayController(playService)
        mockMvc =
            MockMvcBuilders.standaloneSetup(playController)
                .setControllerAdvice(GlobalExceptionHandler())
                .build()
    }

    private fun createSamplePlay(
        gameId: Int = 1,
        playNumber: Int = 1,
    ): Play {
        return Play(
            gameId = gameId,
            playNumber = playNumber,
            homeScore = 0,
            awayScore = 0,
            quarter = 1,
            clock = 420,
            ballLocation = 20,
            possession = TeamSide.HOME,
            down = 1,
            yardsToGo = 10,
            defensiveNumber = "B22",
            offensiveNumber = "A10",
            offensiveSubmitter = "coachA",
            defensiveSubmitter = "coachB",
            playCall = PlayCall.RUN,
            result = Scenario.NO_GAIN,
            actualResult = ActualResult.NO_GAIN,
            yards = 5,
            playTime = 5,
            runoffTime = 0,
            winProbability = 0.5,
            winProbabilityAdded = 0.01,
            homeTeam = "Team A",
            awayTeam = "Team B",
            difference = 0,
            timeoutUsed = false,
            offensiveTimeoutCalled = false,
            defensiveTimeoutCalled = false,
            homeTimeouts = 3,
            awayTimeouts = 3,
            playFinished = true,
            offensiveResponseSpeed = 1000L,
            defensiveResponseSpeed = 1200L,
        )
    }

    @Test
    fun `should get play by id`() {
        val play = createSamplePlay()
        every { playService.getPlayById(1) } returns play

        mockMvc.perform(get("/play").param("playId", "1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.gameId").value(1))
    }

    @Test
    fun `should get all plays by game id`() {
        val plays = listOf(createSamplePlay(1, 1), createSamplePlay(1, 2))
        every { playService.getAllPlaysByGameId(1) } returns plays

        mockMvc.perform(get("/play/all").param("gameId", "1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].playNumber").value(1))
            .andExpect(jsonPath("$[1].playNumber").value(2))
    }

    @Test
    fun `should get previous play`() {
        val play = createSamplePlay()
        every { playService.getPreviousPlay(1) } returns play

        mockMvc.perform(get("/play/previous").param("gameId", "1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.playNumber").value(1))
    }

    @Test
    fun `should get current play`() {
        val play = createSamplePlay()
        every { playService.getCurrentPlay(1) } returns play

        mockMvc.perform(get("/play/current").param("gameId", "1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.playNumber").value(1))
    }

    @Test
    fun `should get all plays by discord tag`() {
        val play = createSamplePlay()
        every { playService.getAllPlaysByDiscordTag("coachA#1234") } returns listOf(play)

        mockMvc.perform(get("/play/all/user").param("discordTag", "coachA#1234"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].offensiveSubmitter").value("coachA"))
    }

    @Test
    fun `should rollback play`() {
        val play = createSamplePlay()
        every { playService.rollbackPlay(1) } returns play

        mockMvc.perform(put("/play/rollback").param("gameId", "1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.playCall").value("RUN"))
    }

    @Test
    fun `should submit defense successfully`() {
        val play = createSamplePlay()
        every { playService.defensiveNumberSubmitted(1, "coachB", 22, false) } returns play

        mockMvc.perform(
            post("/play/submit_defense")
                .param("gameId", "1")
                .param("defensiveSubmitter", "coachB")
                .param("defensiveNumber", "22")
                .param("timeoutCalled", "false"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.defensiveSubmitter").value("coachB"))
    }

    @Test
    fun `should submit offense successfully`() {
        val play = createSamplePlay()
        every {
            playService.offensiveNumberSubmitted(
                gameId = 1,
                offensiveSubmitter = "coachA",
                offensiveNumber = 10,
                playCall = PlayCall.RUN,
                runoffType = RunoffType.NONE,
                offensiveTimeoutCalled = false,
            )
        } returns play

        mockMvc.perform(
            put("/play/submit_offense")
                .param("gameId", "1")
                .param("offensiveSubmitter", "coachA")
                .param("offensiveNumber", "10")
                .param("playCall", "RUN")
                .param("runoffType", "NONE")
                .param("timeoutCalled", "false"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.offensiveSubmitter").value("coachA"))
    }

    @Test
    fun `should return error when play not found`() {
        every { playService.getPlayById(1) } throws RuntimeException("Play not found")

        mockMvc.perform(get("/play").param("playId", "1"))
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.error").value("Play not found"))
    }
}
