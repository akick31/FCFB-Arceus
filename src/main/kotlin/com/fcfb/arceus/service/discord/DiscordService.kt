package com.fcfb.arceus.service.discord

import com.fcfb.arceus.domain.OngoingGamesEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import dev.kord.core.*
import dev.kord.gateway.PrivilegedIntent
import dev.kord.gateway.Intent
import com.fcfb.arceus.utils.Logger

@Service
class DiscordService(private val mailSender: JavaMailSender) {

    @Value("\${discord.bot.token}")
    private lateinit var botToken: String

    /**
     * Create a new Discord thread
     */
    suspend fun createThread(game: OngoingGamesEntity) {
        val client: Kord? = loginToDiscord()
        if (client != null) {

        }
    }

    suspend fun loginToDiscord(): Kord? {
        try {
            val client = Kord(botToken)
            client.login {
                // we need to specify this to receive the content of messages
                @OptIn(PrivilegedIntent::class)
                intents += Intent.MessageContent
            }
            return client
        } catch (e: Exception) {
            Logger.error("{}", e)
            return null
        }
    }
}
