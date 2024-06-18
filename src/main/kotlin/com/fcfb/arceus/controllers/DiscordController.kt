package com.fcfb.arceus.controllers

import com.fcfb.arceus.service.discord.DiscordService
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/discord")
class DiscordController(
    private var discordService: DiscordService
) {

}