package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.UsersEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UsersRepository : CrudRepository<UsersEntity?, String?> {
    fun findById(id: Long?): Optional<UsersEntity?>?
    fun findByCoachName(name: String?): Optional<UsersEntity?>?
    fun findByTeam(team: String?): Optional<UsersEntity?>?
    fun findEntityByTeam(team: String?): UsersEntity?
    @Query("SELECT u FROM UsersEntity u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    fun findByUsernameOrEmail(usernameOrEmail: String): Optional<UsersEntity?>?
    fun findByVerificationToken(token: String?): Optional<UsersEntity?>?

}