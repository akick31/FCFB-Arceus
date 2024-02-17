package com.fcfb.arceus.service.email

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(private val mailSender: JavaMailSender) {

    @Value("\${domain.url}")
    private lateinit var domainUrl: String

    /**
     * Send a verification email
     */
    fun sendVerificationEmail(email: String, userId: Long, verificationToken: String) {
        val subject = "Welcome to Fake College Football! Please verify your email and join Discord."
        val emailBody = """
            Dear User,
            
            Thank you for registering with Fake College Football! To complete your registration and gain full access to the game, please verify your email address by clicking on the following link:
            
            $domainUrl/verify?id=$userId&token=$verificationToken
            
            After verifying your email, we invite you to join our Discord community to get your team and play the game. You can join our Discord server using the following invite link:
            
            discord.gg/fcfb
            
            We're excited to have you join our community! If you have any questions or need assistance, feel free to reach out to our support team on Discord.
            
            Best regards,
            The Fake College Football Team
        """.trimIndent()

        sendEmail(email, subject, emailBody)
    }

    /**
     * Send an email
     */
    fun sendEmail(to: String, subject: String, text: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.setSubject(subject)
        message.setText(text)
        mailSender.send(message)
    }
}
