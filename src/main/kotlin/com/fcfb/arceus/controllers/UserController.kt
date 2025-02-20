package com.fcfb.arceus.controllers

import com.fcfb.arceus.models.dto.UserDTO
import com.fcfb.arceus.models.requests.UserValidationRequest
import com.fcfb.arceus.service.fcfb.UserService
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

    @GetMapping("/free_agents")
    fun getFreeAgents() = userService.getFreeAgents()

    @GetMapping("/name")
    fun getUserDTOByName(
        @RequestParam name: String,
    ) = userService.getUserDTOByName(name)

    @PutMapping("/update/email")
    fun updateUserEmail(
        @RequestParam id: Long,
        @RequestParam newEmail: String,
    ) = userService.updateEmail(id, newEmail)

    @PutMapping("/update")
    fun updateUserRole(
        @RequestBody user: UserDTO,
    ) = userService.updateUser(user)

    @PostMapping("/hash_emails")
    fun encryptEmails() = userService.hashEmails()

    @PostMapping("/validate")
    fun validateUser(
        @RequestBody userValidationRequest: UserValidationRequest,
    ) = userService.validateUser(userValidationRequest)

    @DeleteMapping("")
    fun deleteTeam(
        @RequestParam id: Long,
    ) = userService.deleteUser(id)
}
