package com.fcfb.arceus.controllers

import com.fcfb.arceus.domain.Game.DefensivePlaybook.FOUR_THREE
import com.fcfb.arceus.domain.Game.DefensivePlaybook.THREE_FOUR
import com.fcfb.arceus.domain.Game.OffensivePlaybook.AIR_RAID
import com.fcfb.arceus.domain.Game.OffensivePlaybook.PRO
import com.fcfb.arceus.domain.User
import com.fcfb.arceus.domain.User.CoachPosition.HEAD_COACH
import com.fcfb.arceus.domain.User.Role.USER
import com.fcfb.arceus.models.dto.UserDTO
import com.fcfb.arceus.models.requests.UserValidationRequest
import com.fcfb.arceus.models.response.UserValidationResponse
import com.fcfb.arceus.service.fcfb.UserService
import com.fcfb.arceus.utils.GlobalExceptionHandler
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class UserControllerTest {
    private lateinit var mockMvc: MockMvc
    private val userService: UserService = mockk()
    private lateinit var userController: UserController

    private val sampleUser =
        UserDTO(
            id = 1L,
            username = "testuser",
            coachName = "Test Coach",
            discordTag = "test#1234",
            discordId = "123456789",
            position = HEAD_COACH,
            role = USER,
            team = "Test Team",
            delayOfGameInstances = 2,
            wins = 10,
            losses = 5,
            winPercentage = 0.67,
            conferenceWins = 6,
            conferenceLosses = 2,
            conferenceChampionshipWins = 1,
            conferenceChampionshipLosses = 0,
            bowlWins = 1,
            bowlLosses = 0,
            playoffWins = 2,
            playoffLosses = 1,
            nationalChampionshipWins = 0,
            nationalChampionshipLosses = 1,
            offensivePlaybook = AIR_RAID,
            defensivePlaybook = FOUR_THREE,
            averageResponseTime = 15.5,
            delayOfGameWarningOptOut = false,
        )

    @BeforeEach
    fun setup() {
        userController = UserController(userService)
        mockMvc =
            MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(GlobalExceptionHandler())
                .build()
    }

    @Test
    fun `getUserById returns user`() {
        val fullUser =
            User(
                username = "testuser",
                coachName = "Test Coach",
                discordTag = "test#1234",
                discordId = "123456789",
                email = "test@example.com",
                hashedEmail = "hashedemail123",
                password = "encryptedPassword",
                position = HEAD_COACH,
                role = USER,
                salt = "randomSalt",
                team = "Test Team",
                delayOfGameInstances = 2,
                wins = 10,
                losses = 5,
                winPercentage = 0.67,
                conferenceWins = 6,
                conferenceLosses = 2,
                conferenceChampionshipWins = 1,
                conferenceChampionshipLosses = 0,
                bowlWins = 1,
                bowlLosses = 0,
                playoffWins = 2,
                playoffLosses = 1,
                nationalChampionshipWins = 0,
                nationalChampionshipLosses = 1,
                offensivePlaybook = AIR_RAID,
                defensivePlaybook = FOUR_THREE,
                averageResponseTime = 15.5,
                delayOfGameWarningOptOut = false,
                resetToken = null,
                resetTokenExpiration = null,
            )

        every { userService.getUserById(1L) } returns fullUser

        mockMvc.perform(get("/user/id").param("id", "1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(fullUser.id))
            .andExpect(jsonPath("$.username").value(fullUser.username))
            .andExpect(jsonPath("$.discordId").value(fullUser.discordId))
    }

    @Test
    fun `getUserByDiscordId returns user`() {
        every { userService.getUserDTOByDiscordId("123456789") } returns sampleUser

        mockMvc.perform(get("/user/discord").param("id", "123456789"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.discordId").value("123456789"))
    }

    @Test
    fun `getUserByTeam returns user`() {
        every { userService.getUserByTeam("Test Team") } returns sampleUser

        mockMvc.perform(get("/user/team").param("team", "Test Team"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.team").value("Test Team"))
    }

    @Test
    fun `getAllUsers returns all users`() {
        val users =
            listOf(
                sampleUser,
                sampleUser.copy(
                    id = 2L,
                    username = "user2",
                    team = "Another Team",
                    offensivePlaybook = PRO,
                    defensivePlaybook = THREE_FOUR,
                ),
            )
        every { userService.getAllUsers() } returns users

        mockMvc.perform(get("/user"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].offensivePlaybook").value("PRO"))
    }

    @Test
    fun `getFreeAgents returns list`() {
        val freeAgents = listOf(sampleUser)
        every { userService.getFreeAgents() } returns freeAgents

        mockMvc.perform(get("/user/free_agents"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(sampleUser.id))
    }

    @Test
    fun `getUserDTOByName returns user`() {
        every { userService.getUserDTOByName("testuser") } returns sampleUser

        mockMvc.perform(get("/user/name").param("name", "testuser"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.username").value("testuser"))
    }

    @Test
    fun `updateUserEmail updates email`() {
        every { userService.updateEmail(1L, "newemail@example.com") } returns sampleUser

        mockMvc.perform(
            put("/user/update/email")
                .param("id", "1")
                .param("newEmail", "newemail@example.com"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
    }

    @Test
    fun `updateUserRole updates user`() {
        every { userService.updateUser(sampleUser) } returns sampleUser

        val jsonBody =
            """
            {
              "id": 1,
              "username": "testuser",
              "coachName": "Test Coach",
              "discordTag": "test#1234",
              "discordId": "123456789",
              "position": "HEAD_COACH",
              "role": "USER",
              "team": "Test Team",
              "delayOfGameInstances": 2,
              "wins": 10,
              "losses": 5,
              "winPercentage": 0.67,
              "conferenceWins": 6,
              "conferenceLosses": 2,
              "conferenceChampionshipWins": 1,
              "conferenceChampionshipLosses": 0,
              "bowlWins": 1,
              "bowlLosses": 0,
              "playoffWins": 2,
              "playoffLosses": 1,
              "nationalChampionshipWins": 0,
              "nationalChampionshipLosses": 1,
              "offensivePlaybook": "AIR_RAID",
              "defensivePlaybook": "FOUR_THREE",
              "averageResponseTime": 15.5,
              "delayOfGameWarningOptOut": false
            }
            """.trimIndent()

        mockMvc.perform(
            put("/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("testuser"))
    }

    @Test
    fun `encryptEmails returns success`() {
        every { userService.hashEmails() } returns Unit

        mockMvc.perform(post("/user/hash_emails"))
            .andExpect(status().isOk)
    }

    @Test
    fun `validateUser returns success`() {
        val validationRequest =
            UserValidationRequest(
                discordId = "1234",
                discordTag = "discordTag",
                username = "testUser",
                email = "testemail@test.com",
            )
        every { userService.validateUser(validationRequest) } returns
            UserValidationResponse(
                discordIdExists = true,
                discordTagExists = true,
                usernameExists = true,
                emailExists = true,
            )

        val jsonBody =
            """
            {
              "discordId": "1234",
              "discordTag": "discordTag",
              "username": "testUser",
              "email": "testemail@test.com"
            }
            """.trimIndent()

        mockMvc.perform(
            post("/user/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody),
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `deleteTeam deletes user`() {
        every { userService.deleteUser(1L) } returns OK

        mockMvc.perform(delete("/user").param("id", "1"))
            .andExpect(status().isOk)
    }

    @Test
    fun `getUserById handles error`() {
        every { userService.getUserById(1L) } throws RuntimeException("User not found")

        mockMvc.perform(get("/user/id").param("id", "1"))
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.error").value("User not found"))
    }
}
