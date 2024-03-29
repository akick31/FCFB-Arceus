package com.fcfb.arceus.service.discord

import com.fcfb.arceus.api.repositories.GameMessagesRepository
import com.fcfb.arceus.domain.OngoingGamesEntity
import com.fcfb.arceus.models.Game
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import dev.kord.core.*
import dev.kord.gateway.PrivilegedIntent
import dev.kord.gateway.Intent
import com.fcfb.arceus.utils.Logger
import dev.kord.common.annotation.KordExperimental
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.channel.ForumChannel
import dev.kord.core.entity.channel.thread.TextChannelThread
import org.springframework.beans.factory.annotation.Autowired

@Service
class DiscordService {
    @Autowired
    var gameMessagesRepository: GameMessagesRepository? = null

    @Value("\${discord.bot.token}")
    private lateinit var botToken: String

    @Value("\${discord.guild.id}")
    private lateinit var guildId: String

    @Value("\${discord.forum.channel.id}")
    private lateinit var gameChannelId: String

    //TODO: Prompt for coin toss
    private suspend fun sendMessage(game: OngoingGamesEntity, gameThread: TextChannelThread, scenario: Game.Scenario) {
        var messageContent = gameMessagesRepository?.findByScenario(scenario.toString())?.message ?: return

        // Replace the placeholders in the message
        if ("{home_coach}" in messageContent) {
            messageContent = messageContent.replace("{home_coach}", "@${game.homeCoach}")
        }
        if ("{away_coach}" in messageContent) {
            messageContent = messageContent.replace("{away_coach}", "@${game.awayCoach}")
        }
        if ("<br>" in messageContent) {
            messageContent = messageContent.replace("<br>", "\n")
        }

        // Append the users to ping to the message
        messageContent += "\n\n @${game.homeCoach} @${game.awayCoach}"

        gameThread.createMessage(messageContent)
    }

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

            sendMessage(game, gameThread, Game.Scenario.GAME_START)

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
