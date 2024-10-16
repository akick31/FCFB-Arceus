package com.fcfb.arceus.service.fcfb

import com.fcfb.arceus.converter.DTOConverter
import com.fcfb.arceus.domain.User.CoachPosition
import com.fcfb.arceus.domain.User.Role
import com.fcfb.arceus.dto.UserDTO
import com.fcfb.arceus.repositories.UserRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val dtoConverter: DTOConverter,
) {

    private var emptyHeaders: HttpHeaders = HttpHeaders()

    fun getUserById(id: Long): ResponseEntity<UserDTO> {
        val userData = userRepository.findById(id)
        return if (userData != null) {
            ResponseEntity(dtoConverter.convertToUserDTO(userData), HttpStatus.OK)
        } else {
            ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
    }

    fun getUserByDiscordId(discordId: String): ResponseEntity<UserDTO> {
        val userData = userRepository.findByDiscordId(discordId)
        return if (userData != null) {
            ResponseEntity(dtoConverter.convertToUserDTO(userData), HttpStatus.OK)
        } else {
            ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
    }

    fun getUserByTeam(team: String?): ResponseEntity<UserDTO> {
        val userData = userRepository.findByTeam(team)
        return if (userData != null) {
            ResponseEntity(dtoConverter.convertToUserDTO(userData), HttpStatus.OK)
        } else {
            ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
    }

    fun getAllUsers(): ResponseEntity<List<UserDTO>> {
        val userData = userRepository.findAll().filterNotNull()
        return if (userData.isNotEmpty()) {
            ResponseEntity(userData.map { dtoConverter.convertToUserDTO(it) }, HttpStatus.OK)
        } else {
            ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
    }

    fun getUserByName(name: String?): ResponseEntity<UserDTO> {
        val userData = userRepository.findByCoachName(name)
        return if (userData != null) {
            ResponseEntity(dtoConverter.convertToUserDTO(userData), HttpStatus.OK)
        } else {
            ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
    }

    fun updateUserPassword(id: Long, newPassword: String?): ResponseEntity<UserDTO> {
        val user = userRepository.findById(id)
        if (user != null) {
            if (newPassword.isNullOrEmpty()) {
                return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            }

            val passwordEncoder = BCryptPasswordEncoder()
            user.password = passwordEncoder.encode(newPassword)
            user.salt = passwordEncoder.encode(newPassword)

            userRepository.save(user)
            return ResponseEntity(dtoConverter.convertToUserDTO(user), HttpStatus.OK)
        }
        return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
    }

    fun updateUserUsername(id: Long, newUsername: String?): ResponseEntity<UserDTO> {
        val user = userRepository.findById(id)
        if (user != null) {
            if (newUsername.isNullOrEmpty()) {
                return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            }

            user.username = newUsername
            userRepository.save(user)
            return ResponseEntity(dtoConverter.convertToUserDTO(user), HttpStatus.OK)
        }
        return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
    }

    fun updateUserEmail(id: Long, newEmail: String?): ResponseEntity<UserDTO> {
        val user = userRepository.findById(id)
        if (user != null) {
            if (newEmail.isNullOrEmpty()) {
                return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            }

            user.email = newEmail
            userRepository.save(user)
            return ResponseEntity(dtoConverter.convertToUserDTO(user), HttpStatus.OK)
        }
        return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
    }

    fun updateUserRole(id: Long, newRole: Role?): ResponseEntity<UserDTO> {
        val user = userRepository.findById(id)
        if (user != null) {
            if (newRole == null) {
                return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            }

            user.role = newRole
            userRepository.save(user)
            return ResponseEntity(dtoConverter.convertToUserDTO(user), HttpStatus.OK)
        }
        return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
    }

    fun updateUserPosition(id: Long, newPosition: CoachPosition?): ResponseEntity<UserDTO> {
        val user = userRepository.findById(id)
        if (user != null) {
            if (newPosition == null) {
                return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            }

            user.position = newPosition
            userRepository.save(user)
            return ResponseEntity(dtoConverter.convertToUserDTO(user), HttpStatus.OK)
        }
        return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
    }

    fun updateUserRedditUsername(id: Long, newRedditUsername: String?): ResponseEntity<UserDTO> {
        val user = userRepository.findById(id)
        if (user != null) {
            if (newRedditUsername.isNullOrEmpty()) {
                return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            }

            user.redditUsername = newRedditUsername
            userRepository.save(user)
            return ResponseEntity(dtoConverter.convertToUserDTO(user), HttpStatus.OK)
        }
        return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
    }

    fun updateUserTeam(id: Long, newTeam: String?): ResponseEntity<UserDTO> {
        val user = userRepository.findById(id)
        if (user != null) {
            if (newTeam.isNullOrEmpty()) {
                return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            }

            user.team = newTeam
            userRepository.save(user)
            return ResponseEntity(dtoConverter.convertToUserDTO(user), HttpStatus.OK)
        }
        return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
    }

    fun updateUserWins(id: Long, newWins: Int?): ResponseEntity<UserDTO> {
        val user = userRepository.findById(id)
        if (user != null) {
            if (newWins == null) {
                return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            }

            user.wins = newWins
            user.winPercentage = user.wins!!.toDouble() / (user.wins!! + user.losses!!)
            userRepository.save(user)
            return ResponseEntity(dtoConverter.convertToUserDTO(user), HttpStatus.OK)
        }
        return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
    }

    fun updateUserLosses(id: Long, newLosses: Int?): ResponseEntity<UserDTO> {
        val user = userRepository.findById(id)
        if (user != null) {
            if (newLosses == null) {
                return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            }

            user.losses = newLosses
            user.winPercentage = user.wins!!.toDouble() / (user.wins!! + user.losses!!)
            userRepository.save(user)
            return ResponseEntity(dtoConverter.convertToUserDTO(user), HttpStatus.OK)
        }
        return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
    }

    fun updateUserCoachName(id: Long, newCoachName: String?): ResponseEntity<UserDTO> {
        val user = userRepository.findById(id)
        if (user != null) {
            if (newCoachName.isNullOrEmpty()) {
                return ResponseEntity(emptyHeaders, HttpStatus.BAD_REQUEST)
            }

            user.coachName = newCoachName
            userRepository.save(user)
            return ResponseEntity(dtoConverter.convertToUserDTO(user), HttpStatus.OK)
        }
        return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
    }

    fun updateUserDiscordTag(id: Long, newDiscordTag: String): ResponseEntity<UserDTO> {
        val user = userRepository.findById(id)
        if (user != null) {
            user.discordTag = newDiscordTag
            userRepository.save(user)
            return ResponseEntity(dtoConverter.convertToUserDTO(user), HttpStatus.OK)
        }
        return ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
    }

    fun deleteUser(id: Long): ResponseEntity<HttpStatus> {
        val user = userRepository.findById(id)
        return if (user != null) {
            userRepository.deleteById(id.toString())
            ResponseEntity(HttpStatus.OK)
        } else {
            ResponseEntity(emptyHeaders, HttpStatus.NOT_FOUND)
        }
    }
}
