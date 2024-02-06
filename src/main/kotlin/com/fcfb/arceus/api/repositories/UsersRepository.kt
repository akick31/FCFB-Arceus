package com.fcfb.arceus.api.repositories

import com.fcfb.arceus.domain.UsersEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UsersRepository : CrudRepository<UsersEntity?, String?> {
    fun findByCoachName(name: String?): Optional<UsersEntity?>?
    fun findByTeam(team: String?): Optional<UsersEntity?>?
    fun findEntityByTeam(team: String?): UsersEntity?
}