package com.fcfb.arceus.service.auth

import com.fcfb.arceus.domain.User
import com.fcfb.arceus.models.website.Session
import com.fcfb.arceus.repositories.SessionRepository
import com.fcfb.arceus.repositories.UserRepository
import com.fcfb.arceus.service.discord.DiscordService
import com.fcfb.arceus.service.email.EmailService
import com.fcfb.arceus.utils.Logger
import com.fcfb.arceus.utils.SessionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID
import javax.transaction.Transactional

@Service
open class AuthService(
    private val sessionUtils: SessionUtils,
    private val emailService: EmailService,
    private val discordService: DiscordService
) {
    @Autowired
    var usersRepository: UserRepository? = null

    @Autowired
    var sessionRepository: SessionRepository? = null

    private val emptyHeaders = HttpHeaders()

    suspend fun createUser(user: User): ResponseEntity<User> {
        return try {
            val passwordEncoder = BCryptPasswordEncoder()
            val salt = passwordEncoder.encode(user.password)
            val verificationToken = UUID.randomUUID().toString()

            val discordUser = discordService.getUserByDiscordTag(user.discordTag)
                ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            val discordId = discordUser.id.toString()

            val newUser: User = usersRepository?.save(
                User(
                    user.username,
                    user.coachName,
                    user.discordTag,
                    discordId,
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
                    0,
                    verificationToken
                )
            ) ?: return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)

            emailService.sendVerificationEmail(newUser.email, newUser.id!!, verificationToken)
            Logger.debug("User ${user.username} registered successfully. Verification email sent.")
            ResponseEntity(newUser, HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }
    }

    fun loginUser(usernameOrEmail: String, password: String): ResponseEntity<Session> {
        val user = usersRepository?.findByUsernameOrEmail(usernameOrEmail) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        val passwordEncoder = BCryptPasswordEncoder()
        return if (passwordEncoder.matches(password, user.password)) {
            val token = sessionUtils.generateSessionToken()
            val expirationTime = LocalDateTime.now().plusHours(1)
            val session = sessionRepository?.save(Session(user.id!!, token, expirationTime))
            ResponseEntity(session, HttpStatus.OK)
        } else {
            ResponseEntity(emptyHeaders, HttpStatus.UNAUTHORIZED)
        }
    }

    @Transactional
    open fun logoutUser(token: String): ResponseEntity<String> {
        sessionRepository?.deleteByToken(token)
        return ResponseEntity("User logged out successfully", HttpStatus.OK)
    }

    fun verifyEmail(token: String): ResponseEntity<String> {
        val user = usersRepository?.findByVerificationToken(token) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        user.approved = 1
        usersRepository?.save(user)
        return ResponseEntity("Email verified successfully", HttpStatus.OK)
    }

    fun resetVerificationToken(id: Long): ResponseEntity<User> {
        val user: User = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        val verificationToken = UUID.randomUUID().toString()
        user.verificationToken = verificationToken
        usersRepository?.save(user)
        emailService.sendVerificationEmail(user.email, user.id!!, verificationToken)
        return ResponseEntity(user, HttpStatus.OK)
    }
}
