package com.fcfb.arceus.controllers

import com.fcfb.arceus.domain.Team
import com.fcfb.arceus.domain.User.CoachPosition
import com.fcfb.arceus.service.fcfb.TeamService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/team")
class TeamController(
    private var teamService: TeamService,
) {
    @GetMapping("/id")
    fun getTeamById(
        @RequestParam id: Int,
    ) = teamService.getTeamById(id)

    @GetMapping("")
    fun getAllTeams() = teamService.getAllTeams()

    @GetMapping("/name")
    fun getTeamByName(
        @RequestParam name: String?,
    ) = teamService.getTeamByName(name)

    @PostMapping("")
    fun createTeam(
        @RequestBody team: Team,
    ) = teamService.createTeam(team)

    @PutMapping("")
    fun updateTeam(
        @RequestBody team: Team,
    ) = teamService.updateTeam(team)

    @PutMapping("/color")
    fun updateTeamColor(
        @RequestParam team: String,
        @RequestParam color: String,
    ) = teamService.updateTeamColor(team, color)

    @PostMapping("/hire")
    suspend fun hireCoach(
        @RequestParam name: String?,
        @RequestParam discordId: String,
        @RequestParam coachPosition: CoachPosition,
        @RequestParam processedBy: String,
    ) = teamService.hireCoach(name, discordId, coachPosition, processedBy)

    @PostMapping("/fire")
    fun hireCoach(
        @RequestParam team: String,
        @RequestParam processedBy: String,
    ) = teamService.fireCoach(team, processedBy)

    @DeleteMapping("")
    fun deleteTeam(
        @RequestParam id: Int,
    ) = teamService.deleteTeam(id)
}
