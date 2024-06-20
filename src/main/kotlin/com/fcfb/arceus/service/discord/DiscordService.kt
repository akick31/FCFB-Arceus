package com.fcfb.arceus.service.discord

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.utils.Logger
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.User
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class DiscordService(
    private val restTemplate: RestTemplate
) {
    private var discordBotUrl = "http://0.0.0.0:1212/zebstrika"

    @Value("\${discord.guild.id}")
    private val guildId: String? = null

    @Value("\${discord.bot.token}")
    private val botToken: String? = null

    fun startGameThread(game: Game): String {
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(JavaTimeModule())
        val gameJson = objectMapper.writeValueAsString(game)
        val discordBotUrl = "$discordBotUrl/start_game"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(gameJson, headers)
        return restTemplate.postForEntity(discordBotUrl, requestEntity, String::class.java).toString()
    }

    suspend fun getUserByDiscordTag(
        tag: String
    ): User? {
        return try {
            val client = Kord(botToken!!)
            val guild = client.getGuild(Snowflake(guildId!!))
            val members = guild.members.toList()
            for (member in members) {
                if (member.username == tag) {
                    val user = client.getUser(Snowflake(member.id.value))
                    return user
                }
            }
            null
        } catch (e: Exception) {
            Logger.error("{}", e)
            null
        }
    }
}
