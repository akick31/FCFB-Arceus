package com.fcfb.arceus.service.discord

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.models.requests.SignupInfo
import com.fcfb.arceus.utils.DiscordUserNotFoundException
import com.fcfb.arceus.utils.Logger
import com.fcfb.arceus.utils.ServerUtils
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
    private val restTemplate: RestTemplate,
    private val serverUtils: ServerUtils,
) {
    @Value("\${discord.bot.url}")
    private var discordBotUrl: String? = null

    @Value("\${discord.guild.id}")
    private val guildId: String? = null

    @Value("\${discord.bot.token}")
    private val botToken: String? = null

    /**
     * Start a game thread in Discord
     * @param game
     * @return List<String>?
     */
    suspend fun createGameThread(game: Game): List<String>? {
        val discordBotUrl = "$discordBotUrl/start_game"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(game, headers)

        return try {
            val response =
                serverUtils.retryWithExponentialBackoff {
                    restTemplate.postForEntity(discordBotUrl, requestEntity, String::class.java)
                }
            response.body?.split(",")
        } catch (e: Exception) {
            Logger.error("There was an error starting the game thread for ${game.gameId}: ${e.message}")
            null
        }
    }

    /**
     * Notify game of a delay of game
     * @param game
     * @return Boolean
     */
    fun notifyDelayOfGame(
        game: Game,
        isDelayofGameOut: Boolean,
    ) {
        val discordBotUrl = "$discordBotUrl/delay_of_game?isDelayOfGameOut=$isDelayofGameOut"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(game, headers)
        try {
            restTemplate.postForEntity(discordBotUrl, requestEntity, String::class.java)
        } catch (e: Exception) {
            Logger.error("There was an error notifying the delay of game for ${game.gameId}: " + e.message)
        }
    }

    /**
     * Notify game of a warning for a delay of game
     * @param game
     * @return Boolean
     */
    fun notifyWarning(game: Game) {
        val discordBotUrl = "$discordBotUrl/delay_of_game_warning"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(game, headers)
        try {
            restTemplate.postForEntity(discordBotUrl, requestEntity, String::class.java)
        } catch (e: Exception) {
            Logger.error("There was an error notifying the delay of game  warning for ${game.gameId}: " + e.message)
        }
    }

    /**
     * Notify commissioners of a new signup
     */
    fun sendRegistrationNotice(signupInfo: SignupInfo) {
        val discordBotUrl = "$discordBotUrl/new_signup"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestEntity = HttpEntity(signupInfo, headers)
        try {
            restTemplate.postForEntity(discordBotUrl, requestEntity, String::class.java)
        } catch (e: Exception) {
            Logger.error("There was an error notifying the commissioners of a new signup: " + e.message)
        }
    }

    /**
     * Get a user by their Discord Tag
     * @param tag
     * @return User
     */
    suspend fun getUserByDiscordTag(tag: String): User {
        try {
            val client = Kord(botToken!!)
            val guild = client.getGuild(Snowflake(guildId!!))
            val members = guild.members.toList()
            for (member in members) {
                if (member.username == tag) {
                    val user = client.getUser(Snowflake(member.id.value)) ?: throw DiscordUserNotFoundException()
                    return user
                }
            }
            throw DiscordUserNotFoundException()
        } catch (e: Exception) {
            Logger.error("{}", e)
            throw DiscordUserNotFoundException()
        }
    }
}
