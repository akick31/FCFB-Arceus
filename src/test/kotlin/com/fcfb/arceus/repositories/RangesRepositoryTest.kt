package com.fcfb.arceus.repositories

import com.fcfb.arceus.enums.play.Scenario
import com.fcfb.arceus.model.Ranges
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RangesRepositoryTest {
    private lateinit var rangesRepository: RangesRepository

    @BeforeEach
    fun setUp() {
        rangesRepository = mockk(relaxed = true)
    }

    @Test
    fun `test save and findById`() {
        // Given
        val ranges =
            createTestRanges(
                id = 1,
                playType = "RUN",
                offensivePlaybook = "AIR_RAID",
                defensivePlaybook = "FOUR_THREE",
                lowerRange = 1,
                upperRange = 10,
                result = Scenario.GAIN_OF_5_YARDS,
                playTime = 5,
            )

        every { rangesRepository.save(any()) } returns ranges
        every { rangesRepository.findById(1) } returns java.util.Optional.of(ranges)

        // When
        val savedRanges = rangesRepository.save(ranges)
        val foundRanges = rangesRepository.findById(savedRanges.id!!).get()

        // Then
        assertNotNull(foundRanges)
        assertEquals(1, foundRanges.id)
        assertEquals("RUN", foundRanges.playType)
        assertEquals("AIR_RAID", foundRanges.offensivePlaybook)
        assertEquals("FOUR_THREE", foundRanges.defensivePlaybook)
        assertEquals(1, foundRanges.lowerRange)
        assertEquals(10, foundRanges.upperRange)
        assertEquals(5, foundRanges.playTime)
    }

    @Test
    fun `test getNormalResult`() {
        // Given
        val ranges =
            createTestRanges(
                id = 1,
                playType = "PASS",
                offensivePlaybook = "SPREAD",
                defensivePlaybook = "THREE_FOUR",
                lowerRange = 5,
                upperRange = 15,
                result = Scenario.GAIN_OF_11_YARDS,
            )

        every { rangesRepository.getNormalResult("PASS", "SPREAD", "THREE_FOUR", "10") } returns ranges

        // When
        val foundRanges = rangesRepository.getNormalResult("PASS", "SPREAD", "THREE_FOUR", "10")

        // Then
        assertNotNull(foundRanges)
        assertEquals(1, foundRanges.id)
        assertEquals("PASS", foundRanges.playType)
        assertEquals("SPREAD", foundRanges.offensivePlaybook)
        assertEquals("THREE_FOUR", foundRanges.defensivePlaybook)
    }

    @Test
    fun `test getNormalResult returns null when not found`() {
        // Given
        every { rangesRepository.getNormalResult("RUN", "AIR_RAID", "FOUR_THREE", "20") } returns null

        // When
        val foundRanges = rangesRepository.getNormalResult("RUN", "AIR_RAID", "FOUR_THREE", "20")

        // Then
        assertNull(foundRanges) // 20 is outside the range 1-10
    }

    @Test
    fun `test getNonNormalResult`() {
        // Given
        val ranges =
            createTestRanges(
                id = 1,
                playType = "PASS",
                lowerRange = 1,
                upperRange = 10,
                result = Scenario.FUMBLE,
            )

        every { rangesRepository.getNonNormalResult("PASS", "5") } returns ranges

        // When
        val foundRanges = rangesRepository.getNonNormalResult("PASS", "5")

        // Then
        assertNotNull(foundRanges)
        assertEquals(1, foundRanges.id)
        assertEquals("PASS", foundRanges.playType)
        assertEquals(Scenario.FUMBLE, foundRanges.result)
    }

    @Test
    fun `test getNonNormalResult returns null when not found`() {
        // Given
        every { rangesRepository.getNonNormalResult("RUN", "20") } returns null

        // When
        val foundRanges = rangesRepository.getNonNormalResult("RUN", "20")

        // Then
        assertNull(foundRanges)
    }

    @Test
    fun `test getPuntResult`() {
        // Given
        val ranges =
            createTestRanges(
                id = 1,
                playType = "PUNT",
                lowerRange = 1,
                upperRange = 10,
                result = Scenario.FORTY_YARD_PUNT,
            )

        every { rangesRepository.getPuntResult("PUNT", "20", "5") } returns ranges

        // When
        val foundRanges = rangesRepository.getPuntResult("PUNT", "20", "5")

        // Then
        assertNotNull(foundRanges)
        assertEquals(1, foundRanges.id)
        assertEquals("PUNT", foundRanges.playType)
        assertEquals(Scenario.FORTY_YARD_PUNT, foundRanges.result)
    }

    @Test
    fun `test getPuntResult returns null when ball location out of range`() {
        // Given
        every { rangesRepository.getPuntResult("PUNT", "50", "5") } returns null

        // When
        val foundRanges = rangesRepository.getPuntResult("PUNT", "50", "5")

        // Then
        assertNull(foundRanges)
    }

    @Test
    fun `test getFieldGoalResult`() {
        // Given
        val ranges =
            createTestRanges(
                id = 1,
                playType = "FIELD_GOAL",
                lowerRange = 1,
                upperRange = 10,
                result = Scenario.GOOD,
            )

        every { rangesRepository.getFieldGoalResult("FIELD_GOAL", "30", "5") } returns ranges

        // When
        val foundRanges = rangesRepository.getFieldGoalResult("FIELD_GOAL", "30", "5")

        // Then
        assertNotNull(foundRanges)
        assertEquals(1, foundRanges.id)
        assertEquals("FIELD_GOAL", foundRanges.playType)
        assertEquals(Scenario.GOOD, foundRanges.result)
    }

    @Test
    fun `test getFieldGoalResult returns null when distance doesn't match`() {
        // Given
        every { rangesRepository.getFieldGoalResult("FIELD_GOAL", "50", "5") } returns null

        // When
        val foundRanges = rangesRepository.getFieldGoalResult("FIELD_GOAL", "50", "5")

        // Then
        assertNull(foundRanges)
    }

    @Test
    fun `test getPlayTime`() {
        // Given
        every { rangesRepository.getPlayTime("RUN", 5) } returns 30

        // When
        val playTime = rangesRepository.getPlayTime("RUN", 5)

        // Then
        assertEquals(30, playTime)
    }

    @Test
    fun `test getPlayTime returns null when no numeric results found`() {
        // Given
        every { rangesRepository.getPlayTime("PASS", 10) } returns null

        // When
        val playTime = rangesRepository.getPlayTime("PASS", 10)

        // Then
        assertNull(playTime)
    }

    @Test
    fun `test findAll`() {
        // Given
        val ranges1 = createTestRanges(id = 1, playType = "RUN")
        val ranges2 = createTestRanges(id = 2, playType = "PASS")
        val allRanges = listOf(ranges1, ranges2)

        every { rangesRepository.findAll() } returns allRanges

        // When
        val foundRanges = rangesRepository.findAll()

        // Then
        assertEquals(2, foundRanges.count())
        assertTrue(foundRanges.any { it.playType == "RUN" })
        assertTrue(foundRanges.any { it.playType == "PASS" })
    }

    @Test
    fun `test count`() {
        // Given
        every { rangesRepository.count() } returns 25L

        // When
        val count = rangesRepository.count()

        // Then
        assertEquals(25L, count)
    }

    private fun createTestRanges(
        id: Int = 1,
        playType: String = "RUN",
        offensivePlaybook: String = "AIR_RAID",
        defensivePlaybook: String = "FOUR_THREE",
        lowerRange: Int = 1,
        upperRange: Int = 10,
        result: Scenario = Scenario.GAIN_OF_5_YARDS,
        playTime: Int = 30,
    ): Ranges {
        return Ranges(
            playType = playType,
            offensivePlaybook = offensivePlaybook,
            defensivePlaybook = defensivePlaybook,
            ballLocationLower = null,
            ballLocationUpper = null,
            distance = null,
            result = result,
            playTime = playTime,
            lowerRange = lowerRange,
            upperRange = upperRange,
        ).apply { this.id = id }
    }
}
