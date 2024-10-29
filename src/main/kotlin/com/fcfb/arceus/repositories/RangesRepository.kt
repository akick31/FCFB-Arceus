package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.Ranges
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RangesRepository : CrudRepository<Ranges?, Int?> {
    @Query(
        value =
            "SELECT * FROM ranges WHERE play_type = ? AND offensive_playbook = ? " +
                "AND defensive_playbook = ? AND ? BETWEEN lower_range AND upper_range;",
        nativeQuery = true,
    )
    fun getNormalResult(
        playType: String?,
        offensivePlaybook: String?,
        defensivePlaybook: String?,
        difference: String,
    ): Ranges?

    @Query(
        value = "SELECT * FROM ranges WHERE play_type = ? AND ? BETWEEN lower_range AND upper_range;",
        nativeQuery = true,
    )
    fun getNonNormalResult(
        playType: String?,
        difference: String,
    ): Ranges?

    @Query(
        value =
            "SELECT * FROM ranges WHERE play_type = ? AND ? BETWEEN ball_location_lower AND ball_location_upper " +
                "AND ? BETWEEN lower_range AND upper_range;",
        nativeQuery = true,
    )
    fun getPuntResult(
        playType: String?,
        ballLocation: String,
        difference: String,
    ): Ranges?

    @Query(
        value = "SELECT * FROM ranges WHERE play_type = ? AND distance = ? AND ? BETWEEN lower_range AND upper_range;",
        nativeQuery = true,
    )
    fun getFieldGoalResult(
        playType: String?,
        distance: String,
        difference: String,
    ): Ranges?
}
