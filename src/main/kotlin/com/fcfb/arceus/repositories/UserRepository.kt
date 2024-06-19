package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User?, String?> {
    fun findById(id: Long?): User?

    fun findByCoachName(name: String?): User?

    fun findByTeam(team: String?): User?

    fun findEntityByTeam(team: String?): User?

    @Query("SELECT u FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    fun findByUsernameOrEmail(usernameOrEmail: String): User?

    fun findByVerificationToken(token: String?): User?
}
