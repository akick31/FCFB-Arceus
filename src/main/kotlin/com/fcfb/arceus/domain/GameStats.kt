package com.fcfb.arceus.domain

import com.fcfb.arceus.domain.Game.DefensivePlaybook
import com.fcfb.arceus.domain.Game.GameType
import com.fcfb.arceus.domain.Game.OffensivePlaybook
import com.fcfb.arceus.domain.Game.Subdivision
import com.fcfb.arceus.domain.Game.TVChannel
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "game_stats", schema = "arceus")
class GameStats(
    @Id
    @Column(name = "game_id")
    var gameId: Int? = 0,
    @Basic
    @Column(name = "home_team")
    var homeTeam: String? = null,
    @Basic
    @Column(name = "away_team")
    var awayTeam: String? = null,
    @Basic
    @Column(name = "home_team_rank")
    var homeTeamRank: Int? = 0,
    @Basic
    @Column(name = "away_team_rank")
    var awayTeamRank: Int? = 0,
    @Basic
    @Column(name = "start_time")
    var startTime: String? = null,
    @Basic
    @Column(name = "location")
    var location: String? = null,
    @Basic
    @Column(name = "tv_channel")
    var tvChannel: TVChannel? = null,
    @Basic
    @Column(name = "home_coach1")
    var homeCoach1: String? = null,
    @Basic
    @Column(name = "home_coach2")
    var homeCoach2: String? = null,
    @Basic
    @Column(name = "away_coach1")
    var awayCoach1: String? = null,
    @Basic
    @Column(name = "away_coach2")
    var awayCoach2: String? = null,
    @Basic
    @Column(name = "home_offensive_playbook")
    var homeOffensivePlaybook: OffensivePlaybook? = null,
    @Basic
    @Column(name = "away_offensive_playbook")
    var awayOffensivePlaybook: OffensivePlaybook? = null,
    @Basic
    @Column(name = "home_defensive_playbook")
    var homeDefensivePlaybook: DefensivePlaybook? = null,
    @Basic
    @Column(name = "away_defensive_playbook")
    var awayDefensivePlaybook: DefensivePlaybook? = null,
    @Basic
    @Column(name = "season")
    var season: Int? = 0,
    @Basic
    @Column(name = "week")
    var week: Int? = 0,
    @Basic
    @Column(name = "subdivision")
    var subdivision: Subdivision? = null,
    @Basic
    @Column(name = "home_score")
    var homeScore: Int? = 0,
    @Basic
    @Column(name = "away_score")
    var awayScore: Int? = 0,
    @Basic
    @Column(name = "home_pass_attempts")
    var homePassAttempts: Int? = 0,
    @Basic
    @Column(name = "home_pass_completions")
    var homePassCompletions: Int? = 0,
    @Basic
    @Column(name = "home_pass_completion_percentage")
    var homePassCompletionPercentage: Double? = 0.0,
    @Basic
    @Column(name = "home_pass_yards")
    var homePassYards: Int? = 0,
    @Basic
    @Column(name = "away_pass_attempts")
    var awayPassAttempts: Int? = 0,
    @Basic
    @Column(name = "away_pass_completions")
    var awayPassCompletions: Int? = 0,
    @Basic
    @Column(name = "away_pass_completion_percentage")
    var awayPassCompletionPercentage: Double? = 0.0,
    @Basic
    @Column(name = "away_pass_yards")
    var awayPassYards: Int? = 0,
    @Basic
    @Column(name = "home_rush_attempts")
    var homeRushAttempts: Int? = 0,
    @Basic
    @Column(name = "home_rush_successes")
    var homeRushSuccesses: Int? = 0,
    @Basic
    @Column(name = "home_rush_success_percentage")
    var homeRushSuccessPercentage: Double? = 0.0,
    @Basic
    @Column(name = "home_rush_yards")
    var homeRushYards: Int? = 0,
    @Basic
    @Column(name = "away_rush_attempts")
    var awayRushAttempts: Int? = 0,
    @Basic
    @Column(name = "away_rush_successes")
    var awayRushSuccesses: Int? = 0,
    @Basic
    @Column(name = "away_rush_success_percentage")
    var awayRushSuccessPercentage: Double? = 0.0,
    @Basic
    @Column(name = "away_rush_yards")
    var awayRushYards: Int? = 0,
    @Basic
    @Column(name = "home_total_yards")
    var homeTotalYards: Int? = 0,
    @Basic
    @Column(name = "away_total_yards")
    var awayTotalYards: Int? = 0,
    @Basic
    @Column(name = "home_interceptions_lost")
    var homeInterceptionsLost: Int? = 0,
    @Basic
    @Column(name = "home_interceptions_forced")
    var homeInterceptionsForced: Int? = 0,
    @Basic
    @Column(name = "home_fumbles_lost")
    var homeFumblesLost: Int? = 0,
    @Basic
    @Column(name = "home_fumbles_forced")
    var homeFumblesForced: Int? = 0,
    @Basic
    @Column(name = "home_turnovers_lost")
    var homeTurnoversLost: Int? = 0,
    @Basic
    @Column(name = "home_turnovers_forced")
    var homeTurnoversForced: Int? = 0,
    @Basic
    @Column(name = "home_turnover_touchdowns_lost")
    var homeTurnoverTouchdownsLost: Int? = 0,
    @Basic
    @Column(name = "home_turnover_touchdowns_forced")
    var homeTurnoverTouchdownsForced: Int? = 0,
    @Basic
    @Column(name = "away_interceptions_lost")
    var awayInterceptionsLost: Int? = 0,
    @Basic
    @Column(name = "away_interceptions_forced")
    var awayInterceptionsForced: Int? = 0,
    @Basic
    @Column(name = "away_fumbles_lost")
    var awayFumblesLost: Int? = 0,
    @Basic
    @Column(name = "away_fumbles_forced")
    var awayFumblesForced: Int? = 0,
    @Basic
    @Column(name = "away_turnovers_lost")
    var awayTurnoversLost: Int? = 0,
    @Basic
    @Column(name = "away_turnovers_forced")
    var awayTurnoversForced: Int? = 0,
    @Basic
    @Column(name = "away_turnover_touchdowns_lost")
    var awayTurnoverTouchdownsLost: Int? = 0,
    @Basic
    @Column(name = "away_turnover_touchdowns_forced")
    var awayTurnoverTouchdownsForced: Int? = 0,
    @Basic
    @Column(name = "home_field_goal_made")
    var homeFieldGoalMade: Int? = 0,
    @Basic
    @Column(name = "home_field_goal_attempts")
    var homeFieldGoalAttempts: Int? = 0,
    @Basic
    @Column(name = "home_field_goal_percentage")
    var homeFieldGoalPercentage: Double? = 0.0,
    @Basic
    @Column(name = "home_longest_field_goal")
    var homeLongestFieldGoal: Int? = 0,
    @Basic
    @Column(name = "home_blocked_opponent_field_goals")
    var homeBlockedOpponentFieldGoals: Int? = 0,
    @Basic
    @Column(name = "home_field_goal_touchdown")
    var homeFieldGoalTouchdown: Int? = 0,
    @Basic
    @Column(name = "away_field_goal_made")
    var awayFieldGoalMade: Int? = 0,
    @Basic
    @Column(name = "away_field_goal_attempts")
    var awayFieldGoalAttempts: Int? = 0,
    @Basic
    @Column(name = "away_field_goal_percentage")
    var awayFieldGoalPercentage: Double? = 0.0,
    @Basic
    @Column(name = "away_longest_field_goal")
    var awayLongestFieldGoal: Int? = 0,
    @Basic
    @Column(name = "away_blocked_opponent_field_goals")
    var awayBlockedOpponentFieldGoals: Int? = 0,
    @Basic
    @Column(name = "away_field_goal_touchdown")
    var awayFieldGoalTouchdown: Int? = 0,
    @Basic
    @Column(name = "home_punts_attempted")
    var homePuntsAttempted: Int? = 0,
    @Basic
    @Column(name = "home_longest_punt")
    var homeLongestPunt: Int? = 0,
    @Basic
    @Column(name = "home_average_punt_length")
    var homeAveragePuntLength: Double? = 0.0,
    @Basic
    @Column(name = "home_blocked_opponent_punt")
    var homeBlockedOpponentPunt: Int? = 0,
    @Basic
    @Column(name = "home_punt_return_td")
    var homePuntReturnTd: Int? = 0,
    @Basic
    @Column(name = "home_punt_return_td_percentage")
    var homePuntReturnTdPercentage: Double? = 0.0,
    @Basic
    @Column(name = "away_punts_attempted")
    var awayPuntsAttempted: Int? = 0,
    @Basic
    @Column(name = "away_longest_punt")
    var awayLongestPunt: Int? = 0,
    @Basic
    @Column(name = "away_average_punt_length")
    var awayAveragePuntLength: Double? = 0.0,
    @Basic
    @Column(name = "away_blocked_opponent_punt")
    var awayBlockedOpponentPunt: Int? = 0,
    @Basic
    @Column(name = "away_punt_return_td")
    var awayPuntReturnTd: Int? = 0,
    @Basic
    @Column(name = "away_punt_return_td_percentage")
    var awayPuntReturnTdPercentage: Double? = 0.0,
    @Basic
    @Column(name = "home_number_of_kickoffs")
    var homeNumberOfKickoffs: Int? = 0,
    @Basic
    @Column(name = "home_onside_attempts")
    var homeOnsideAttempts: Int? = 0,
    @Basic
    @Column(name = "home_onside_success")
    var homeOnsideSuccess: Int? = 0,
    @Basic
    @Column(name = "home_onside_success_percentage")
    var homeOnsideSuccessPercentage: Double? = 0.0,
    @Basic
    @Column(name = "home_normal_kickoff_attempts")
    var homeNormalKickoffAttempts: Int? = 0,
    @Basic
    @Column(name = "home_touchbacks")
    var homeTouchbacks: Int? = 0,
    @Basic
    @Column(name = "home_touchback_percentage")
    var homeTouchbackPercentage: Double? = 0.0,
    @Basic
    @Column(name = "home_kick_return_td")
    var homeKickReturnTd: Int? = 0,
    @Basic
    @Column(name = "home_kick_return_td_percentage")
    var homeKickReturnTdPercentage: Double? = 0.0,
    @Basic
    @Column(name = "away_number_of_kickoffs")
    var awayNumberOfKickoffs: Int? = 0,
    @Basic
    @Column(name = "away_onside_attempts")
    var awayOnsideAttempts: Int? = 0,
    @Basic
    @Column(name = "away_onside_success")
    var awayOnsideSuccess: Int? = 0,
    @Basic
    @Column(name = "away_onside_success_percentage")
    var awayOnsideSuccessPercentage: Double? = 0.0,
    @Basic
    @Column(name = "away_normal_kickoff_attempts")
    var awayNormalKickoffAttempts: Int? = 0,
    @Basic
    @Column(name = "away_touchbacks")
    var awayTouchbacks: Int? = 0,
    @Basic
    @Column(name = "away_touchback_percentage")
    var awayTouchbackPercentage: Double? = 0.0,
    @Basic
    @Column(name = "away_kick_return_td")
    var awayKickReturnTd: Int? = 0,
    @Basic
    @Column(name = "away_kick_return_td_percentage")
    var awayKickReturnTdPercentage: Double? = 0.0,
    @Basic
    @Column(name = "home_number_of_drives")
    var homeNumberOfDrives: Int? = 0,
    @Basic
    @Column(name = "away_number_of_drives")
    var awayNumberOfDrives: Int? = 0,
    @Basic
    @Column(name = "home_time_of_possession")
    var homeTimeOfPossession: Int? = 0,
    @Basic
    @Column(name = "away_time_of_possession")
    var awayTimeOfPossession: Int? = 0,
    @Basic
    @Column(name = "q1_home_score")
    var q1HomeScore: Int? = 0,
    @Basic
    @Column(name = "q2_home_score")
    var q2HomeScore: Int? = 0,
    @Basic
    @Column(name = "q3_home_score")
    var q3HomeScore: Int? = 0,
    @Basic
    @Column(name = "q4_home_score")
    var q4HomeScore: Int? = 0,
    @Basic
    @Column(name = "ot_home_score")
    var otHomeScore: Int? = 0,
    @Basic
    @Column(name = "q1_away_score")
    var q1AwayScore: Int? = 0,
    @Basic
    @Column(name = "q2_away_score")
    var q2AwayScore: Int? = 0,
    @Basic
    @Column(name = "q3_away_score")
    var q3AwayScore: Int? = 0,
    @Basic
    @Column(name = "q4_away_score")
    var q4AwayScore: Int? = 0,
    @Basic
    @Column(name = "ot_away_score")
    var otAwayScore: Int? = 0,
    @Basic
    @Column(name = "home_touchdowns")
    var homeTouchdowns: Int? = 0,
    @Basic
    @Column(name = "away_touchdowns")
    var awayTouchdowns: Int? = 0,
    @Basic
    @Column(name = "average_home_offensive_diff")
    var averageHomeOffensiveDiff: Double? = 0.0,
    @Basic
    @Column(name = "average_home_defensive_diff")
    var averageHomeDefensiveDiff: Double? = 0.0,
    @Basic
    @Column(name = "average_home_offensive_special_teams_diff")
    var averageHomeOffensiveSpecialTeamsDiff: Double? = 0.0,
    @Basic
    @Column(name = "average_home_defensive_special_teams_diff")
    var averageHomeDefensiveSpecialTeamsDiff: Double? = 0.0,
    @Basic
    @Column(name = "average_away_offensive_diff")
    var averageAwayOffensiveDiff: Double? = 0.0,
    @Basic
    @Column(name = "average_away_defensive_diff")
    var averageAwayDefensiveDiff: Double? = 0.0,
    @Basic
    @Column(name = "average_away_offensive_special_teams_diff")
    var averageAwayOffensiveSpecialTeamsDiff: Double? = 0.0,
    @Basic
    @Column(name = "average_away_defensive_special_teams_diff")
    var averageAwayDefensiveSpecialTeamsDiff: Double? = 0.0,
    @Basic
    @Column(name = "home_average_yards_per_play")
    var homeAverageYardsPerPlay: Double? = 0.0,
    @Basic
    @Column(name = "away_average_yards_per_play")
    var awayAverageYardsPerPlay: Double? = 0.0,
    @Basic
    @Column(name = "home_third_down_conversion_success")
    var homeThirdDownConversionSuccess: Int? = 0,
    @Basic
    @Column(name = "home_third_down_conversion_attempts")
    var homeThirdDownConversionAttempts: Int? = 0,
    @Basic
    @Column(name = "home_third_down_conversion_percentage")
    var homeThirdDownConversionPercentage: Double? = 0.0,
    @Basic
    @Column(name = "home_fourth_down_conversion_success")
    var homeFourthDownConversionSuccess: Int? = 0,
    @Basic
    @Column(name = "home_fourth_down_conversion_attempts")
    var homeFourthDownConversionAttempts: Int? = 0,
    @Basic
    @Column(name = "home_fourth_down_conversion_percentage")
    var homeFourthDownConversionPercentage: Double? = 0.0,
    @Basic
    @Column(name = "away_third_down_conversion_success")
    var awayThirdDownConversionSuccess: Int? = 0,
    @Basic
    @Column(name = "away_third_down_conversion_attempts")
    var awayThirdDownConversionAttempts: Int? = 0,
    @Basic
    @Column(name = "away_third_down_conversion_percentage")
    var awayThirdDownConversionPercentage: Double? = 0.0,
    @Basic
    @Column(name = "away_fourth_down_conversion_success")
    var awayFourthDownConversionSuccess: Int? = 0,
    @Basic
    @Column(name = "away_fourth_down_conversion_attempts")
    var awayFourthDownConversionAttempts: Int? = 0,
    @Basic
    @Column(name = "away_fourth_down_conversion_percentage")
    var awayFourthDownConversionPercentage: Double? = 0.0,
    @Basic
    @Column(name = "home_largest_lead")
    var homeLargestLead: Int? = 0,
    @Basic
    @Column(name = "away_largest_lead")
    var awayLargestLead: Int? = 0,
    @Basic
    @Column(name = "home_largest_deficit")
    var homeLargestDeficit: Int? = 0,
    @Basic
    @Column(name = "away_largest_deficit")
    var awayLargestDeficit: Int? = 0,
    @Basic
    @Column(name = "home_pass_touchdowns")
    var homePassTouchdowns: Int? = 0,
    @Basic
    @Column(name = "home_rush_touchdowns")
    var homeRushTouchdowns: Int? = 0,
    @Basic
    @Column(name = "away_pass_touchdowns")
    var awayPassTouchdowns: Int? = 0,
    @Basic
    @Column(name = "away_rush_touchdowns")
    var awayRushTouchdowns: Int? = 0,
    @Basic
    @Column(name = "home_record")
    var homeRecord: String? = null,
    @Basic
    @Column(name = "away_record")
    var awayRecord: String? = null,
    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "game_type")
    var gameType: GameType? = GameType.SCRIMMAGE,
    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "game_status")
    var gameStatus: Game.GameStatus? = null,
    @Basic
    @Column(name = "home_red_zone_attempts")
    var homeRedZoneAttempts: Int? = 0,
    @Basic
    @Column(name = "home_red_zone_successes")
    var homeRedZoneSuccesses: Int? = 0,
    @Basic
    @Column(name = "home_red_zone_success_percentage")
    var homeRedZoneSuccessPercentage: Double? = 0.0,
    @Basic
    @Column(name = "away_red_zone_attempts")
    var awayRedZoneAttempts: Int? = 0,
    @Basic
    @Column(name = "away_red_zone_successes")
    var awayRedZoneSuccesses: Int? = 0,
    @Basic
    @Column(name = "away_red_zone_success_percentage")
    var awayRedZoneSuccessPercentage: Double? = 0.0,
    @Basic
    @Column(name = "average_diff")
    var averageDiff: Double? = 0.9,
    @Basic
    @Column(name = "home_turnover_differential")
    var homeTurnoverDifferential: Int? = 0,
    @Basic
    @Column(name = "away_turnover_differential")
    var awayTurnoverDifferential: Int? = 0,
    @Basic
    @Column(name = "home_pick_sixes_thrown")
    var homePickSixesThrown: Int? = 0,
    @Basic
    @Column(name = "home_pick_sixes_forced")
    var homePickSixesForced: Int? = 0,
    @Basic
    @Column(name = "away_pick_sixes_thrown")
    var awayPickSixesThrown: Int? = 0,
    @Basic
    @Column(name = "away_pick_sixes_forced")
    var awayPickSixesForced: Int? = 0,
    @Basic
    @Column(name = "home_fumble_return_tds_committed")
    var homeFumbleReturnTdsCommitted: Int? = 0,
    @Basic
    @Column(name = "home_fumble_return_tds_forced")
    var homeFumbleReturnTdsForced: Int? = 0,
    @Basic
    @Column(name = "away_fumble_return_tds_committed")
    var awayFumbleReturnTdsCommitted: Int? = 0,
    @Basic
    @Column(name = "away_fumble_return_tds_forced")
    var awayFumbleReturnTdsForced: Int? = 0,
    @Basic
    @Column(name = "home_safeties_forced")
    var homeSafetiesForced: Int? = 0,
    @Basic
    @Column(name = "home_safeties_committed")
    var homeSafetiesCommitted: Int? = 0,
    @Basic
    @Column(name = "away_safeties_forced")
    var awaySafetiesForced: Int? = 0,
    @Basic
    @Column(name = "away_safeties_committed")
    var awaySafetiesCommitted: Int? = 0,
)
