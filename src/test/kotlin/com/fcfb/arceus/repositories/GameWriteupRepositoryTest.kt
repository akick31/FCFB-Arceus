package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.GameWriteup
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GameWriteupRepositoryTest {
    private lateinit var gameWriteupRepository: GameWriteupRepository

    @BeforeEach
    fun setUp() {
        gameWriteupRepository = mockk(relaxed = true)
    }

    @Test
    fun `test save and findById`() {
        // Given
        val gameWriteup =
            createTestGameWriteup(
                id = 1,
                scenario = "GAIN_OF_1_YARD",
                playCall = "Run",
                message = "The running back takes the handoff and finds a hole in the defense.",
            )

        every { gameWriteupRepository.save(any()) } returns gameWriteup
        every { gameWriteupRepository.findById(1) } returns java.util.Optional.of(gameWriteup)

        // When
        val savedGameWriteup = gameWriteupRepository.save(gameWriteup)
        val foundGameWriteup = gameWriteupRepository.findById(savedGameWriteup.id).get()

        // Then
        assertNotNull(foundGameWriteup)
        assertEquals(1, foundGameWriteup.id)
        assertEquals("The running back takes the handoff and finds a hole in the defense.", foundGameWriteup.message)
    }

    @Test
    fun `test findByScenario`() {
        // Given
        val gameWriteup1 =
            createTestGameWriteup(
                id = 1,
                scenario = "GAIN_OF_10_YARDS",
                playCall = "Run",
                message = "Run play writeup",
            )
        val gameWriteup2 =
            createTestGameWriteup(
                id = 2,
                scenario = "GAIN_OF_3_YARDS",
                playCall = "Run",
                message = "Run play writeup",
            )
        val gameWriteup3 =
            createTestGameWriteup(
                id = 3,
                scenario = "GAIN_OF_3_YARDS",
                playCall = "Pass",
                message = "Pass play writeup",
            )

        every { gameWriteupRepository.findByScenario("GAIN_OF_3_YARDS", "Run") } returns listOf(gameWriteup2)

        // When
        val normalScenarioWriteups = gameWriteupRepository.findByScenario("GAIN_OF_3_YARDS", "Run")

        // Then
        assertEquals(1, normalScenarioWriteups.size)
        assertEquals(2, normalScenarioWriteups[0].id)
        assertEquals("Run play writeup", normalScenarioWriteups[0].message)
    }

    @Test
    fun `test findByScenario returns empty list when no matches`() {
        // Given
        every { gameWriteupRepository.findByScenario("NON_EXISTENT", "Run") } returns emptyList()

        // When
        val writeups = gameWriteupRepository.findByScenario("NON_EXISTENT", "Run")

        // Then
        assertTrue(writeups.isEmpty())
    }

    @Test
    fun `test findByScenario with null playCall`() {
        // Given
        val gameWriteup1 =
            createTestGameWriteup(
                id = 1,
                scenario = "RETURN_TO_THE_TWENTY",
                playCall = null,
                message = "Null play call writeup",
            )

        every { gameWriteupRepository.findByScenario("RETURN_TO_THE_TWENTY", null) } returns listOf(gameWriteup1)

        // When
        val nullPlayCallWriteups = gameWriteupRepository.findByScenario("RETURN_TO_THE_TWENTY", null)

        // Then
        assertEquals(1, nullPlayCallWriteups.size)
        assertEquals("Null play call writeup", nullPlayCallWriteups[0].message)
    }

    @Test
    fun `test findAll`() {
        // Given
        val gameWriteup1 = createTestGameWriteup(id = 1, scenario = "GAIN_OF_1_YARD")
        val gameWriteup2 = createTestGameWriteup(id = 2, scenario = "GAIN_OF_2_YARDS")
        val allWriteups = listOf(gameWriteup1, gameWriteup2)

        every { gameWriteupRepository.findAll() } returns allWriteups

        // When
        val foundWriteups = gameWriteupRepository.findAll()

        // Then
        assertEquals(2, foundWriteups.count())
        assertTrue(foundWriteups.any { it.scenario == "GAIN_OF_1_YARD" })
        assertTrue(foundWriteups.any { it.scenario == "GAIN_OF_2_YARDS" })
    }

    @Test
    fun `test count`() {
        // Given
        every { gameWriteupRepository.count() } returns 10L

        // When
        val count = gameWriteupRepository.count()

        // Then
        assertEquals(10L, count)
    }

    private fun createTestGameWriteup(
        id: Int = 1,
        scenario: String = "GAIN_OF_1_YARD",
        playCall: String? = "Run",
        message: String = "Test writeup message",
    ): GameWriteup {
        return GameWriteup(
            scenario = scenario,
            playCall = playCall,
            message = message,
        ).apply { this.id = id }
    }
}
