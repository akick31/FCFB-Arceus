package com.fcfb.arceus.service.auth

import com.fcfb.arceus.domain.User
import com.fcfb.arceus.models.website.Session
import com.fcfb.arceus.repositories.SessionRepository
import com.fcfb.arceus.service.email.EmailService
import com.fcfb.arceus.service.fcfb.UserService
import com.fcfb.arceus.utils.Logger
import com.fcfb.arceus.utils.SessionUtils
import com.fcfb.arceus.utils.UserUnauthorizedException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class AuthService(
    private val sessionUtils: SessionUtils,
    private val emailService: EmailService,
    private val userService: UserService,
    private val sessionRepository: SessionRepository,
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
    fun loginUser(
        usernameOrEmail: String,
        password: String,
    ): Session {
        val user = userService.getUserByUsernameOrEmail(usernameOrEmail)
        val passwordEncoder = BCryptPasswordEncoder()
        return if (passwordEncoder.matches(password, user.password)) {
            val token = sessionUtils.generateSessionToken()
            val expirationTime = LocalDateTime.now().plusHours(1)
            val session = sessionRepository.save(Session(user.id, token, expirationTime))
            session
        } else {
            throw UserUnauthorizedException()
        }
    }

    /**
     * Logout a user
     * @param token
     * @return
     */
    fun logoutUser(token: String): String {
        sessionRepository.deleteByToken(token)
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
