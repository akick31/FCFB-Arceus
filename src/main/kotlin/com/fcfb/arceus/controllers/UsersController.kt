package com.fcfb.arceus.controllers

import com.fcfb.arceus.service.user.UsersService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/arceus/users")
class UsersController(
    private var usersService: UsersService
) {
    @GetMapping("id")
    fun getUserById(
        @RequestParam id: Long
    ) = usersService.getUserById(id)

    @GetMapping("/team")
    fun getUserByTeam(
        @RequestParam team: String?
    ) = usersService.getUserByTeam(team)

    @GetMapping("")
    fun getAllUsers() = usersService.getAllUsers()

    @GetMapping("/name")
    fun getUserByName(
        @RequestParam name: String?
    ) = usersService.getUserByName(name)

    @PutMapping("/update/password")
    fun updateUserPassword(
        @RequestParam("id") id: Long,
        @RequestParam newPassword: String?
    ) = usersService.updateUserPassword(id, newPassword)

    @PutMapping("/username")
    fun updateUserUsername(
        @RequestParam("id") id: Long,
        @RequestParam newUsername: String?
    ) = usersService.updateUserUsername(id, newUsername)

    @PutMapping("/update/email")
    fun updateUserEmail(
        @RequestParam("id") id: Long,
        @RequestParam newEmail: String?
    ) = usersService.updateUserEmail(id, newEmail)

    @PutMapping("/update/role")
    fun updateUserRole(
        @RequestParam("id") id: Long,
        @RequestParam newRole: String?
    ) = usersService.updateUserRole(id, newRole)

    @PutMapping("/update/position")
    fun updateUserPosition(
        @RequestParam("id") id: Long,
        @RequestParam newPosition: String?
    ) = usersService.updateUserPosition(id, newPosition)

    @PutMapping("/update/reddit-username")
    fun updateUserRedditUsername(
        @RequestParam("id") id: Long,
        @RequestParam newRedditUsername: String?
    ) = usersService.updateUserRedditUsername(id, newRedditUsername)

    @PutMapping("/update/team")
    fun updateUserTeam(
        @RequestParam("id") id: Long,
        @RequestParam newTeam: String?
    ) = usersService.updateUserTeam(id, newTeam)

    @PutMapping("/update/wins")
    fun updateUserWins(
        @RequestParam("id") id: Long,
        @RequestParam newWins: Int?
    ) = usersService.updateUserWins(id, newWins)

    @PutMapping("/update/losses")
    fun updateUserLosses(
        @RequestParam("id") id: Long,
        @RequestParam newLosses: Int?
    ) = usersService.updateUserLosses(id, newLosses)

    @PutMapping("/update/coach-name")
    fun updateUserCoachName(
        @RequestParam("id") id: Long,
        @RequestParam newCoachName: String?
    ) = usersService.updateUserCoachName(id, newCoachName)

    @PutMapping("/update/discord-tag")
    fun updateUserDiscordTag(
        @RequestParam("id") id: Long,
        @RequestParam newDiscordTag: String
    ) = usersService.updateUserDiscordTag(id, newDiscordTag)

    @DeleteMapping("/{id}")
    fun deleteTeam(
        @PathVariable("id") id: String
    ) = usersService.deleteUser(id)
}
