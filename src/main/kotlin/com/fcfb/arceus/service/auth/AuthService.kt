package com.fcfb.arceus.service.auth

import com.fcfb.arceus.domain.User
import com.fcfb.arceus.models.website.LoginResponse
import com.fcfb.arceus.service.email.EmailService
import com.fcfb.arceus.service.fcfb.UserService
import com.fcfb.arceus.utils.Logger
import com.fcfb.arceus.utils.UserUnauthorizedException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AuthService(
    private val emailService: EmailService,
    private val userService: UserService,
    private val sessionService: SessionService,
    private val passwordEncoder: PasswordEncoder,
) {
    /**
     * Create a new user
     * @param user
     * @return
     */
    suspend fun createUser(user: User): User {
        try {
            val newUser = userService.createUser(user)
            emailService.sendVerificationEmail(newUser.email, newUser.id!!, newUser.verificationToken)
            Logger.info("User ${user.username} registered successfully. Verification email sent.")
            return newUser
        } catch (e: Exception) {
            Logger.error("Error creating user: ", e.message)
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
        return LoginResponse(token, user.id)
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
    fun verifyEmail(token: String): String {
        val user = userService.getByVerificationToken(token)
        user.approved = 1
        userService.approveUser(user.id)
        return "Email verified successfully"
    }

    /**
     * Reset verification token
     * @param id
     * @return
     */
    fun resetVerificationToken(id: Long): User {
        val user = userService.getUserById(id)
        val verificationToken = UUID.randomUUID().toString()
        user.verificationToken = verificationToken
        userService.saveUser(user)
        emailService.sendVerificationEmail(user.email, user.id, verificationToken)
        return user
    }
}
