package com.fcfb.arceus.service.fcfb

import com.fcfb.arceus.converter.DTOConverter
import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Game.GameType
import com.fcfb.arceus.domain.User
import com.fcfb.arceus.domain.User.Role.USER
import com.fcfb.arceus.dto.UserDTO
import com.fcfb.arceus.repositories.UserRepository
import com.fcfb.arceus.service.discord.DiscordService
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val discordService: DiscordService,
    private val dtoConverter: DTOConverter,
) {
    /**
     * After a game ends, update the user's wins and losses
     * @param game
     */
    fun updateUserWinsAndLosses(game: Game) {
        val homeUser = getUserByTeam(game.homeTeam)
        val awayUser = getUserByTeam(game.awayTeam)

        if (game.homeScore > game.awayScore) {
            homeUser.wins += 1
            awayUser.losses += 1
            if (game.gameType == GameType.CONFERENCE_GAME) {
                homeUser.conferenceWins += 1
                awayUser.conferenceLosses += 1
            } else if (game.gameType == GameType.CONFERENCE_CHAMPIONSHIP) {
                homeUser.conferenceChampionshipWins += 1
                awayUser.conferenceChampionshipLosses += 1
            } else if (game.gameType == GameType.BOWL) {
                homeUser.bowlWins += 1
                awayUser.bowlLosses += 1
            } else if (game.gameType == GameType.PLAYOFFS) {
                homeUser.bowlWins += 1
                awayUser.bowlLosses += 1
                homeUser.playoffWins += 1
                awayUser.playoffLosses += 1
            } else if (game.gameType == GameType.NATIONAL_CHAMPIONSHIP) {
                homeUser.bowlWins += 1
                awayUser.bowlLosses += 1
                homeUser.playoffWins += 1
                awayUser.playoffLosses += 1
                homeUser.nationalChampionshipWins += 1
                awayUser.nationalChampionshipLosses += 1
            }
        } else {
            homeUser.losses += 1
            awayUser.wins += 1
            if (game.gameType == GameType.CONFERENCE_GAME) {
                homeUser.conferenceLosses += 1
                awayUser.conferenceWins += 1
            } else if (game.gameType == GameType.CONFERENCE_CHAMPIONSHIP) {
                homeUser.conferenceChampionshipLosses += 1
                awayUser.conferenceChampionshipWins += 1
            } else if (game.gameType == GameType.BOWL) {
                homeUser.bowlLosses += 1
                awayUser.bowlWins += 1
            } else if (game.gameType == GameType.PLAYOFFS) {
                homeUser.bowlLosses += 1
                awayUser.bowlWins += 1
                homeUser.playoffLosses += 1
                awayUser.playoffWins += 1
            } else if (game.gameType == GameType.NATIONAL_CHAMPIONSHIP) {
                homeUser.bowlLosses += 1
                awayUser.bowlWins += 1
                homeUser.playoffLosses += 1
                awayUser.playoffWins += 1
                homeUser.nationalChampionshipLosses += 1
                awayUser.nationalChampionshipWins += 1
            }
        }
        updateUser(homeUser)
        updateUser(awayUser)
    }

    /**
     * Create a new user
     * @param user
     */
    suspend fun createUser(user: User): User {
        val passwordEncoder = BCryptPasswordEncoder()
        val salt = passwordEncoder.encode(user.password)
        val verificationToken = UUID.randomUUID().toString()

        val discordUser = discordService.getUserByDiscordTag(user.discordTag)
        val discordId = discordUser.id.toString()

        val newUser =
            User(
                user.username,
                user.coachName,
                user.discordTag,
                discordId,
                user.email,
                passwordEncoder.encode(user.password),
                user.position,
                user.redditUsername,
                USER,
                salt,
                null,
                0,
                0,
                0,
                0.0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                user.offensivePlaybook,
                user.defensivePlaybook,
                0,
                verificationToken,
            )

        saveUser(newUser)
        return newUser
    }

    /**
     * Get a user DTO by its ID
     * @param id
     */
    private fun getUserDTOById(id: Long) = dtoConverter.convertToUserDTO(getUserById(id))

    /**
     * Get a user by its ID
     * @param id
     */
    fun getUserById(id: Long) = userRepository.getById(id)

    /**
     * Get a user by its Discord ID
     * @param discordId
     */
    fun getUserByDiscordId(discordId: String) = dtoConverter.convertToUserDTO(userRepository.getByDiscordId(discordId))

    /**
     * Get a user by its team
     * @param team
     */
    fun getUserByTeam(team: String) = dtoConverter.convertToUserDTO(userRepository.getByTeam(team))

    /**
     * Get a user by its username or email
     */
    fun getUserByUsernameOrEmail(usernameOrEmail: String) = userRepository.getByUsernameOrEmail(usernameOrEmail)

    /**
     * Get a user by its verification token
     * @param token
     */
    fun getByVerificationToken(token: String) = userRepository.getByVerificationToken(token)

    /**
     * Get all users
     * @return List<UserDTO>
     */
    fun getAllUsers(): List<UserDTO> {
        val userData = userRepository.findAll().filterNotNull()
        return userData.map { dtoConverter.convertToUserDTO(it) }
    }

    /**
     * Get a user by its name
     * @param name
     */
    fun getUserByName(name: String) = dtoConverter.convertToUserDTO(userRepository.getByCoachName(name))

    /**
     * Update a user's password
     * @param id
     * @param newPassword
     */
    fun updateUserPassword(
        id: Long,
        newPassword: String,
    ): UserDTO {
        val user = getUserById(id)

        val passwordEncoder = BCryptPasswordEncoder()
        user.password = passwordEncoder.encode(newPassword)
        user.salt = passwordEncoder.encode(newPassword)

        userRepository.save(user)
        return dtoConverter.convertToUserDTO(user)
    }

    /**
     * Approve a user
     * @param id
     * @return Boolean
     */
    fun approveUser(id: Long): Boolean {
        try {
            val user = getUserById(id)
            user.apply {
                approved = 1
            }
            saveUser(user)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * Update a user's email
     * @param id
     * @param email
     * @return Boolean
     */
    fun updateEmail(
        id: Long,
        email: String,
    ): UserDTO {
        val user = getUserById(id)
        user.apply {
            this.email = email
        }
        saveUser(user)
        return dtoConverter.convertToUserDTO(user)
    }

    /**
     * Update a user
     * @param id
     * @param user
     * @return UserDTO
     */
    fun updateUser(user: UserDTO): UserDTO {
        val existingUser = getUserDTOById(user.id)

        existingUser.apply {
            username = user.username
            coachName = user.coachName
            discordTag = user.discordTag
            discordId = user.discordId
            position = user.position
            redditUsername = user.redditUsername
            role = user.role
            team = user.team
            wins = user.wins
            losses = user.losses
            winPercentage = if (user.wins + user.losses > 0) user.wins.toDouble() / (user.wins + user.losses) else 0.0
            conferenceWins = user.conferenceWins
            conferenceLosses = user.conferenceLosses
            conferenceChampionshipWins = user.conferenceChampionshipWins
            conferenceChampionshipLosses = user.conferenceChampionshipLosses
            bowlWins = user.bowlWins
            bowlLosses = user.bowlLosses
            playoffWins = user.playoffWins
            playoffLosses = user.playoffLosses
            nationalChampionshipWins = user.nationalChampionshipWins
            nationalChampionshipLosses = user.nationalChampionshipLosses
            offensivePlaybook = user.offensivePlaybook
            defensivePlaybook = user.defensivePlaybook
        }

        return saveUserDTOToUser(user.id, existingUser)
    }

    /**
     * Save a user DTO into a User object in the db
     * @param id
     * @param user
     */
    private fun saveUserDTOToUser(
        id: Long,
        user: UserDTO,
    ): UserDTO {
        val existingUser = getUserById(id)

        existingUser.apply {
            username = user.username
            coachName = user.coachName
            discordTag = user.discordTag
            discordId = user.discordId
            position = user.position
            redditUsername = user.redditUsername
            role = user.role
            team = user.team
            wins = user.wins
            losses = user.losses
            winPercentage = user.winPercentage
            conferenceWins = user.conferenceWins
            conferenceLosses = user.conferenceLosses
            conferenceChampionshipWins = user.conferenceChampionshipWins
            conferenceChampionshipLosses = user.conferenceChampionshipLosses
            bowlWins = user.bowlWins
            bowlLosses = user.bowlLosses
            playoffWins = user.playoffWins
            playoffLosses = user.playoffLosses
            nationalChampionshipWins = user.nationalChampionshipWins
            nationalChampionshipLosses = user.nationalChampionshipLosses
            offensivePlaybook = user.offensivePlaybook
            defensivePlaybook = user.defensivePlaybook
        }

        userRepository.save(existingUser)
        return user
    }

    /**
     * Save a user
     * @param user
     */
    fun saveUser(user: User): User = userRepository.save(user)

    /**
     * Delete a user
     * @param id
     */
    fun deleteUser(id: Long): HttpStatus {
        userRepository.getById(id)

        userRepository.deleteById(id.toString())
        return HttpStatus.OK
    }
}
