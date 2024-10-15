package com.fcfb.arceus.service.fcfb

import com.fcfb.arceus.domain.User
import com.fcfb.arceus.domain.User.CoachPosition
import com.fcfb.arceus.domain.User.Role
import com.fcfb.arceus.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    var usersRepository: UserRepository? = null

    private var emptyHeaders: HttpHeaders = HttpHeaders()

    fun getUserById(id: Long): ResponseEntity<User> {
        val userData: User = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        return ResponseEntity(userData, HttpStatus.OK)
    }

    fun getUserByDiscordId(discordId: String): ResponseEntity<User> {
        val userData: User = usersRepository?.findByDiscordId(discordId) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        return ResponseEntity(userData, HttpStatus.OK)
    }

    fun getUserByTeam(team: String?): ResponseEntity<User> {
        val userData: User = usersRepository?.findByTeam(team) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        return ResponseEntity(userData, HttpStatus.OK)
    }

    fun getAllUsers(): ResponseEntity<List<User>> {
        val userData: MutableIterable<User?> = usersRepository?.findAll() ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.iterator().hasNext()) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(userData.filterNotNull(), HttpStatus.OK)
    }

    fun getUserByName(name: String?): ResponseEntity<User> {
        val userData: User = usersRepository?.findByCoachName(name) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        return ResponseEntity(userData, HttpStatus.OK)
    }

    fun updateUserPassword(id: Long, newPassword: String?): ResponseEntity<User> {
        val user = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

        val passwordEncoder = BCryptPasswordEncoder()
        val newSalt = passwordEncoder.encode(newPassword)
        user.password = passwordEncoder.encode(newPassword)
        user.salt = newSalt

        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserUsername(id: Long, newUsername: String?): ResponseEntity<User> {
        val user = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

        if (newUsername == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        user.username = newUsername
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserEmail(id: Long, newEmail: String?): ResponseEntity<User> {
        val user = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

        if (newEmail == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        user.email = newEmail
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserRole(id: Long, newRole: Role?): ResponseEntity<User> {
        val user = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

        if (newRole == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        user.role = newRole
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserPosition(id: Long, newPosition: CoachPosition?): ResponseEntity<User> {
        val user = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

        if (newPosition == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        user.position = newPosition
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserRedditUsername(id: Long, newRedditUsername: String?): ResponseEntity<User> {
        val user = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

        if (newRedditUsername == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        user.redditUsername = newRedditUsername
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserTeam(id: Long, newTeam: String?): ResponseEntity<User> {
        val user = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

        if (newTeam == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        user.team = newTeam
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserWins(id: Long, newWins: Int?): ResponseEntity<User> {
        val user = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

        if (newWins == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        user.wins = newWins
        user.winPercentage = user.wins!!.toDouble() / (user.wins!! + user.losses!!)
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserLosses(id: Long, newLosses: Int?): ResponseEntity<User> {
        val user = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

        if (newLosses == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        user.losses = newLosses
        user.winPercentage = user.wins!!.toDouble() / (user.wins!! + user.losses!!)
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserCoachName(id: Long, newCoachName: String?): ResponseEntity<User> {
        val user = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

        if (newCoachName == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        user.coachName = newCoachName
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserDiscordTag(id: Long, newDiscordTag: String): ResponseEntity<User> {
        val user = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)

        user.discordTag = newDiscordTag
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun deleteUser(id: String): ResponseEntity<HttpStatus> {
        usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!usersRepository?.findById(id)!!.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        usersRepository?.deleteById(id)
        return ResponseEntity(HttpStatus.OK)
    }
}
