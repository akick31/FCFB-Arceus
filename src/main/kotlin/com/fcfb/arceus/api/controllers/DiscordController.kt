package com.fcfb.arceus.api.controllers

import com.fcfb.arceus.domain.OngoingGamesEntity
import com.fcfb.arceus.domain.TeamsEntity
import com.fcfb.arceus.service.discord.DiscordService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/arceus/discord")
class DiscordController(
    private var discordService: DiscordService
) {
    @PostMapping("/create_game_thread")
    suspend fun createGameThread(
        @RequestBody game: OngoingGamesEntity
    ) = discordService.createGameThread(game)
}