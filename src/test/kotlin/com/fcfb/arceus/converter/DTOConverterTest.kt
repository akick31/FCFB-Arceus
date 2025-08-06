package com.fcfb.arceus.converter

import com.fcfb.arceus.domain.enums.DefensivePlaybook
import com.fcfb.arceus.domain.enums.OffensivePlaybook
import com.fcfb.arceus.domain.NewSignup
import com.fcfb.arceus.domain.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DTOConverterTest {
    private lateinit var dtoConverter: DTOConverter

    @BeforeEach
    fun setup() {
        dtoConverter = DTOConverter()
    }

    @Test
    fun `DTOConverter should be properly annotated`() {
        val componentAnnotation = DTOConverter::class.annotations.find { it is org.springframework.stereotype.Component }
        assertNotNull(componentAnnotation, "DTOConverter should be annotated with @Component")
    }

    @Test
    fun `convertToUserDTO should convert User to UserDTO correctly`() {
        val user =
            User().apply {
                id = 1L
                username = "testuser"
                coachName = "Test Coach"
                discordTag = "testuser#1234"
                discordId = "123456789"
                position = User.CoachPosition.HEAD_COACH
                role = User.Role.ADMIN
                team = "Test Team"
                delayOfGameInstances = 5
                wins = 10
                losses = 5
                winPercentage = 0.667
                conferenceWins = 8
                conferenceLosses = 2
                conferenceChampionshipWins = 1
                conferenceChampionshipLosses = 0
                bowlWins = 2
                bowlLosses = 1
                playoffWins = 1
                playoffLosses = 0
                nationalChampionshipWins = 1
                nationalChampionshipLosses = 0
                offensivePlaybook = OffensivePlaybook.AIR_RAID
                defensivePlaybook = DefensivePlaybook.THREE_FOUR
                averageResponseTime = 120.5
                delayOfGameWarningOptOut = false
            }

        val userDTO = dtoConverter.convertToUserDTO(user)

        assertNotNull(userDTO, "UserDTO should not be null")
        assertEquals(user.id, userDTO.id)
        assertEquals(user.username, userDTO.username)
        assertEquals(user.coachName, userDTO.coachName)
        assertEquals(user.discordTag, userDTO.discordTag)
        assertEquals(user.discordId, userDTO.discordId)
        assertEquals(user.position, userDTO.position)
        assertEquals(user.role, userDTO.role)
        assertEquals(user.team, userDTO.team)
        assertEquals(user.delayOfGameInstances, userDTO.delayOfGameInstances)
        assertEquals(user.wins, userDTO.wins)
        assertEquals(user.losses, userDTO.losses)
        assertEquals(user.winPercentage, userDTO.winPercentage)
        assertEquals(user.conferenceWins, userDTO.conferenceWins)
        assertEquals(user.conferenceLosses, userDTO.conferenceLosses)
        assertEquals(user.conferenceChampionshipWins, userDTO.conferenceChampionshipWins)
        assertEquals(user.conferenceChampionshipLosses, userDTO.conferenceChampionshipLosses)
        assertEquals(user.bowlWins, userDTO.bowlWins)
        assertEquals(user.bowlLosses, userDTO.bowlLosses)
        assertEquals(user.playoffWins, userDTO.playoffWins)
        assertEquals(user.playoffLosses, userDTO.playoffLosses)
        assertEquals(user.nationalChampionshipWins, userDTO.nationalChampionshipWins)
        assertEquals(user.nationalChampionshipLosses, userDTO.nationalChampionshipLosses)
        assertEquals(user.offensivePlaybook, userDTO.offensivePlaybook)
        assertEquals(user.defensivePlaybook, userDTO.defensivePlaybook)
        assertEquals(user.averageResponseTime, userDTO.averageResponseTime)
        assertEquals(user.delayOfGameWarningOptOut, userDTO.delayOfGameWarningOptOut)
    }

    @Test
    fun `convertToUserDTO should handle null values correctly`() {
        val user =
            User().apply {
                id = 2L
                username = "testuser2"
                coachName = "Test Coach 2"
                discordTag = "testuser2#5678"
                discordId = null
                position = User.CoachPosition.OFFENSIVE_COORDINATOR
                role = User.Role.USER
                team = null
                delayOfGameInstances = 0
                wins = 0
                losses = 0
                winPercentage = 0.0
                conferenceWins = 0
                conferenceLosses = 0
                conferenceChampionshipWins = 0
                conferenceChampionshipLosses = 0
                bowlWins = 0
                bowlLosses = 0
                playoffWins = 0
                playoffLosses = 0
                nationalChampionshipWins = 0
                nationalChampionshipLosses = 0
                offensivePlaybook = OffensivePlaybook.AIR_RAID
                defensivePlaybook = DefensivePlaybook.THREE_FOUR
                averageResponseTime = 0.0
                delayOfGameWarningOptOut = false
            }

        val userDTO = dtoConverter.convertToUserDTO(user)

        assertNotNull(userDTO, "UserDTO should not be null")
        assertEquals(user.id, userDTO.id)
        assertEquals(user.username, userDTO.username)
        assertEquals(user.coachName, userDTO.coachName)
        assertEquals(user.discordTag, userDTO.discordTag)
        assertEquals(user.discordId, userDTO.discordId)
        assertEquals(user.position, userDTO.position)
        assertEquals(user.role, userDTO.role)
        assertEquals(user.team, userDTO.team)
        assertEquals(user.delayOfGameInstances, userDTO.delayOfGameInstances)
        assertEquals(user.wins, userDTO.wins)
        assertEquals(user.losses, userDTO.losses)
        assertEquals(user.winPercentage, userDTO.winPercentage)
        assertEquals(user.offensivePlaybook, userDTO.offensivePlaybook)
        assertEquals(user.defensivePlaybook, userDTO.defensivePlaybook)
        assertEquals(user.averageResponseTime, userDTO.averageResponseTime)
        assertEquals(user.delayOfGameWarningOptOut, userDTO.delayOfGameWarningOptOut)
    }

    @Test
    fun `convertToNewSignupDTO should convert NewSignup to NewSignupDTO correctly`() {
        val newSignup =
            NewSignup().apply {
                id = 1L
                username = "newsignup"
                coachName = "New Coach"
                discordTag = "newsignup#9999"
                discordId = "987654321"
                email = "newsignup@example.com"
                hashedEmail = "hashed_email_1"
                password = "password123"
                salt = "salt123"
                position = User.CoachPosition.HEAD_COACH
                teamChoiceOne = "Team A"
                teamChoiceTwo = "Team B"
                teamChoiceThree = "Team C"
                offensivePlaybook = OffensivePlaybook.SPREAD
                defensivePlaybook = DefensivePlaybook.FOUR_THREE
                approved = true
            }

        val newSignupDTO = dtoConverter.convertToNewSignupDTO(newSignup)

        assertNotNull(newSignupDTO, "NewSignupDTO should not be null")
        assertEquals(newSignup.id, newSignupDTO.id)
        assertEquals(newSignup.username, newSignupDTO.username)
        assertEquals(newSignup.coachName, newSignupDTO.coachName)
        assertEquals(newSignup.discordTag, newSignupDTO.discordTag)
        assertEquals(newSignup.discordId, newSignupDTO.discordId)
        assertEquals(newSignup.position, newSignupDTO.position)
        assertEquals(newSignup.teamChoiceOne, newSignupDTO.teamChoiceOne)
        assertEquals(newSignup.teamChoiceTwo, newSignupDTO.teamChoiceTwo)
        assertEquals(newSignup.teamChoiceThree, newSignupDTO.teamChoiceThree)
        assertEquals(newSignup.offensivePlaybook, newSignupDTO.offensivePlaybook)
        assertEquals(newSignup.defensivePlaybook, newSignupDTO.defensivePlaybook)
        assertEquals(newSignup.approved, newSignupDTO.approved)
    }

    @Test
    fun `convertToNewSignupDTO should handle null values correctly`() {
        val newSignup =
            NewSignup().apply {
                id = 2L
                username = "newsignup2"
                coachName = "New Coach 2"
                discordTag = "newsignup2#8888"
                discordId = null
                email = "newsignup2@example.com"
                hashedEmail = "hashed_email_2"
                password = "password456"
                salt = "salt456"
                position = User.CoachPosition.DEFENSIVE_COORDINATOR
                teamChoiceOne = "Default Team 1"
                teamChoiceTwo = "Default Team 2"
                teamChoiceThree = "Default Team 3"
                offensivePlaybook = OffensivePlaybook.SPREAD
                defensivePlaybook = DefensivePlaybook.FOUR_THREE
                approved = false
            }

        val newSignupDTO = dtoConverter.convertToNewSignupDTO(newSignup)

        assertNotNull(newSignupDTO, "NewSignupDTO should not be null")
        assertEquals(newSignup.id, newSignupDTO.id)
        assertEquals(newSignup.username, newSignupDTO.username)
        assertEquals(newSignup.coachName, newSignupDTO.coachName)
        assertEquals(newSignup.discordTag, newSignupDTO.discordTag)
        assertEquals(newSignup.discordId, newSignupDTO.discordId)
        assertEquals(newSignup.position, newSignupDTO.position)
        assertEquals(newSignup.teamChoiceOne, newSignupDTO.teamChoiceOne)
        assertEquals(newSignup.teamChoiceTwo, newSignupDTO.teamChoiceTwo)
        assertEquals(newSignup.teamChoiceThree, newSignupDTO.teamChoiceThree)
        assertEquals(newSignup.offensivePlaybook, newSignupDTO.offensivePlaybook)
        assertEquals(newSignup.defensivePlaybook, newSignupDTO.defensivePlaybook)
        assertEquals(newSignup.approved, newSignupDTO.approved)
    }

    @Test
    fun `DTOConverter should be instantiable`() {
        val converter = DTOConverter()
        assertNotNull(converter, "DTOConverter should be instantiable")
    }

    @Test
    fun `convertToUserDTO should handle different enum values`() {
        val user =
            User().apply {
                id = 3L
                username = "enumtest"
                coachName = "Enum Test Coach"
                discordTag = "enumtest#0000"
                position = User.CoachPosition.OFFENSIVE_COORDINATOR
                role = User.Role.ADMIN
                offensivePlaybook = OffensivePlaybook.PRO
                defensivePlaybook = DefensivePlaybook.FIVE_TWO
            }

        val userDTO = dtoConverter.convertToUserDTO(user)

        assertEquals(User.CoachPosition.OFFENSIVE_COORDINATOR, userDTO.position)
        assertEquals(User.Role.ADMIN, userDTO.role)
        assertEquals(OffensivePlaybook.PRO, userDTO.offensivePlaybook)
        assertEquals(DefensivePlaybook.FIVE_TWO, userDTO.defensivePlaybook)
    }

    @Test
    fun `convertToNewSignupDTO should handle different enum values`() {
        val newSignup =
            NewSignup().apply {
                id = 3L
                username = "enumtest2"
                coachName = "Enum Test Coach 2"
                discordTag = "enumtest2#1111"
                email = "test@example.com"
                hashedEmail = "hashed_email"
                password = "password"
                salt = "salt"
                position = User.CoachPosition.DEFENSIVE_COORDINATOR
                teamChoiceOne = "Team A"
                teamChoiceTwo = "Team B"
                teamChoiceThree = "Team C"
                offensivePlaybook = OffensivePlaybook.FLEXBONE
                defensivePlaybook = DefensivePlaybook.FIVE_TWO
            }

        val newSignupDTO = dtoConverter.convertToNewSignupDTO(newSignup)

        assertEquals(User.CoachPosition.DEFENSIVE_COORDINATOR, newSignupDTO.position)
        assertEquals(OffensivePlaybook.FLEXBONE, newSignupDTO.offensivePlaybook)
        assertEquals(DefensivePlaybook.FIVE_TWO, newSignupDTO.defensivePlaybook)
    }
}
