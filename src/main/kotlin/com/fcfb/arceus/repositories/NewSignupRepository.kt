package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.NewSignup
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface NewSignupRepository : CrudRepository<NewSignup?, String?> {
    fun getById(id: Long?): NewSignup

    @Query("SELECT * FROM new_signup", nativeQuery = true)
    fun getNewSignups(): List<NewSignup>

    fun getByDiscordId(discordId: String?): NewSignup?

    fun getByVerificationToken(token: String?): NewSignup
}
