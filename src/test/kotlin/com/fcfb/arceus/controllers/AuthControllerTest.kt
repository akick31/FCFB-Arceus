package com.fcfb.arceus.controllers

import com.fcfb.arceus.domain.Game.DefensivePlaybook.FOUR_THREE
import com.fcfb.arceus.domain.Game.OffensivePlaybook.AIR_RAID
import com.fcfb.arceus.domain.NewSignup
import com.fcfb.arceus.domain.User
import com.fcfb.arceus.domain.User.CoachPosition.HEAD_COACH
import com.fcfb.arceus.domain.User.Role.USER
import com.fcfb.arceus.models.dto.UserDTO
import com.fcfb.arceus.models.requests.SignupInfo
import com.fcfb.arceus.models.website.LoginResponse
import com.fcfb.arceus.service.auth.AuthService
import com.fcfb.arceus.service.auth.SessionService
import com.fcfb.arceus.service.discord.DiscordService
import com.fcfb.arceus.service.email.EmailService
import com.fcfb.arceus.service.fcfb.NewSignupService
import com.fcfb.arceus.service.fcfb.UserService
import com.fcfb.arceus.utils.UserUnauthorizedException
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import java.util.UUID

class AuthControllerTest {

    private lateinit var authService: AuthService
    private val discordService: DiscordService = mockk()
    private val emailService: EmailService = mockk()
    private val userService: UserService = mockk()
    private val newSignupService: NewSignupService = mockk()
    private val sessionService: SessionService = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()

    @BeforeEach
    fun setup() {
        authService = AuthService(
            discordService,
            emailService,
            userService,
            newSignupService,
            sessionService,
            passwordEncoder
        )
    }

    @Test
    fun `should create new signup successfully`() {
        val newSignup = NewSignup(
            username = "username",
            coachName = "coachName",
            discordTag = "discordTag",
            discordId = "discordId",
            email = "email@example.com",
            hashedEmail = "hashedEmail",
            password = "password",
            position = HEAD_COACH,
            salt = "salt",
            teamChoiceOne = "team1",
            teamChoiceTwo = "team2",
            teamChoiceThree = "team3",
            offensivePlaybook = AIR_RAID,
            defensivePlaybook = FOUR_THREE,
            approved = false,
            verificationToken = UUID.randomUUID().toString()
        )

        every { newSignupService.createNewSignup(newSignup) } returns newSignup
        every { emailService.sendVerificationEmail(newSignup.email, newSignup.id, newSignup.verificationToken) } just Runs
        every { discordService.sendRegistrationNotice(any()) } just Runs

        val result = authService.createNewSignup(newSignup)

        assertEquals(newSignup, result)
        verify { emailService.sendVerificationEmail(newSignup.email, newSignup.id, newSignup.verificationToken) }
        verify { discordService.sendRegistrationNotice(any()) }
    }

    @Test
    fun `should reset verification token successfully`() {
        val id = 1L
        val fixedToken = "fixed-verification-token"
        val newSignup = NewSignup(
            username = "username",
            coachName = "coachName",
            discordTag = "discordTag",
            discordId = "discordId",
            email = "email@example.com",
            hashedEmail = "hashedEmail",
            password = "password",
            position = HEAD_COACH,
            salt = "salt",
            teamChoiceOne = "team1",
            teamChoiceTwo = "team2",
            teamChoiceThree = "team3",
            offensivePlaybook = AIR_RAID,
            defensivePlaybook = FOUR_THREE,
            approved = false,
            verificationToken = "verificationToken"
        )

        mockkStatic(UUID::class)
        every { UUID.randomUUID().toString() } returns fixedToken

        every { newSignupService.getNewSignupById(id) } returns newSignup
        every { newSignupService.saveNewSignup(newSignup) } returns newSignup
        every { emailService.sendVerificationEmail(eq(newSignup.email), eq(newSignup.id), eq(fixedToken)) } just Runs

        val result = authService.resetVerificationToken(id)

        assertEquals(fixedToken, result.verificationToken)
        verify { emailService.sendVerificationEmail(eq(newSignup.email), eq(newSignup.id), eq(fixedToken)) }
    }

    @Test
    fun `should login successfully`() {
        val usernameOrEmail = "username"
        val rawPassword = "password"
        val encodedPassword = "encodedPassword"
        val token = "abc123"

        val user = User(
            username = usernameOrEmail,
            coachName = "Coach X",
            discordTag = "User#1234",
            discordId = "123456",
            email = "user@example.com",
            hashedEmail = "hashedEmail",
            password = encodedPassword,
            position = User.CoachPosition.HEAD_COACH,
            role = User.Role.USER,
            salt = "somesalt",
            team = "FakeU",
            delayOfGameInstances = 0,
            wins = 0,
            losses = 0,
            winPercentage = 0.0,
            conferenceWins = 0,
            conferenceLosses = 0,
            conferenceChampionshipWins = 0,
            conferenceChampionshipLosses = 0,
            bowlWins = 0,
            bowlLosses = 0,
            playoffWins = 0,
            playoffLosses = 0,
            nationalChampionshipWins = 0,
            nationalChampionshipLosses = 0,
            offensivePlaybook = AIR_RAID,
            defensivePlaybook = FOUR_THREE,
            averageResponseTime = 0.0,
            delayOfGameWarningOptOut = false,
            resetToken = null,
            resetTokenExpiration = null
        ).apply {
            id = 1L
        }

        every { userService.getUserByUsernameOrEmail(usernameOrEmail) } returns user
        every { passwordEncoder.matches(rawPassword, encodedPassword) } returns true
        every { sessionService.generateToken(1L) } returns token

        val result = authService.login(usernameOrEmail, rawPassword)

        assertEquals(LoginResponse(token, 1L, User.Role.USER), result)
    }

    @Test
    fun `should throw exception when login fails due to incorrect password`() {
        val usernameOrEmail = "username"
        val password = "wrongPassword"
        val user = User(
            username = usernameOrEmail,
            coachName = "Coach X",
            discordTag = "User#1234",
            discordId = "123456",
            email = "user@example.com",
            hashedEmail = "hashedEmail",
            password = password,
            position = User.CoachPosition.HEAD_COACH,
            role = User.Role.USER,
            salt = "somesalt",
            team = "FakeU",
            delayOfGameInstances = 0,
            wins = 0,
            losses = 0,
            winPercentage = 0.0,
            conferenceWins = 0,
            conferenceLosses = 0,
            conferenceChampionshipWins = 0,
            conferenceChampionshipLosses = 0,
            bowlWins = 0,
            bowlLosses = 0,
            playoffWins = 0,
            playoffLosses = 0,
            nationalChampionshipWins = 0,
            nationalChampionshipLosses = 0,
            offensivePlaybook = AIR_RAID,
            defensivePlaybook = FOUR_THREE,
            averageResponseTime = 0.0,
            delayOfGameWarningOptOut = false,
            resetToken = null,
            resetTokenExpiration = null
        ).apply {
            id = 1L
        }

        every { userService.getUserByUsernameOrEmail(usernameOrEmail) } returns user
        every { passwordEncoder.matches(password, user.password) } returns false

        assertThrows<UserUnauthorizedException> {
            authService.login(usernameOrEmail, password)
        }
    }

    @Test
    fun `should logout successfully`() {
        val token = "abc123"

        every { sessionService.blacklistUserSession(token) } just Runs

        val result = authService.logout(token)

        assertEquals("User logged out successfully", result)
        verify { sessionService.blacklistUserSession(token) }
    }

    @Test
    fun `should verify email successfully`() {
        val token = "verificationToken"
        val newSignup = mockk<NewSignup>()

        every { newSignupService.getByVerificationToken(token) } returns newSignup
        every { newSignupService.approveNewSignup(newSignup) } returns true

        val result = authService.verifyEmail(token)

        assertTrue(result)
    }

    @Test
    fun `should send password reset email successfully`() {
        val email = "email@example.com"
        val user = User(
            username = "username",
            coachName = "Coach X",
            discordTag = "User#1234",
            discordId = "123456",
            email = "user@example.com",
            hashedEmail = "hashedEmail",
            password = "passsword",
            position = User.CoachPosition.HEAD_COACH,
            role = User.Role.USER,
            salt = "somesalt",
            team = "FakeU",
            delayOfGameInstances = 0,
            wins = 0,
            losses = 0,
            winPercentage = 0.0,
            conferenceWins = 0,
            conferenceLosses = 0,
            conferenceChampionshipWins = 0,
            conferenceChampionshipLosses = 0,
            bowlWins = 0,
            bowlLosses = 0,
            playoffWins = 0,
            playoffLosses = 0,
            nationalChampionshipWins = 0,
            nationalChampionshipLosses = 0,
            offensivePlaybook = AIR_RAID,
            defensivePlaybook = FOUR_THREE,
            averageResponseTime = 0.0,
            delayOfGameWarningOptOut = false,
            resetToken = "resettoken",
            resetTokenExpiration = null
        ).apply {
            id = 1L
        }

        every { userService.updateResetToken(email) } returns user
        every { emailService.sendPasswordResetEmail(user.email, user.id, user.resetToken!!) } just Runs

        val result = authService.forgotPassword(email)

        assertEquals(ResponseEntity.ok("Reset email sent"), result)
        verify { emailService.sendPasswordResetEmail(user.email, user.id, user.resetToken!!) }
    }

    @Test
    fun `should reset password successfully`() {
        val token = "resetToken"
        val userId = 1L
        val newPassword = "newPassword"
        val user = mockk<User> {
            every { id } returns userId
            every { resetToken } returns token
            every { resetTokenExpiration } returns LocalDateTime.now().plusDays(1).toString()
        }

        every { userService.getUserById(userId) } returns user
        every { userService.updateUserPassword(userId, newPassword) } returns mockk<UserDTO>()

        val result = authService.resetPassword(token, userId, newPassword)

        assertEquals(ResponseEntity.ok("Password updated successfully"), result)
    }

    @Test
    fun `should return bad request when reset token is invalid or expired`() {
        val token = "invalidToken"
        val userId = 1L
        val newPassword = "newPassword"
        val user = mockk<User> {
            every { resetToken } returns "validToken"
            every { resetTokenExpiration } returns LocalDateTime.now().minusDays(1).toString()
        }

        every { userService.getUserById(userId) } returns user

        val result = authService.resetPassword(token, userId, newPassword)

        assertEquals(ResponseEntity.badRequest().body("Invalid or expired token"), result)
    }
}