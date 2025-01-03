package com.fcfb.arceus.controllers

import com.fcfb.arceus.dto.UserDTO
import com.fcfb.arceus.service.fcfb.UserService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/user")
class UserController(
    private var userService: UserService,
) {
    @GetMapping("id")
    fun getUserById(
        @RequestParam id: Long,
    ) = userService.getUserById(id)

    @GetMapping("/discord")
    fun getUserDTOByDiscordId(
        @RequestParam id: String,
    ) = userService.getUserDTOByDiscordId(id)

    @GetMapping("/team")
    fun getUserByTeam(
        @RequestParam team: String,
    ) = userService.getUserByTeam(team)

    @GetMapping("")
    fun getAllUsers() = userService.getAllUsers()

    @GetMapping("/name")
    fun getUserByName(
        @RequestParam name: String,
    ) = userService.getUserByName(name)

    @PutMapping("/update/password")
    fun updateUserPassword(
        @RequestParam("id") id: Long,
        @RequestParam newPassword: String,
    ) = userService.updateUserPassword(id, newPassword)

    @PutMapping("/update/email")
    fun updateUserEmail(
        @RequestParam("id") id: Long,
        @RequestParam newEmail: String,
    ) = userService.updateEmail(id, newEmail)

    @PutMapping("/update")
    fun updateUserRole(
        @RequestBody user: UserDTO,
    ) = userService.updateUser(user)

    @DeleteMapping("/{id}")
    fun deleteTeam(
        @PathVariable("id") id: Long,
    ) = userService.deleteUser(id)
}
