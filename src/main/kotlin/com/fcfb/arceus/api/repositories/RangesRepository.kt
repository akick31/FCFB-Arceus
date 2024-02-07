package com.fcfb.arceus.api.repositories

import com.fcfb.arceus.domain.RangesEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RangesRepository : CrudRepository<RangesEntity?, Int?> {
    @Query(
        value = "SELECT * FROM ranges WHERE play_type = ? AND offensive_playbook = ? " +
                "AND defensive_playbook = ? AND ? BETWEEN lower_range AND upper_range;", nativeQuery = true
    )
    fun findNormalResult(
        playType: String?,
        offensivePlaybook: String?,
        defensivePlaybook: String?,
        difference: Int
    ): RangesEntity?

    @Query(
        value = "SELECT * FROM ranges WHERE play_type = ? AND ? BETWEEN lower_range AND upper_range;",
        nativeQuery = true
    )
    fun findNonNormalResult(playType: String?,
                            difference: Int
    ): RangesEntity?
}