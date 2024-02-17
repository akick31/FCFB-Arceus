package com.fcfb.arceus.api.controllers

import com.fcfb.arceus.domain.UsersEntity
import com.fcfb.arceus.api.repositories.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
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
    @GetMapping("id")
    fun getUserById(
        @RequestParam id: Long
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(userData.get(), HttpStatus.OK)
    }

    /**
     * Get a user by team
     * @param team
     * @return
     */
    @GetMapping("/team")
    fun getUserByTeam(
        @RequestParam team: String?
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findByTeam(team) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(userData.get(), HttpStatus.OK)
    }

    @GetMapping("")
    fun getAllUsers(): ResponseEntity<List<UsersEntity>> {
        val userData: Iterable<UsersEntity?> = usersRepository?.findAll() ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.iterator().hasNext()) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(userData.filterNotNull(), HttpStatus.OK)
    }

    /**
     * Get a user by name
     * @param name
     * @return
     */
    @GetMapping("/name")
    fun getUserByName(
        @RequestParam name: String?
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findByCoachName(name) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(userData.get(), HttpStatus.OK)
    }

    /**
     * Update a user
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable("id") id: String,
        @RequestBody updatedUser: UsersEntity?
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }

        val user: UsersEntity = userData.get()

        // Check if password is being updated
        if (updatedUser?.password != null) {
            // Generate new salt and hash new password
            val passwordEncoder = BCryptPasswordEncoder()
            val newSalt = passwordEncoder.encode(updatedUser.password)
            user.password = passwordEncoder.encode(updatedUser.password)
            user.salt = newSalt
        }

        // Update other user information
        // Update username
        if (updatedUser?.username != null) {
            user.username = updatedUser.username
        }

        // Update coachName
        if (updatedUser?.coachName != null) {
            user.coachName = updatedUser.coachName
        }

        // Update discordTag
        if (updatedUser?.discordTag != null) {
            user.discordTag = updatedUser.discordTag
        }

        // Update email
        if (updatedUser?.email != null) {
            user.email = updatedUser.email
        }

        // Update role
        if (updatedUser?.role != null) {
            user.role = updatedUser.role
        }

        // Update position
        if (updatedUser?.position != null) {
            user.position = updatedUser.position
        }

        // Update reddit username
        if (updatedUser?.redditUsername != null) {
            user.redditUsername = updatedUser.redditUsername
        }

        // Update team
        if (updatedUser?.team != null) {
            user.team = updatedUser.team
        }

        // Update wins
        if (updatedUser?.wins != null) {
            user.wins = updatedUser.wins
        }

        // Update losses
        if (updatedUser?.losses != null) {
            user.losses = updatedUser.losses
        }

        // Update winPercentage
        user.winPercentage = user.wins.toDouble() / (user.wins + user.losses)

        // Update approved
        if (updatedUser?.approved != null) {
            user.approved = updatedUser.approved
        }

        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
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
        if (!usersRepository?.findById(id)!!.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        usersRepository?.deleteById(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
