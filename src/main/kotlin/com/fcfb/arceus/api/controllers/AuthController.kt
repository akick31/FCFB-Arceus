package com.fcfb.arceus.api.controllers

import com.fcfb.arceus.domain.UsersEntity
import com.fcfb.arceus.api.repositories.UsersRepository
import com.fcfb.arceus.api.repositories.SessionRepository
import com.fcfb.arceus.models.Session
import com.fcfb.arceus.service.email.EmailService
import com.fcfb.arceus.utils.Logger
import com.fcfb.arceus.utils.SessionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/arceus/auth")
open class AuthController(
    private var sessionUtils: SessionUtils,
    private var emailService: EmailService
) {
    @Autowired
    var usersRepository: UsersRepository? = null

    @Autowired
    var sessionRepository: SessionRepository? = null

    /**
     * Register a new yser
     * @param user
     * @return
     */
    @PostMapping("/register")
    fun createUser(
        @RequestBody user: UsersEntity
    ): ResponseEntity<UsersEntity> {
        return try {
            // Generate salt and hash password
            val passwordEncoder = BCryptPasswordEncoder()
            val salt = passwordEncoder.encode(user.password)

            // Generate verification token
            val verificationToken = UUID.randomUUID().toString()

            val newUser: UsersEntity = usersRepository?.save(
                UsersEntity(
                    user.username,
                    user.coachName,
                    user.discordTag,
                    user.email,
                    0,
                    passwordEncoder.encode(user.password),
                    user.position,
                    user.redditUsername,
                    "user",
                    salt,
                    null,
                    0.0,
                    0,
                    0, // Approved
                    verificationToken
                )
            ) ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST)

            // Send verification email
            emailService.sendVerificationEmail(newUser.email, newUser.id, verificationToken)

            Logger.debug("User ${user.username} registered successfully. Verification email sent.")
            ResponseEntity(newUser, HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * Login a user
     * @param usernameOrEmail
     * @param password
     * @return
     */
    @PostMapping("/login")
    fun loginUser(
        @RequestParam("usernameOrEmail") usernameOrEmail: String,
        @RequestParam("password") password: String
    ): ResponseEntity<Session> {
        val userData: Optional<UsersEntity?> =
            usersRepository?.findByUsernameOrEmail(usernameOrEmail) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        val user = userData.get()
        val passwordEncoder = BCryptPasswordEncoder()
        if (passwordEncoder.matches(password, user.password)) {
            // Passwords match, generate session token
            val token = sessionUtils.generateSessionToken()

            // Set expiration time for the session (e.g., 1 hour from now)
            val expirationTime = LocalDateTime.now().plusHours(1)

            // Save session information in the database
            val session = sessionRepository?.save(Session(user.id, token, expirationTime))

            // Return session information to the client
            return ResponseEntity(session, HttpStatus.OK)
        } else {
            // Passwords do not match
            return ResponseEntity(null, HttpStatus.UNAUTHORIZED)
        }
    }

    /**
     * Logout a user
     * @param userId
     * @return
     */
    @PostMapping("/logout")
    @Transactional
    open fun logoutUser(
        @RequestParam("token") token: String
    ): ResponseEntity<String> {
        sessionRepository?.deleteByToken(token)
        return ResponseEntity("User logged out successfully", HttpStatus.OK)
    }

    /**
     * Verify a user's email address
     * @param token
     * @return
     */
    @GetMapping("/verify")
    fun verifyEmail(
        @RequestParam("token") token: String
    ): ResponseEntity<String> {
        val userData: Optional<UsersEntity?> = usersRepository?.findByVerificationToken(token) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        val user = userData.get()
        user.approved = 1
        usersRepository?.save(user)
        return ResponseEntity("Email verified successfully", HttpStatus.OK)
    }

    /**
     * Reset a verification token
     * @param id
     * @return
     */
    @PutMapping("/resend-verification-email")
    fun resetVerificationToken(
        @RequestParam("id") id: Long
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        val user = userData.get()
        val verificationToken = UUID.randomUUID().toString()
        user.verificationToken = verificationToken
        usersRepository?.save(user)

        // Send verification email
        emailService.sendVerificationEmail(user.email, user.id, verificationToken)

        return ResponseEntity(user, HttpStatus.OK)
    }
}

