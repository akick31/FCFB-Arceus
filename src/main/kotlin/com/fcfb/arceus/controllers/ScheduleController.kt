package com.fcfb.arceus.controllers

import com.fcfb.arceus.service.fcfb.ScheduleService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("${ApiConstants.FULL_PATH}/schedule")
class ScheduleController(
    private var scheduleService: ScheduleService,
) {
    /**
     * Get an opponent for a given week and team
     * @param week
     * @param team
     * @return
     */
    @GetMapping("/opponent")
    fun getTeamOpponent(
        @RequestParam("team") team: String,
    ) = scheduleService.getTeamOpponent(team)

    /**
     * Get the schedule for a given season for a team
     * @param season
     * @param team
     * @return
     */
    @GetMapping("/season")
    fun getScheduleBySeasonAndTeam(
        @RequestParam("season") season: Int,
        @RequestParam("team") team: String,
    ) = scheduleService.getScheduleBySeasonAndTeam(season, team)
}
