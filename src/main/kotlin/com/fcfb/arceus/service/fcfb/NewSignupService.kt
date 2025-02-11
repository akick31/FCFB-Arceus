package com.fcfb.arceus.service.fcfb

import com.fcfb.arceus.converter.DTOConverter
import com.fcfb.arceus.domain.NewSignup
import com.fcfb.arceus.models.dto.NewSignupDTO
import com.fcfb.arceus.repositories.NewSignupRepository
import com.fcfb.arceus.utils.EncryptionUtils
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class NewSignupService(
    private val dtoConverter: DTOConverter,
    private val encryptionUtils: EncryptionUtils,
    private val newSignupRepository: NewSignupRepository,
) {
    /**
     * Create a new signup
     * @param newSignup
     */
    fun createNewSignup(newSignup: NewSignup): NewSignup {
        val passwordEncoder = BCryptPasswordEncoder()
        val salt = passwordEncoder.encode(newSignup.password)
        val verificationToken = UUID.randomUUID().toString()

        val newSignup =
            NewSignup(
                newSignup.username,
                newSignup.coachName,
                newSignup.discordTag,
                newSignup.discordId,
                encryptionUtils.encrypt(newSignup.email),
                passwordEncoder.encode(newSignup.password),
                newSignup.position,
                salt,
                newSignup.teamChoiceOne,
                newSignup.teamChoiceTwo,
                newSignup.teamChoiceThree,
                newSignup.offensivePlaybook,
                newSignup.defensivePlaybook,
                false,
                verificationToken,
            )

        saveNewSignup(newSignup)
        return newSignup
    }

    /**
     * Approve a new signup
     * @param newSignup
     * @return Boolean
     */
    fun approveNewSignup(newSignup: NewSignup): Boolean {
        try {
            newSignup.apply {
                approved = true
            }
            saveNewSignup(newSignup)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * Get a new signup by its id
     * @param id
     */
    fun getNewSignupById(id: Long) = newSignupRepository.getById(id)

    /**
     * Get a new signup by its verification token
     * @param token
     */
    fun getByVerificationToken(token: String) = newSignupRepository.getByVerificationToken(token)

    /**
     * Get all new signups
     */
    fun getNewSignups(): List<NewSignupDTO> {
        val userData = newSignupRepository.getNewSignups()
        return userData.map { dtoConverter.convertToNewSignupDTO(it) }
    }

    /**
     * Save a new signup
     * @param newSignup
     */
    fun saveNewSignup(newSignup: NewSignup) = newSignupRepository.save(newSignup)
}
