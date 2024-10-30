package com.fcfb.arceus.controllers

import com.fcfb.arceus.service.fcfb.SeasonService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/season")
class SeasonController(
    private var seasonService: SeasonService,
) {
    @PostMapping("/start")
    fun startSeason() = seasonService.startSeason()

    @GetMapping("/current")
    fun getCurrentSeason() = seasonService.getCurrentSeason()
}
