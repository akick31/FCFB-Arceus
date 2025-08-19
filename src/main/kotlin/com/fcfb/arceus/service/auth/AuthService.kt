package com.fcfb.arceus.service.auth

import com.fcfb.arceus.dto.LoginResponse
import com.fcfb.arceus.dto.SignupInfo
import com.fcfb.arceus.model.NewSignup
import com.fcfb.arceus.service.discord.DiscordService
import com.fcfb.arceus.service.email.EmailService
import com.fcfb.arceus.service.fcfb.NewSignupService
import com.fcfb.arceus.service.fcfb.UserService
import com.fcfb.arceus.util.Logger
import com.fcfb.arceus.util.UserUnauthorizedException
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class AuthService(
    private val discordService: DiscordService,
    private val emailService: EmailService,
    private val userService: UserService,
    private val newSignupService: NewSignupService,
    private val sessionService: SessionService,
    private val passwordEncoder: PasswordEncoder,
) {
    /**
     * Create a new user
     * @param newSignup
     * @return
     */
    fun createNewSignup(newSignup: NewSignup): NewSignup {
        try {
            val signup = newSignupService.createNewSignup(newSignup)
            emailService.sendVerificationEmail(signup.email, signup.id, signup.verificationToken)
            val signupInfo =
                SignupInfo(
                    signup.discordTag,
                    signup.discordId ?: "",
                    signup.teamChoiceOne,
                    signup.teamChoiceTwo,
                    signup.teamChoiceThree,
                )
            discordService.sendRegistrationNotice(signupInfo)
            Logger.info("User ${signup.username} registered successfully. Verification email sent.")
            return signup
        } catch (e: Exception) {
            Logger.error("Error creating new sign up: ", e.message)
            throw e
        }
    }

    /**
     * Login a user
     * @param usernameOrEmail
     * @param password
     * @return
     */
    fun login(
        usernameOrEmail: String,
        password: String,
    ): LoginResponse {
        val user = userService.getUserByUsernameOrEmail(usernameOrEmail)
        if (!passwordEncoder.matches(password, user.password)) {
            throw UserUnauthorizedException()
        }
        val token = sessionService.generateToken(user.id)
        return LoginResponse(token, user.id, user.role)
    }

    /**
     * Logout a user
     * @param token
     * @return
     */
    fun logout(token: String): String {
        sessionService.blacklistUserSession(token)
        return "User logged out successfully"
    }

    /**
     * Verify user email
     * @param token
     * @return
     */
    fun verifyEmail(token: String): Boolean {
        val newSignup = newSignupService.getByVerificationToken(token)
        return newSignup?.let { newSignupService.approveNewSignup(it) } ?: false
    }

    /**
     * Reset verification token
     * @param id
     * @return
     */
    fun resetVerificationToken(id: Long): NewSignup {
        val newSignup =
            newSignupService.getNewSignupById(id)
                ?: throw IllegalArgumentException("NewSignup with id $id not found")
        val verificationToken = UUID.randomUUID().toString()
        newSignup.verificationToken = verificationToken
        newSignupService.saveNewSignup(newSignup)
        emailService.sendVerificationEmail(newSignup.email, newSignup.id, verificationToken)
        return newSignup
    }

    /**
     * Send password reset email
     * @param email
     * @return
     */
    fun forgotPassword(email: String): ResponseEntity<String> {
        val user = userService.updateResetToken(email)

        emailService.sendPasswordResetEmail(user.email, user.id, user.resetToken ?: "")
        return ResponseEntity.ok("Reset email sent")
    }

    /**
     * Reset user password
     * @param token
     * @param userId
     * @param newPassword
     * @return
     */
    fun resetPassword(
        token: String,
        userId: Long,
        newPassword: String,
    ): ResponseEntity<String> {
        val user = userService.getUserById(userId)

        if (user.resetToken != token ||
            user.resetTokenExpiration?.let { LocalDateTime.parse(it).isBefore(LocalDateTime.now()) } == true
        ) {
            return ResponseEntity.badRequest().body("Invalid or expired token")
        }

        userService.updateUserPassword(user.id, newPassword)
        return ResponseEntity.ok("Password updated successfully")
    }
}
