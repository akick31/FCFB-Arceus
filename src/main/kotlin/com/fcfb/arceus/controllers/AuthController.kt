package com.fcfb.arceus.controllers

import com.fcfb.arceus.domain.NewSignup
import com.fcfb.arceus.service.auth.AuthService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/register")
    fun registerUser(
        @RequestBody newSignup: NewSignup,
    ) = authService.createNewSignup(newSignup)

    @PostMapping("/login")
    fun login(
        @RequestParam("usernameOrEmail") usernameOrEmail: String,
        @RequestParam("password") password: String,
    ) = authService.login(usernameOrEmail, password)

    @PostMapping("/logout")
    fun logout(
        @RequestParam("token") token: String,
    ) = authService.logout(token)

    @GetMapping("/verify")
    fun verifyEmail(
        @RequestParam("token") token: String,
    ) = authService.verifyEmail(token)

    @PutMapping("/resend-verification-email")
    fun resetVerificationToken(
        @RequestParam("id") id: Long,
    ) = authService.resetVerificationToken(id)

    // Add to AuthController.kt
    @PostMapping("/forgot-password")
    fun forgotPassword(
        @RequestParam email: String,
    ) = authService.forgotPassword(email)

    @PostMapping("/reset-password")
    fun resetPassword(
        @RequestParam token: String,
        @RequestParam userId: Long,
        @RequestParam newPassword: String,
    ) = authService.resetPassword(token, userId, newPassword)
}
