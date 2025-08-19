package com.fcfb.arceus.controllers

import com.fcfb.arceus.util.GlobalExceptionHandler
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.lang.reflect.Field

class HealthControllerTest {
    private lateinit var mockMvc: MockMvc
    private val healthEndpoint: HealthEndpoint = mockk()
    private lateinit var healthController: HealthController

    @BeforeEach
    fun setup() {
        healthController = HealthController()
        // Use reflection to set the mocked healthEndpoint
        val field: Field = HealthController::class.java.getDeclaredField("healthEndpoint")
        field.isAccessible = true
        field.set(healthController, healthEndpoint)

        mockMvc =
            MockMvcBuilders.standaloneSetup(healthController)
                .setControllerAdvice(GlobalExceptionHandler())
                .build()
    }

    @Test
    fun `should return healthy when health endpoint returns UP status`() {
        val health = Health.up().build()
        every { healthEndpoint.health() } returns health

        mockMvc.perform(get("/api/v1/arceus/health").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().string("Application is healthy"))
    }

    @Test
    fun `should return unhealthy when health endpoint returns DOWN status`() {
        val health = Health.down().build()
        every { healthEndpoint.health() } returns health

        mockMvc.perform(get("/api/v1/arceus/health").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError)
            .andExpect(content().string("Application is unhealthy"))
    }

    @Test
    fun `should return unhealthy when health endpoint returns null`() {
        every { healthEndpoint.health() } returns null

        mockMvc.perform(get("/api/v1/arceus/health").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError)
            .andExpect(content().string("Application is unhealthy"))
    }
}
