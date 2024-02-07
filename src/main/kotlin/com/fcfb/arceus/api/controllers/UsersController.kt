package com.fcfb.arceus.api.controllers

import com.fcfb.arceus.domain.UsersEntity
import com.fcfb.arceus.api.repositories.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@CrossOrigin(origins = ["http://localhost:8082"])
@RestController
@RequestMapping("/arceus/users")
class UsersController {
    @Autowired
    var usersRepository: UsersRepository? = null

    /**
     * Get a user by id
     * @param id
     * @return
     */
    @GetMapping("/id")
    fun getUserById(
        @RequestParam id: String
    ): ResponseEntity<String> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)

        return ResponseEntity(userData.get().toString(), HttpStatus.OK)
    }

    /**
     * Get a user by team
     * @param team
     * @return
     */
    @GetMapping("/team")
    fun getUserByTeam(
        @RequestParam team: String?
    ): ResponseEntity<String> {
        val userData: Optional<UsersEntity?> = usersRepository?.findByTeam(team) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)

        return ResponseEntity(userData.get().toString(), HttpStatus.OK)
    }

    @GetMapping("")
    fun getAllUsers(): ResponseEntity<String> {
        val userData: Iterable<UsersEntity?> =
            usersRepository?.findAll() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        return if (userData.iterator().hasNext()) {
            val usersString = userData.joinToString("\n") { user -> user.toString() }
            ResponseEntity(usersString, HttpStatus.OK)
        } else {
            ResponseEntity("No users found", HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Get a user by name
     * @param name
     * @return
     */
    @GetMapping("/name")
    fun getUserByName(
        @RequestParam name: String?
    ): ResponseEntity<String> {
        val userData: Optional<UsersEntity?> = usersRepository?.findByCoachName(name) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)

        return ResponseEntity(userData.get().toString(), HttpStatus.OK)
    }

    /**
     * Create a new yser
     * @param user
     * @return
     */
    @PostMapping("")
    fun createUser(
        @RequestBody user: UsersEntity
    ): ResponseEntity<String> {
        return try {
            val newUser: UsersEntity = usersRepository?.save(
                UsersEntity(
                    user.username,
                    user.coachName,
                    user.discordTag,
                    user.email,
                    0,
                    user.password,
                    user.position,
                    user.redditUsername,
                    user.role,
                    user.salt,
                    user.team,
                    0.0,
                    0,
                    user.approved
                )
            ) ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST)
            ResponseEntity(newUser.toString(), HttpStatus.CREATED )
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
    }

    /**
     * Update a user
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable("id") id: String,
        @RequestBody user: UsersEntity?
    ): ResponseEntity<String> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        val user: UsersEntity = userData.get()
        usersRepository!!.save(user)
        return ResponseEntity(user.toString(), HttpStatus.OK)
    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    fun deleteTeam(
        @PathVariable("id") id: String
    ): ResponseEntity<HttpStatus> {
        usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        usersRepository?.deleteById(id)
        return ResponseEntity(HttpStatus.OK)
    }
}