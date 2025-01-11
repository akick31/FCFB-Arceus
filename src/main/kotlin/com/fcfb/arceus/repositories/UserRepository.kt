package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

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

    fun getByVerificationToken(token: String?): User
}
