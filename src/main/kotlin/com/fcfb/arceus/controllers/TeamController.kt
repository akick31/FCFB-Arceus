package com.fcfb.arceus.controllers

import com.fcfb.arceus.domain.Team
import com.fcfb.arceus.domain.User.CoachPosition
import com.fcfb.arceus.service.fcfb.TeamService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:8082"])
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

    @PutMapping("/{name}")
    fun updateTeam(
        @PathVariable("name") name: String?,
        @RequestBody team: Team,
    ) = teamService.updateTeam(name, team)

    @PostMapping("/{name}/hire")
    suspend fun hireCoach(
        @PathVariable("name") name: String?,
        @RequestParam discordId: String,
        @RequestParam coachPosition: CoachPosition,
    ) = teamService.hireCoach(name, discordId, coachPosition)

    @DeleteMapping("/{id}")
    fun deleteTeam(
        @PathVariable("id") id: Int,
    ) = teamService.deleteTeam(id)
}
