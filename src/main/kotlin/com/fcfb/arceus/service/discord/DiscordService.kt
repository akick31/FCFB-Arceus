package com.fcfb.arceus.service.discord

import com.fcfb.arceus.domain.OngoingGamesEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import dev.kord.core.*
import dev.kord.gateway.PrivilegedIntent
import dev.kord.gateway.Intent
import com.fcfb.arceus.utils.Logger
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.channel.ForumChannel

@Service
class DiscordService {

    @Value("\${discord.bot.token}")
    private lateinit var botToken: String

    @Value("\${discord.guild.id}")
    private lateinit var guildId: String

    @Value("\${discord.forum.channel.id}")
    private lateinit var gameChannelId: String

    /**
     * Create a new Discord thread
     */
    suspend fun createGameThread(game: OngoingGamesEntity): Snowflake? {
        val client: Kord = loginToDiscord() ?: return null
        return try {
            val guild = client.getGuild(Snowflake(guildId))
            val gameChannel = guild.getChannel(Snowflake(gameChannelId)) as ForumChannel

            // Get the available tags in the game channel
            val availableTags = gameChannel.availableTags
            val tagsToApply = mutableListOf<Snowflake>()
            for (tag in availableTags) {
                if (tag.name == game.subdivision) {
                    tagsToApply.add(tag.id)
                }
                if (tag.name == "Week " + game.week) {
                    tagsToApply.add(tag.id)
                }
                if (tag.name == "Season " + game.season) {
                    tagsToApply.add(tag.id)
                }
            }
            if (game.scrimmage == true) {
                tagsToApply.add(availableTags.first { it.name == "Scrimmage" }.id)
            }

            // Get the thread name
            val threadName = game.homeTeam + " vs " + game.awayTeam

            // Get the thread content
            val threadContent = "[INSERT FCFB WEBSITE LINK HERE]"

            val gameThread = gameChannel.startPublicThread(threadName) {
                name = threadName
                appliedTags = tagsToApply
                message {
                    content = threadContent
                }
            }
            Logger.info("Game thread created: $gameThread")
            logoutFromDiscord(client)
            Logger.info("Logged out of Discord")
            gameThread.id
        } catch (e: Exception) {
            Logger.error("{}", e)
            logoutFromDiscord(client)
            null
        }
    }

    private suspend fun logoutFromDiscord(client: Kord) {
        client.logout()
        Logger.info("Logged out of Discord")
    }

    private suspend fun loginToDiscord(): Kord? {
        return try {
            val client = Kord(botToken)
            client.login {
                // we need to specify this to receive the content of messages
                @OptIn(PrivilegedIntent::class)
                intents += Intent.MessageContent
            }
            Logger.info("Logged in to Discord")
            client
        } catch (e: Exception) {
            Logger.error("{}", e)
            null
        }
    }
}
