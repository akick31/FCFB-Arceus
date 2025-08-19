package com.fcfb.arceus.controllers

import com.fcfb.arceus.model.Season
import com.fcfb.arceus.service.fcfb.SeasonService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("${ApiConstants.FULL_PATH}/season")
class SeasonController(
    private var seasonService: SeasonService,
) {
    @PostMapping
    fun startSeason(): ResponseEntity<Season> = ResponseEntity.ok(seasonService.startSeason())

    @GetMapping("/current")
    fun getCurrentSeason(): ResponseEntity<Season> = ResponseEntity.ok(seasonService.getCurrentSeason())

    @GetMapping("/current/week")
    fun getCurrentWeek(): ResponseEntity<Int> = ResponseEntity.ok(seasonService.getCurrentWeek())
}
