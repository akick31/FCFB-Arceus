package com.fcfb.arceus.controllers

import com.fcfb.arceus.model.NewSignup
import com.fcfb.arceus.service.auth.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("${ApiConstants.FULL_PATH}/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping
    fun registerUser(
        @RequestBody newSignup: NewSignup,
    ): ResponseEntity<NewSignup> = ResponseEntity.ok(authService.createNewSignup(newSignup))

    @PostMapping("/sessions")
    fun login(
        @RequestParam("usernameOrEmail") usernameOrEmail: String,
        @RequestParam("password") password: String,
    ): ResponseEntity<Any> = ResponseEntity.ok(authService.login(usernameOrEmail, password))

    @DeleteMapping("/sessions/{token}")
    fun logout(
        @PathVariable("token") token: String,
    ): ResponseEntity<String> = ResponseEntity.ok(authService.logout(token))

    @GetMapping("/verify-email")
    fun verifyEmail(
        @RequestParam("token") token: String,
    ): ResponseEntity<Boolean> = ResponseEntity.ok(authService.verifyEmail(token))

    @PutMapping("/{id}/verification-email")
    fun resetVerificationToken(
        @PathVariable("id") id: Long,
    ): ResponseEntity<NewSignup> = ResponseEntity.ok(authService.resetVerificationToken(id))

    @PostMapping("/password-reset")
    fun forgotPassword(
        @RequestParam email: String,
    ): ResponseEntity<String> = authService.forgotPassword(email)

    @PutMapping("/{userId}/password")
    fun resetPassword(
        @RequestParam token: String,
        @PathVariable("userId") userId: Long,
        @RequestParam newPassword: String,
    ): ResponseEntity<String> = authService.resetPassword(token, userId, newPassword)
}
