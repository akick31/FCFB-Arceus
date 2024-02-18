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
     * Update a user password
     * @param id
     * @param newPassword
     * @return
     */
    @PutMapping("/update/password")
    fun updateUserPassword(
        @RequestParam("id") id: Long,
        @RequestParam newPassword: String?
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }

        val user: UsersEntity = userData.get()

        // Generate new salt and hash new password
        val passwordEncoder = BCryptPasswordEncoder()
        val newSalt = passwordEncoder.encode(newPassword)
        user.password = passwordEncoder.encode(newPassword)
        user.salt = newSalt

        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    /**
     * Update a user username
     * @param id
     * @param newUsername
     */
    @PutMapping("/update/username")
    fun updateUserUsername(
        @RequestParam("id") id: Long,
        @RequestParam newUsername: String?
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        if (newUsername == null) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.username = newUsername
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    /**
     * Update a user email
     * @param id
     * @param newEmail
     */
    @PutMapping("/update/email")
    fun updateUserEmail(
        @RequestParam("id") id: Long,
        @RequestParam newEmail: String?
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        if (newEmail == null) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.email = newEmail
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    /**
     * Update a user role
     * @param id
     * @param newRole
     */
    @PutMapping("/update/role")
    fun updateUserRole(
        @RequestParam("id") id: Long,
        @RequestParam newRole: String?
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        if (newRole == null) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.role = newRole
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    /**
     * Update a user position
     * @param id
     * @param newPosition
     */
    @PutMapping("/update/position")
    fun updateUserPosition(
        @RequestParam("id") id: Long,
        @RequestParam newPosition: String?
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        if (newPosition == null) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.position = newPosition
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    /**
     * Update a user reddit username
     * @param id
     * @param newRedditUsername
     */

    @PutMapping("/update/reddit-username")
    fun updateUserRedditUsername(
        @RequestParam("id") id: Long,
        @RequestParam newRedditUsername: String?
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        if (newRedditUsername == null) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.redditUsername = newRedditUsername
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    /**
     * Update a user team
     * @param id
     * @param newTeam
     */
    @PutMapping("/update/team")
    fun updateUserTeam(
        @RequestParam("id") id: Long,
        @RequestParam newTeam: String?
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        if (newTeam == null) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.team = newTeam
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    /**
     * Update a user wins
     * @param id
     * @param newWins
     */
    @PutMapping("/update/wins")
    fun updateUserWins(
        @RequestParam("id") id: Long,
        @RequestParam newWins: Int?
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        if (newWins == null) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.wins = newWins
        user.winPercentage = user.wins.toDouble() / (user.wins + user.losses)
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    /**
     * Update a user losses
     * @param id
     * @param newLosses
     */
    @PutMapping("/update/losses")
    fun updateUserLosses(
        @RequestParam("id") id: Long,
        @RequestParam newLosses: Int?
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        if (newLosses == null) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.losses = newLosses
        user.winPercentage = user.wins.toDouble() / (user.wins + user.losses)
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    /**
     * Update a user's coach name
     * @param id
     * @param newCoachName
     */
    @PutMapping("/update/coach-name")
    fun updateUserCoachName(
        @RequestParam("id") id: Long,
        @RequestParam newCoachName: String?
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
        if (newCoachName == null) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.coachName = newCoachName
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    /**
     * Update a user's discord tag
     * @param id
     * @param newDiscordTag
     */
    @PutMapping("/update/discord-tag")
    fun updateUserDiscordTag(
        @RequestParam("id") id: Long,
        @RequestParam newDiscordTag: String?
    ): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity?> = usersRepository?.findById(id) ?: return ResponseEntity(null, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(null, HttpStatus.NOT_FOUND)
        }

        val user: UsersEntity = userData.get()
        user.discordTag = newDiscordTag
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
