package com.fcfb.arceus.service.discord

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.utils.Logger
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.User
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class DiscordService {
    private var restTemplate: RestTemplate? = null
    private var discordBotUrl = "http://0.0.0.0:1212/zebstrika"

    @Value("\${discord.guild.id}")
    private val guildId: String? = null

    @Value("\${discord.bot.token}")
    private val botToken: String? = null

    fun DiscordService(restTemplate: RestTemplate?) {
        this.restTemplate = restTemplate
    }

    fun startGameThread(ongoingGame: Game): String {
        val discordBotUrl = "$discordBotUrl/start_game"
        return restTemplate!!.postForEntity(discordBotUrl, ongoingGame, String::class.java).toString()
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
