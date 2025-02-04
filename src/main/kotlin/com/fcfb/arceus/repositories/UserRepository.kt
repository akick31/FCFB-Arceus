package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.User
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface UserRepository : CrudRepository<User?, String?> {
    fun getById(id: Long?): User

    @Query("SELECT u FROM User u WHERE u.discordId = :discordId")
    fun getByDiscordId(discordId: String?): User

    fun getByCoachName(name: String?): User

    fun getByTeam(team: String?): User

    @Query("SELECT * FROM user WHERE team = :team", nativeQuery = true)
    fun getUsersByTeam(team: String?): List<User>

    fun getEntityByTeam(team: String?): User

    @Query("SELECT * FROM user WHERE team IS NULL", nativeQuery = true)
    fun getNewSignups(): List<User>

    @Query("SELECT * FROM user WHERE username = :usernameOrEmail OR email = :usernameOrEmail", nativeQuery = true)
    fun getByUsernameOrEmail(usernameOrEmail: String): User

    @Transactional
    @Modifying
    @Query("UPDATE user u SET u.average_response_time = :responseTime WHERE u.id = :id", nativeQuery = true)
    fun updateAverageResponseTime(
        id: Long?,
        responseTime: Double,
    )

    fun getByVerificationToken(token: String?): User
}
