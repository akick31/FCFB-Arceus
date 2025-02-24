package com.fcfb.arceus.service.fcfb.game

import com.fcfb.arceus.domain.Drive
import com.fcfb.arceus.domain.Play
import com.fcfb.arceus.repositories.DriveRepository
import com.fcfb.arceus.repositories.PlayRepository
import org.springframework.stereotype.Component

@Component
class DriveService(
    private val driveRepository: DriveRepository,
    private val playRepository: PlayRepository,
) {
    /**
     * Get a drive by its id
     * @param driveId
     * @return
     */
    fun getDriveById(driveId: Int): Drive = driveRepository.getDriveById(driveId)

    /**
     * Get all plays of a drive by its id
     * @param driveId
     * @return
     */
    fun getAllPlaysByDriveId(driveId: Int): List<Play> = playRepository.getAllPlaysByDriveId(driveId)

    /**
     * Get the previous drive of a game
     * @param gameId
     * @return
     */
    fun getPreviousDrive(gameId: Int): Drive = driveRepository.getPreviousDrive(gameId)

    /**
     * Get all plays of the previous drive of a game
     * @param gameId
     * @return
     */
    fun getAllPlaysOfPreviousDrive(gameId: Int): List<Play> {
        val drive = getPreviousDrive(gameId)
        return playRepository.getAllPlaysByDriveId(drive.driveId)
    }

    /**
     * Get the current drive of a game
     */
    fun getCurrentDrive(gameId: Int): Drive = driveRepository.getCurrentDrive(gameId)

    /**
     * Get all plays of the current drive of a game
     * @param gameId
     * @return
     */
    fun getAllPlaysOfCurrentDrive(gameId: Int): List<Play> {
        val drive = getCurrentDrive(gameId)
        return playRepository.getAllPlaysByDriveId(drive.driveId)
    }

    /**
     * Get all drives for a game
     */
    fun getAllDrivesByGameId(gameId: Int): List<Drive> = driveRepository.getAllDrivesByGameId(gameId)

    /**
     * Delete all drives for a game
     */
    fun deleteAllDrivesByGameId(gameId: Int) = driveRepository.deleteAllDrivesByGameId(gameId)
}
