package com.fcfb.arceus.controllers

import com.fcfb.arceus.domain.NewSignup
import com.fcfb.arceus.service.auth.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/users")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping
    fun registerUser(@RequestBody newSignup: NewSignup): ResponseEntity<NewSignup> = ResponseEntity.ok(authService.createNewSignup(newSignup))

    @PostMapping("/sessions")
    fun login(@RequestParam("usernameOrEmail") usernameOrEmail: String, @RequestParam("password") password: String): ResponseEntity<Any> = ResponseEntity.ok(authService.login(usernameOrEmail, password))

    @DeleteMapping("/sessions/{token}")
    fun logout(@PathVariable("token") token: String): ResponseEntity<String> = ResponseEntity.ok(authService.logout(token))

    @GetMapping("/verify-email")
    fun verifyEmail(@RequestParam("token") token: String): ResponseEntity<Boolean> = ResponseEntity.ok(authService.verifyEmail(token))

    @PutMapping("/{id}/verification-email")
    fun resetVerificationToken(@PathVariable("id") id: Long): ResponseEntity<NewSignup> = ResponseEntity.ok(authService.resetVerificationToken(id))

    @PostMapping("/password-reset")
    fun forgotPassword(@RequestParam email: String): ResponseEntity<String> = authService.forgotPassword(email)

    @PutMapping("/{userId}/password")
    fun resetPassword(@RequestParam token: String, @PathVariable("userId") userId: Long, @RequestParam newPassword: String): ResponseEntity<String> = authService.resetPassword(token, userId, newPassword)
}