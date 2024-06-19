package com.fcfb.arceus.controllers

import com.fcfb.arceus.service.discord.DiscordService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/discord")
class DiscordController(
    private var discordService: DiscordService
)
