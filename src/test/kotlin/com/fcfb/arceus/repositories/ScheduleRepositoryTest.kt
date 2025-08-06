package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Schedule
import com.fcfb.arceus.domain.enums.GameType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ScheduleRepositoryTest {
    private lateinit var scheduleRepository: ScheduleRepository

    @BeforeEach
    fun setUp() {
        scheduleRepository = mockk(relaxed = true)
    }

    @Test
    fun `test save and findById`() {
        // Given
        val schedule =
            createTestSchedule(
                id = 1,
                homeTeam = "Alabama",
                awayTeam = "Auburn",
                season = 2024,
                week = 8,
                gameType = GameType.CONFERENCE_GAME,
                started = false,
                finished = false,
            )

        every { scheduleRepository.save(any()) } returns schedule
        every { scheduleRepository.findById(1) } returns java.util.Optional.of(schedule)

        // When
        val savedSchedule = scheduleRepository.save(schedule)
        val foundSchedule = scheduleRepository.findById(savedSchedule.id).get()

        // Then
        assertNotNull(foundSchedule)
        assertEquals(1, foundSchedule.id)
        assertEquals("Alabama", foundSchedule.homeTeam)
        assertEquals("Auburn", foundSchedule.awayTeam)
        assertEquals(2024, foundSchedule.season)
        assertEquals(8, foundSchedule.week)
        assertEquals(GameType.CONFERENCE_GAME, foundSchedule.gameType)
        foundSchedule.started?.let { assertFalse(it) }
        foundSchedule.finished?.let { assertFalse(it) }
    }

    @Test
    fun `test getGamesToStartBySeasonAndWeek`() {
        // Given
        val unstartedGame1 =
            createTestSchedule(
                id = 1,
                homeTeam = "Alabama",
                awayTeam = "Auburn",
                season = 2024,
                week = 8,
                started = false,
            )
        val unstartedGame2 =
            createTestSchedule(
                id = 2,
                homeTeam = "Georgia",
                awayTeam = "Florida",
                season = 2024,
                week = 8,
                started = false,
            )
        val unstartedGames = listOf(unstartedGame1, unstartedGame2)

        every { scheduleRepository.getGamesToStartBySeasonAndWeek(2024, 8) } returns unstartedGames

        // When
        val foundGames = scheduleRepository.getGamesToStartBySeasonAndWeek(2024, 8)

        // Then
        assertNotNull(foundGames)
        assertEquals(2, foundGames.size)
        assertTrue(foundGames.any { it.homeTeam == "Alabama" })
        assertTrue(foundGames.any { it.homeTeam == "Georgia" })
    }

    @Test
    fun `test getTeamOpponent when team is home`() {
        // Given
        every { scheduleRepository.getTeamOpponent(2024, 8, "Alabama") } returns "Auburn"

        // When
        val opponent = scheduleRepository.getTeamOpponent(2024, 8, "Alabama")

        // Then
        assertEquals("Auburn", opponent)
    }

    @Test
    fun `test getTeamOpponent when team is away`() {
        // Given
        every { scheduleRepository.getTeamOpponent(2024, 8, "Auburn") } returns "Alabama"

        // When
        val opponent = scheduleRepository.getTeamOpponent(2024, 8, "Auburn")

        // Then
        assertEquals("Alabama", opponent)
    }

    @Test
    fun `test getTeamOpponent returns null when team not found`() {
        // Given
        every { scheduleRepository.getTeamOpponent(2024, 8, "NonExistent") } returns null

        // When
        val opponent = scheduleRepository.getTeamOpponent(2024, 8, "NonExistent")

        // Then
        assertNull(opponent)
    }

    @Test
    fun `test getScheduleBySeasonAndTeam`() {
        // Given
        val schedule1 =
            createTestSchedule(
                id = 1,
                homeTeam = "Alabama",
                awayTeam = "Auburn",
                season = 2024,
                week = 8,
            )
        val schedule2 =
            createTestSchedule(
                id = 2,
                homeTeam = "Georgia",
                awayTeam = "Alabama",
                season = 2024,
                week = 9,
            )
        val schedules = listOf(schedule1, schedule2)

        every { scheduleRepository.getScheduleBySeasonAndTeam(2024, "Alabama") } returns schedules

        // When
        val foundSchedules = scheduleRepository.getScheduleBySeasonAndTeam(2024, "Alabama")

        // Then
        assertNotNull(foundSchedules)
        assertEquals(2, foundSchedules.size)
        assertTrue(foundSchedules.any { it.homeTeam == "Alabama" })
        assertTrue(foundSchedules.any { it.awayTeam == "Alabama" })
    }

    @Test
    fun `test findGameInSchedule`() {
        // Given
        val schedule =
            createTestSchedule(
                id = 1,
                homeTeam = "Alabama",
                awayTeam = "Auburn",
                season = 2024,
                week = 8,
            )

        every { scheduleRepository.findGameInSchedule("Alabama", "Auburn", 2024, 8) } returns schedule

        // When
        val foundSchedule = scheduleRepository.findGameInSchedule("Alabama", "Auburn", 2024, 8)

        // Then
        assertNotNull(foundSchedule)
        assertEquals(1, foundSchedule.id)
        assertEquals("Alabama", foundSchedule.homeTeam)
        assertEquals("Auburn", foundSchedule.awayTeam)
    }

    @Test
    fun `test findGameInSchedule returns null when not found`() {
        // Given
        every { scheduleRepository.findGameInSchedule("Alabama", "Georgia", 2024, 8) } returns null

        // When
        val foundSchedule = scheduleRepository.findGameInSchedule("Alabama", "Georgia", 2024, 8)

        // Then
        assertNull(foundSchedule)
    }

    @Test
    fun `test checkIfWeekIsOver when all games finished`() {
        // Given
        every { scheduleRepository.checkIfWeekIsOver(2024, 8) } returns 1

        // When
        val result = scheduleRepository.checkIfWeekIsOver(2024, 8)

        // Then
        assertEquals(1, result)
    }

    @Test
    fun `test checkIfWeekIsOver when some games unfinished`() {
        // Given
        every { scheduleRepository.checkIfWeekIsOver(2024, 8) } returns 0

        // When
        val result = scheduleRepository.checkIfWeekIsOver(2024, 8)

        // Then
        assertEquals(0, result)
    }

    @Test
    fun `test findAll`() {
        // Given
        val schedule1 = createTestSchedule(id = 1, homeTeam = "Alabama")
        val schedule2 = createTestSchedule(id = 2, homeTeam = "Georgia")
        val allSchedules = listOf(schedule1, schedule2)

        every { scheduleRepository.findAll() } returns allSchedules

        // When
        val foundSchedules = scheduleRepository.findAll()

        // Then
        assertEquals(2, foundSchedules.count())
        assertTrue(foundSchedules.any { it.homeTeam == "Alabama" })
        assertTrue(foundSchedules.any { it.homeTeam == "Georgia" })
    }

    @Test
    fun `test count`() {
        // Given
        every { scheduleRepository.count() } returns 100L

        // When
        val count = scheduleRepository.count()

        // Then
        assertEquals(100L, count)
    }

    @Test
    fun `test delete`() {
        // Given
        val schedule = createTestSchedule(id = 1)
        every { scheduleRepository.delete(schedule) } returns Unit

        // When
        scheduleRepository.delete(schedule)

        // Then
        verify { scheduleRepository.delete(schedule) }
    }

    private fun createTestSchedule(
        id: Int = 1,
        homeTeam: String = "Alabama",
        awayTeam: String = "Auburn",
        season: Int = 2024,
        week: Int = 8,
        gameType: GameType = GameType.CONFERENCE_GAME,
        started: Boolean = false,
        finished: Boolean = false,
    ): Schedule {
        return Schedule().apply {
            this.id = id
            this.homeTeam = homeTeam
            this.awayTeam = awayTeam
            this.season = season
            this.week = week
            this.gameType = gameType
            this.started = started
            this.finished = finished
        }
    }
}
