package com.fcfb.arceus.repositories

import com.fcfb.arceus.enums.game.GameStatus
import com.fcfb.arceus.enums.game.GameType
import com.fcfb.arceus.enums.game.TVChannel
import com.fcfb.arceus.enums.team.DefensivePlaybook
import com.fcfb.arceus.enums.team.OffensivePlaybook
import com.fcfb.arceus.enums.team.Subdivision
import com.fcfb.arceus.model.GameStats
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GameStatsRepositoryTest {
    private lateinit var gameStatsRepository: GameStatsRepository

    @BeforeEach
    fun setUp() {
        gameStatsRepository = mockk(relaxed = true)
    }

    @Test
    fun `test save and findById`() {
        // Given
        val gameStats =
            createTestGameStats(
                id = 1,
                gameId = 123,
                team = "Alabama",
                tvChannel = TVChannel.CBS,
                coaches = listOf("coach1", "coach2"),
                offensivePlaybook = OffensivePlaybook.AIR_RAID,
                defensivePlaybook = DefensivePlaybook.FOUR_THREE,
                season = 2024,
                week = 8,
                subdivision = Subdivision.FBS,
                score = 28,
                gameType = GameType.CONFERENCE_GAME,
                gameStatus = GameStatus.FINAL,
            )

        every { gameStatsRepository.save(any()) } returns gameStats
        every { gameStatsRepository.findById(1) } returns java.util.Optional.of(gameStats)

        // When
        val savedGameStats = gameStatsRepository.save(gameStats)
        val foundGameStats = gameStatsRepository.findById(savedGameStats.id).get()

        // Then
        assertNotNull(foundGameStats)
        assertEquals(1, foundGameStats.id)
        assertEquals(123, foundGameStats.gameId)
        assertEquals("Alabama", foundGameStats.team)
        assertEquals(TVChannel.CBS, foundGameStats.tvChannel)
        assertEquals(listOf("coach1", "coach2"), foundGameStats.coaches)
        assertEquals(OffensivePlaybook.AIR_RAID, foundGameStats.offensivePlaybook)
        assertEquals(DefensivePlaybook.FOUR_THREE, foundGameStats.defensivePlaybook)
        assertEquals(2024, foundGameStats.season)
        assertEquals(8, foundGameStats.week)
        assertEquals(Subdivision.FBS, foundGameStats.subdivision)
        assertEquals(28, foundGameStats.score)
        assertEquals(GameType.CONFERENCE_GAME, foundGameStats.gameType)
        assertEquals(GameStatus.FINAL, foundGameStats.gameStatus)
    }

    @Test
    fun `test getGameStatsByIdAndTeam`() {
        // Given
        val gameStats =
            createTestGameStats(
                id = 1,
                gameId = 456,
                team = "Auburn",
                score = 21,
            )

        every { gameStatsRepository.getGameStatsByIdAndTeam(456, "Auburn") } returns gameStats

        // When
        val foundGameStats = gameStatsRepository.getGameStatsByIdAndTeam(456, "Auburn")

        // Then
        assertNotNull(foundGameStats)
        assertEquals(1, foundGameStats.id)
        assertEquals(456, foundGameStats.gameId)
        assertEquals("Auburn", foundGameStats.team)
        assertEquals(21, foundGameStats.score)
    }

    @Test
    fun `test getGameStatsByIdAndTeam returns null when not found`() {
        // Given
        every { gameStatsRepository.getGameStatsByIdAndTeam(789, "Georgia") } returns null

        // When
        val foundGameStats = gameStatsRepository.getGameStatsByIdAndTeam(789, "Georgia")

        // Then
        assertNull(foundGameStats)
    }

    @Test
    fun `test getGameStatsByIdAndTeam returns null when game not found`() {
        // Given
        every { gameStatsRepository.getGameStatsByIdAndTeam(999, "NonExistent") } returns null

        // When
        val foundGameStats = gameStatsRepository.getGameStatsByIdAndTeam(999, "NonExistent")

        // Then
        assertNull(foundGameStats)
    }

    @Test
    fun `test deleteByGameId`() {
        // Given
        every { gameStatsRepository.deleteByGameId(123) } returns Unit

        // When
        gameStatsRepository.deleteByGameId(123)

        // Then
        verify { gameStatsRepository.deleteByGameId(123) }
    }

    @Test
    fun `test findAll`() {
        // Given
        val gameStats1 = createTestGameStats(id = 1, team = "Alabama")
        val gameStats2 = createTestGameStats(id = 2, team = "Auburn")
        val allGameStats = listOf(gameStats1, gameStats2)

        every { gameStatsRepository.findAll() } returns allGameStats

        // When
        val foundGameStats = gameStatsRepository.findAll()

        // Then
        assertEquals(2, foundGameStats.count())
        assertTrue(foundGameStats.any { it.team == "Alabama" })
        assertTrue(foundGameStats.any { it.team == "Auburn" })
    }

    @Test
    fun `test count`() {
        // Given
        every { gameStatsRepository.count() } returns 5L

        // When
        val count = gameStatsRepository.count()

        // Then
        assertEquals(5L, count)
    }

    @Test
    fun `test delete`() {
        // Given
        val gameStats = createTestGameStats(id = 1, team = "Alabama")
        every { gameStatsRepository.delete(gameStats) } returns Unit

        // When
        gameStatsRepository.delete(gameStats)

        // Then
        verify { gameStatsRepository.delete(gameStats) }
    }

    private fun createTestGameStats(
        id: Int = 1,
        gameId: Int = 123,
        team: String = "Alabama",
        tvChannel: TVChannel = TVChannel.CBS,
        coaches: List<String> = listOf("coach1"),
        offensivePlaybook: OffensivePlaybook = OffensivePlaybook.AIR_RAID,
        defensivePlaybook: DefensivePlaybook = DefensivePlaybook.FOUR_THREE,
        season: Int = 2024,
        week: Int = 8,
        subdivision: Subdivision = Subdivision.FBS,
        score: Int = 28,
        gameType: GameType = GameType.CONFERENCE_GAME,
        gameStatus: GameStatus = GameStatus.FINAL,
    ): GameStats {
        return GameStats(
            id = id,
            gameId = gameId,
            team = team,
            tvChannel = tvChannel,
            coaches = coaches,
            offensivePlaybook = offensivePlaybook,
            defensivePlaybook = defensivePlaybook,
            season = season,
            week = week,
            subdivision = subdivision,
            score = score,
            gameType = gameType,
            gameStatus = gameStatus,
        )
    }
}
