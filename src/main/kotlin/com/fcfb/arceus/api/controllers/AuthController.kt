package com.fcfb.arceus.api.controllers

import com.fcfb.arceus.domain.UsersEntity
import com.fcfb.arceus.api.repositories.UsersRepository
import com.fcfb.arceus.api.repositories.SessionRepository
import com.fcfb.arceus.models.Session
import com.fcfb.arceus.utils.SessionUtils
import org.springframework.beans.factory.annotation.Autowired
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
class AuthController(
    private var sessionUtils: SessionUtils
) {
    @Autowired
    var usersRepository: UsersRepository? = null

    @Autowired
    var sessionRepository: SessionRepository? = null

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
    fun logoutUser(
        @RequestParam("token") token: String
    ): ResponseEntity<String> {
        sessionRepository?.deleteByToken(token)
        return ResponseEntity("User logged out successfully", HttpStatus.OK)
    }
}

