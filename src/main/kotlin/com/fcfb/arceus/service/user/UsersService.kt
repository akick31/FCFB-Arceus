package com.fcfb.arceus.service.user

import com.fcfb.arceus.domain.UsersEntity
import com.fcfb.arceus.repositories.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class UsersService {
    @Autowired
    var usersRepository: UsersRepository? = null

    private var emptyHeaders: HttpHeaders = HttpHeaders()

    fun getUserById(id: Long): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity> = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(userData.get(), HttpStatus.OK)
    }

    fun getUserByTeam(team: String?): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity> = usersRepository?.findByTeam(team) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(userData.get(), HttpStatus.OK)
    }

    fun getAllUsers(): ResponseEntity<List<UsersEntity>> {
        val userData: MutableIterable<UsersEntity?> = usersRepository?.findAll() ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.iterator().hasNext()) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(userData.filterNotNull(), HttpStatus.OK)
    }

    fun getUserByName(name: String?): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity> = usersRepository?.findByCoachName(name) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity(userData.get(), HttpStatus.OK)
    }

    fun updateUserPassword(id: Long, newPassword: String?): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity> = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }

        val user: UsersEntity = userData.get()

        val passwordEncoder = BCryptPasswordEncoder()
        val newSalt = passwordEncoder.encode(newPassword)
        user.password = passwordEncoder.encode(newPassword)
        user.salt = newSalt

        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserUsername(id: Long, newUsername: String?): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity> = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        if (newUsername == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.username = newUsername
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserEmail(id: Long, newEmail: String?): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity> = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        if (newEmail == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.email = newEmail
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserRole(id: Long, newRole: String?): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity> = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        if (newRole == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.role = newRole
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserPosition(id: Long, newPosition: String?): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity> = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        if (newPosition == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.position = newPosition
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserRedditUsername(id: Long, newRedditUsername: String?): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity> = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        if (newRedditUsername == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.redditUsername = newRedditUsername
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserTeam(id: Long, newTeam: String?): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity> = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        if (newTeam == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.team = newTeam
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserWins(id: Long, newWins: Int?): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity> = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        if (newWins == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.wins = newWins
        user.winPercentage = user.wins!!.toDouble() / (user.wins!! + user.losses!!)
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserLosses(id: Long, newLosses: Int?): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity> = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        if (newLosses == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.losses = newLosses
        user.winPercentage = user.wins!!.toDouble() / (user.wins!! + user.losses!!)
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserCoachName(id: Long, newCoachName: String?): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity> = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
        if (newCoachName == null) {
            return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
        }

        val user: UsersEntity = userData.get()
        user.coachName = newCoachName
        usersRepository!!.save(user)
        return ResponseEntity(user, HttpStatus.OK)
    }

    fun updateUserDiscordTag(id: Long, newDiscordTag: String): ResponseEntity<UsersEntity> {
        val userData: Optional<UsersEntity> = usersRepository?.findById(id) ?: return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        if (!userData.isPresent) {
            return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }

        val user: UsersEntity = userData.get()
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
