package com.fcfb.arceus.controllers

import com.fcfb.arceus.domain.TeamsEntity
import com.fcfb.arceus.service.team.TeamsService
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
@RequestMapping("/teams")
class TeamsController(
    private var teamsService: TeamsService
) {
    @GetMapping("/id")
    fun getTeamById(
        @RequestParam id: Int
    ) = teamsService.getTeamById(id)

    @GetMapping("")
    fun getAllTeams() = teamsService.getAllTeams()

    @GetMapping("/name")
    fun getTeamByName(
        @RequestParam name: String?
    ) = teamsService.getTeamByName(name)

    @PostMapping("")
    fun createTeam(
        @RequestBody team: TeamsEntity
    ) = teamsService.createTeam(team)

    @PutMapping("/{name}")
    fun updateTeam(
        @PathVariable("name") name: String?,
        @RequestBody team: TeamsEntity
    ) = teamsService.updateTeam(name, team)

    @DeleteMapping("/{id}")
    fun deleteTeam(
        @PathVariable("id") id: Int
    ) = teamsService.deleteTeam(id)
}
