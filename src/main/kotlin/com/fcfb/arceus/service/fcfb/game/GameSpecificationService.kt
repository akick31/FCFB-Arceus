// GameSpecifications.kt
package com.fcfb.arceus.service

import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.repositories.GameRepository
import com.fcfb.arceus.service.fcfb.TeamService
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@Component
class GameSpecificationService(
    private val teamService: TeamService,
    private val gameRepository: GameRepository,
) {
    enum class GameFilter {
        RANKED_GAME,
        CONFERENCE_GAME,
        OUT_OF_CONFERENCE,
        CONFERENCE_CHAMPIONSHIP,
        BOWL,
        PLAYOFFS,
        NATIONAL_CHAMPIONSHIP,
        PREGAME,
        OPENING_KICKOFF,
        IN_PROGRESS,
        OVERTIME,
    }

    enum class GameCategory {
        ONGOING,
        PAST,
        SCRIMMAGE,
        PAST_SCRIMMAGE,
    }

    enum class GameSort {
        CLOSEST_TO_END,
        MOST_TIME_REMAINING,
    }

    /**
     * Create the spec for a game
     * @param filters
     * @param conference
     * @param season
     * @param week
     */
    fun createSpecification(
        filters: List<GameFilter>,
        category: GameCategory?,
        conference: String?,
        season: Int?,
        week: Int?,
    ): Specification<Game> {
        return Specification { root: Root<Game>, _: CriteriaQuery<*>, cb: CriteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            // Handle category filter
            category?.let {
                when (it) {
                    GameCategory.ONGOING -> {
                        predicates.add(cb.notEqual(root.get<Game.GameStatus>("gameStatus"), Game.GameStatus.FINAL))
                        predicates.add(cb.notEqual(root.get<Game.GameType>("gameType"), Game.GameType.SCRIMMAGE))
                    }
                    GameCategory.PAST -> {
                        predicates.add(cb.equal(root.get<Game.GameStatus>("gameStatus"), Game.GameStatus.FINAL))
                        predicates.add(cb.notEqual(root.get<Game.GameType>("gameType"), Game.GameType.SCRIMMAGE))
                    }
                    GameCategory.SCRIMMAGE -> {
                        predicates.add(cb.notEqual(root.get<Game.GameStatus>("gameStatus"), Game.GameStatus.FINAL))
                        predicates.add(cb.equal(root.get<Game.GameType>("gameType"), Game.GameType.SCRIMMAGE))
                    }
                    GameCategory.PAST_SCRIMMAGE -> {
                        predicates.add(cb.equal(root.get<Game.GameStatus>("gameStatus"), Game.GameStatus.FINAL))
                        predicates.add(cb.equal(root.get<Game.GameType>("gameType"), Game.GameType.SCRIMMAGE))
                    }
                }
            }

            // Handle other filters
            filters.forEach { filter ->
                when (filter) {
                    GameFilter.RANKED_GAME -> {
                        val rankedGames = gameRepository.getRankedGames().map { game -> game.gameId }
                        if (rankedGames.isNotEmpty()) {
                            predicates.add(root.get<Int>("gameId").`in`(rankedGames))
                        }
                    }
                    GameFilter.CONFERENCE_GAME -> {
                        predicates.add(cb.equal(root.get<Game.GameType>("gameType"), Game.GameType.CONFERENCE_GAME))
                    }
                    GameFilter.OUT_OF_CONFERENCE -> {
                        predicates.add(cb.equal(root.get<Game.GameType>("gameType"), Game.GameType.OUT_OF_CONFERENCE))
                    }
                    GameFilter.CONFERENCE_CHAMPIONSHIP -> {
                        predicates.add(cb.equal(root.get<Game.GameType>("gameType"), Game.GameType.CONFERENCE_CHAMPIONSHIP))
                    }
                    GameFilter.BOWL -> {
                        predicates.add(cb.equal(root.get<Game.GameType>("gameType"), Game.GameType.BOWL))
                    }
                    GameFilter.PLAYOFFS -> {
                        predicates.add(cb.equal(root.get<Game.GameType>("gameType"), Game.GameType.PLAYOFFS))
                    }
                    GameFilter.NATIONAL_CHAMPIONSHIP -> {
                        predicates.add(cb.equal(root.get<Game.GameType>("gameType"), Game.GameType.NATIONAL_CHAMPIONSHIP))
                    }
                    GameFilter.PREGAME -> {
                        predicates.add(cb.equal(root.get<Game.GameStatus>("gameStatus"), Game.GameStatus.PREGAME))
                    }
                    GameFilter.OPENING_KICKOFF -> {
                        predicates.add(cb.equal(root.get<Game.GameStatus>("gameStatus"), Game.GameStatus.OPENING_KICKOFF))
                    }
                    GameFilter.IN_PROGRESS -> {
                        predicates.add(cb.equal(root.get<Game.GameStatus>("gameStatus"), Game.GameStatus.IN_PROGRESS))
                    }
                    GameFilter.OVERTIME -> {
                        predicates.add(cb.equal(root.get<Game.GameStatus>("gameStatus"), Game.GameStatus.OVERTIME))
                    }
                }
            }

            // Conference filter
            conference?.let {
                val conferenceTeams = teamService.getTeamsInConference(it)?.map { team -> team.name }
                predicates.add(
                    cb.or(
                        root.get<String>("homeTeam").`in`(conferenceTeams),
                        root.get<String>("awayTeam").`in`(conferenceTeams),
                    ),
                )
            }

            // Season/week filters
            season?.let { predicates.add(cb.equal(root.get<Int>("season"), it)) }
            week?.let { predicates.add(cb.equal(root.get<Int>("week"), it)) }

            cb.and(*predicates.toTypedArray())
        }
    }

    /**
     * Sort games by how far they are from being done
     * @param sort
     */
    fun createSort(sort: GameSort): List<org.springframework.data.domain.Sort.Order> {
        return when (sort) {
            GameSort.CLOSEST_TO_END ->
                listOf(
                    org.springframework.data.domain.Sort.Order.desc("quarter"),
                    org.springframework.data.domain.Sort.Order.asc("clock"),
                )
            GameSort.MOST_TIME_REMAINING ->
                listOf(
                    org.springframework.data.domain.Sort.Order.asc("quarter"),
                    org.springframework.data.domain.Sort.Order.desc("clock"),
                )
        }
    }
}
