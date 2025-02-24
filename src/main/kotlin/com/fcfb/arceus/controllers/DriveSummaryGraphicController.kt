package com.fcfb.arceus.controllers

import com.fcfb.arceus.service.fcfb.game.DriveSummaryGraphicService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/driveSummaryGraphic")
class DriveSummaryGraphicController(
    private var driveSummaryGraphicService: DriveSummaryGraphicService,
) {
    //@GetMapping("")
    //fun getDriveSummaryGraphicByGameId(
    //    @RequestParam("gameId") gameId: Int,
    //) = driveSummaryGraphicService.getDriveSummaryGraphicByGameId(gameId)

    @PostMapping("/generate")
    fun generateDriveSummaryGraphic(
        @RequestParam("gameId") gameId: Int,
    ) = driveSummaryGraphicService.generateDriveSummaryGraphic(gameId)
}
