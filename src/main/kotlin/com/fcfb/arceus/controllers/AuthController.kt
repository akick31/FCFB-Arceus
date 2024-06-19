package com.fcfb.arceus.controllers

import com.fcfb.arceus.domain.UsersEntity
import com.fcfb.arceus.service.auth.AuthService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    suspend fun createUser(
        @RequestBody user: UsersEntity
    ) = authService.createUser(user)

    @PostMapping("/login")
    fun loginUser(
        @RequestParam("usernameOrEmail") usernameOrEmail: String,
        @RequestParam("password") password: String
    ) = authService.loginUser(usernameOrEmail, password)

    @PostMapping("/logout")
    fun logoutUser(
        @RequestParam("token") token: String
    ) = authService.logoutUser(token)

    @GetMapping("/verify")
    fun verifyEmail(
        @RequestParam("token") token: String
    ) = authService.verifyEmail(token)

    @PutMapping("/resend-verification-email")
    fun resetVerificationToken(
        @RequestParam("id") id: Long
    ) = authService.resetVerificationToken(id)
}
