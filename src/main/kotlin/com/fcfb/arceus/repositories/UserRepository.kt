package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun getById(id: Long): User?

    @Query("SELECT u FROM User u WHERE u.discordId = :discordId")
    fun getByDiscordId(discordId: String): User?

    fun getByCoachName(name: String): User?

    fun getByTeam(team: String): User?

    @Query("SELECT * FROM user WHERE team = :team", nativeQuery = true)
    fun getUsersByTeam(team: String): List<User>

    fun getEntityByTeam(team: String): User?

    @Query("SELECT * FROM user WHERE username = :username", nativeQuery = true)
    fun getByUsername(username: String): User?

    @Transactional
    @Modifying
    @Query("UPDATE user u SET u.average_response_time = :responseTime WHERE u.id = :id", nativeQuery = true)
    fun updateAverageResponseTime(
        id: Long,
        responseTime: Double,
    )

    @Query("SELECT * FROM user WHERE hashed_email = :email", nativeQuery = true)
    fun getUserByEmail(email: String): User?

    @Query("SELECT u FROM User u LEFT JOIN Team t ON u.team = t.name WHERE u.team IS NULL OR (t.isTaken = FALSE)")
    fun getFreeAgents(): List<User>

    fun existsByDiscordId(discordId: String): Boolean

    fun existsByDiscordTag(discordUsername: String): Boolean

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean

    fun findByUsername(username: String): User?

    fun findByEmail(email: String): User?

    fun findByDiscordId(discordId: String): User?

    fun findByTeam(team: String): User?

    fun findByPosition(position: User.CoachPosition): List<User>

    fun findByRole(role: User.Role): List<User>
}
