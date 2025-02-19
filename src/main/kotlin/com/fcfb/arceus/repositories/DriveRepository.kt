package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.domain.Drive
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface DriveRepository : CrudRepository<Play?, Int?> {
    @Query(value = "SELECT * FROM drive WHERE drive_id =?", nativeQuery = true)
    fun getDriveById(driveId: Int): Drive

    @Query(value = "SELECT * FROM drive WHERE game_id = ? ORDER BY drive_id DESC", nativeQuery = true)
    fun getAllDrivesByGameId(gameId: Int): List<Drive>

    @Query(value = "SELECT * FROM drive WHERE game_id = ? ORDER BY drive_id DESC LIMIT 1", nativeQuery = true)
    fun getCurrentDrive(gameId: Int): Drive

    @Query(value = "SELECT * FROM drive WHERE game_id = ? AND drive_finished = true ORDER BY drive_id DESC LIMIT 1", nativeQuery = true)
    fun getPreviousDrive(gameId: Int): Drive

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM drive WHERE game_id =?", nativeQuery = true)
    fun deleteAllDrivesByGameId(gameId: Int)
}
