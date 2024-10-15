package com.fcfb.arceus.controllers

import com.fcfb.arceus.domain.User.CoachPosition
import com.fcfb.arceus.domain.User.Role
import com.fcfb.arceus.service.fcfb.UserService
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
@RequestMapping("/user")
class UserController(
    private var userService: UserService
) {
    @GetMapping("id")
    fun getUserById(
        @RequestParam id: Long
    ) = userService.getUserById(id)

    @GetMapping("/discord")
    fun getUserByDiscordId(
        @RequestParam id: String
    ) = userService.getUserByDiscordId(id)

    @GetMapping("/team")
    fun getUserByTeam(
        @RequestParam team: String?
    ) = userService.getUserByTeam(team)

    @GetMapping("")
    fun getAllUsers() = userService.getAllUsers()

    @GetMapping("/name")
    fun getUserByName(
        @RequestParam name: String?
    ) = userService.getUserByName(name)

    @PutMapping("/update/password")
    fun updateUserPassword(
        @RequestParam("id") id: Long,
        @RequestParam newPassword: String?
    ) = userService.updateUserPassword(id, newPassword)

    @PutMapping("/username")
    fun updateUserUsername(
        @RequestParam("id") id: Long,
        @RequestParam newUsername: String?
    ) = userService.updateUserUsername(id, newUsername)

    @PutMapping("/update/email")
    fun updateUserEmail(
        @RequestParam("id") id: Long,
        @RequestParam newEmail: String?
    ) = userService.updateUserEmail(id, newEmail)

    @PutMapping("/update/role")
    fun updateUserRole(
        @RequestParam("id") id: Long,
        @RequestParam newRole: Role?
    ) = userService.updateUserRole(id, newRole)

    @PutMapping("/update/position")
    fun updateUserPosition(
        @RequestParam("id") id: Long,
        @RequestParam newPosition: CoachPosition?
    ) = userService.updateUserPosition(id, newPosition)

    @PutMapping("/update/reddit-username")
    fun updateUserRedditUsername(
        @RequestParam("id") id: Long,
        @RequestParam newRedditUsername: String?
    ) = userService.updateUserRedditUsername(id, newRedditUsername)

    @PutMapping("/update/team")
    fun updateUserTeam(
        @RequestParam("id") id: Long,
        @RequestParam newTeam: String?
    ) = userService.updateUserTeam(id, newTeam)

    @PutMapping("/update/wins")
    fun updateUserWins(
        @RequestParam("id") id: Long,
        @RequestParam newWins: Int?
    ) = userService.updateUserWins(id, newWins)

    @PutMapping("/update/losses")
    fun updateUserLosses(
        @RequestParam("id") id: Long,
        @RequestParam newLosses: Int?
    ) = userService.updateUserLosses(id, newLosses)

    @PutMapping("/update/coach-name")
    fun updateUserCoachName(
        @RequestParam("id") id: Long,
        @RequestParam newCoachName: String?
    ) = userService.updateUserCoachName(id, newCoachName)

    @PutMapping("/update/discord-tag")
    fun updateUserDiscordTag(
        @RequestParam("id") id: Long,
        @RequestParam newDiscordTag: String
    ) = userService.updateUserDiscordTag(id, newDiscordTag)

    @DeleteMapping("/{id}")
    fun deleteTeam(
        @PathVariable("id") id: String
    ) = userService.deleteUser(id)
}
