package com.fcfb.arceus.domain

import com.fcfb.arceus.domain.Game.DefensivePlaybook
import com.fcfb.arceus.domain.Game.OffensivePlaybook
import com.fcfb.arceus.domain.Game.Subdivision
import com.fcfb.arceus.domain.Game.TVChannel
import java.util.Objects
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "game_stats", schema = "arceus")
class GameStats (
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
    @Column(name = "home_coach")
    var homeCoach: String? = null,

    @Basic
    @Column(name = "away_coach")
    var awayCoach: String? = null,

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
    @Column(name = "home_rush_3_yards_or_more")
    var homeRush3YardsOrMore: Int? = 0,

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
    @Column(name = "away_rush_3_yards_or_more")
    var awayRush3YardsOrMore: Int? = 0,

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
    @Column(name = "home_fumbles_lost")
    var homeFumblesLost: Int? = 0,

    @Basic
    @Column(name = "home_turnovers_lost")
    var homeTurnoversLost: Int? = 0,

    @Basic
    @Column(name = "home_turnover_touchdowns_lost")
    var homeTurnoverTouchdownsLost: Int? = 0,

    @Basic
    @Column(name = "away_interceptions_lost")
    var awayInterceptionsLost: Int? = 0,

    @Basic
    @Column(name = "away_home_fumbles_lost")
    var awayHomeFumblesLost: Int? = 0,

    @Basic
    @Column(name = "away_home_turnovers_lost")
    var awayHomeTurnoversLost: Int? = 0,

    @Basic
    @Column(name = "away_turnover_touchdowns_lost")
    var awayTurnoverTouchdownsLost: Int? = 0,

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
    @Column(name = "home_number_of_td_drives")
    var homeNumberOfTdDrives: Int? = 0,

    @Basic
    @Column(name = "home_td_drive_percentage")
    var homeTdDrivePercentage: Double? = 0.0,

    @Basic
    @Column(name = "home_number_of_fg_drives")
    var homeNumberOfFgDrives: Int? = 0,

    @Basic
    @Column(name = "home_fg_drive_percentage")
    var homeFgDrivePercentage: Double? = 0.0,

    @Basic
    @Column(name = "home_number_of_punt_drives")
    var homeNumberOfPuntDrives: Int? = 0,

    @Basic
    @Column(name = "home_punt_drive_percentage")
    var homePuntDrivePercentage: Double? = 0.0,

    @Basic
    @Column(name = "home_number_of_turnover_drives")
    var homeNumberOfTurnoverDrives: Int? = 0,

    @Basic
    @Column(name = "home_turnover_drive_percentage")
    var homeTurnoverDrivePercentage: Double? = 0.0,

    @Basic
    @Column(name = "away_number_of_drives")
    var awayNumberOfDrives: Int? = 0,

    @Basic
    @Column(name = "away_number_of_td_drives")
    var awayNumberOfTdDrives: Int? = 0,

    @Basic
    @Column(name = "away_td_drive_percentage")
    var awayTdDrivePercentage: Double? = 0.0,

    @Basic
    @Column(name = "away_number_of_fg_drives")
    var awayNumberOfFgDrives: Int? = 0,

    @Basic
    @Column(name = "away_fg_drive_percentage")
    var awayFgDrivePercentage: Double? = 0.0,

    @Basic
    @Column(name = "away_number_of_punt_drives")
    var awayNumberOfPuntDrives: Int? = 0,

    @Basic
    @Column(name = "away_punt_drive_percentage")
    var awayPuntDrivePercentage: Double? = 0.0,

    @Basic
    @Column(name = "away_number_of_turnover_drives")
    var awayNumberOfTurnoverDrives: Int? = 0,

    @Basic
    @Column(name = "away_turnover_drive_percentage")
    var awayTurnoverDrivePercentage: Double? = 0.0,

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
    @Column(name = "is_bowl")
    var isBowl: Boolean? = false,

    @Basic
    @Column(name = "is_playoffs")
    var isPlayoffs: Boolean? = false,

    @Basic
    @Column(name = "is_national_championship")
    var isNationalChampionship: Boolean? = false,

    @Basic
    @Column(name = "is_conference_game")
    var isConferenceGame: Boolean? = false,

    @Basic
    @Column(name = "is_division_game")
    var isDivisionGame: Boolean? = false,

    @Basic
    @Column(name = "scorebug")
    var scorebug: String? = null,

    @Basic
    @Column(name = "win_probability_plot")
    var winProbabilityPlot: String? = null,

    @Basic
    @Column(name = "score_plot_graph")
    var scorePlotGraph: String? = null,

    @Basic
    @Column(name = "stats_updated")
    var statsUpdated: Boolean? = false,

    @Basic
    @Column(name = "average_home_offensive_diff")
    var averageHomeOffensiveDiff: Double? = 0.0,

    @Basic
    @Column(name = "average_home_defensive_diff")
    var averageHomeDefensiveDiff: Double? = 0.0,

    @Basic
    @Column(name = "average_home_specialteams_diff")
    var averageHomeSpecialteamsDiff: Double? = 0.0,

    @Basic
    @Column(name = "average_away_offensive_diff")
    var averageAwayOffensiveDiff: Double? = 0.0,

    @Basic
    @Column(name = "average_away_defensive_diff")
    var averageAwayDefensiveDiff: Double? = 0.0,

    @Basic
    @Column(name = "average_away_specialteams_diff")
    var averageAwaySpecialteamsDiff: Double? = 0.0,

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
    @Column(name = "home_blocked_opponent_punt_td")
    var homeBlockedOpponentPuntTd: Int? = 0,

    @Basic
    @Column(name = "away_blocked_opponent_punt_td")
    var awayBlockedOpponentPuntTd: Int? = 0,

    @Basic
    @Column(name = "home_record")
    var homeRecord: String? = null,

    @Basic
    @Column(name = "away_record")
    var awayRecord: String? = null,

    @Basic
    @Column(name = "is_scrimmage")
    var isScrimmage: Boolean? = false,

    @Basic
    @Column(name = "is_final")
    var isFinal: Boolean? = false
) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as GameStats
        return gameId == that.gameId && homeTeam == that.homeTeam && awayTeam == that.awayTeam && homeTeamRank == that.homeTeamRank && awayTeamRank == that.awayTeamRank && startTime == that.startTime && location == that.location && tvChannel == that.tvChannel && homeCoach == that.homeCoach && awayCoach == that.awayCoach && homeOffensivePlaybook == that.homeOffensivePlaybook && awayOffensivePlaybook == that.awayOffensivePlaybook && homeDefensivePlaybook == that.homeDefensivePlaybook && awayDefensivePlaybook == that.awayDefensivePlaybook && season == that.season && week == that.week && subdivision == that.subdivision && homeScore == that.homeScore && awayScore == that.awayScore && homePassAttempts == that.homePassAttempts && homePassCompletions == that.homePassCompletions && homePassCompletionPercentage == that.homePassCompletionPercentage && homePassYards == that.homePassYards && awayPassAttempts == that.awayPassAttempts && awayPassCompletions == that.awayPassCompletions && awayPassCompletionPercentage == that.awayPassCompletionPercentage && awayPassYards == that.awayPassYards && homeRushAttempts == that.homeRushAttempts && homeRush3YardsOrMore == that.homeRush3YardsOrMore && homeRushSuccessPercentage == that.homeRushSuccessPercentage && homeRushYards == that.homeRushYards && awayRushAttempts == that.awayRushAttempts && awayRush3YardsOrMore == that.awayRush3YardsOrMore && awayRushSuccessPercentage == that.awayRushSuccessPercentage && awayRushYards == that.awayRushYards && homeTotalYards == that.homeTotalYards && awayTotalYards == that.awayTotalYards && homeInterceptionsLost == that.homeInterceptionsLost && homeFumblesLost == that.homeFumblesLost && homeTurnoversLost == that.homeTurnoversLost && homeTurnoverTouchdownsLost == that.homeTurnoverTouchdownsLost && awayInterceptionsLost == that.awayInterceptionsLost && awayHomeFumblesLost == that.awayHomeFumblesLost && awayHomeTurnoversLost == that.awayHomeTurnoversLost && awayTurnoverTouchdownsLost == that.awayTurnoverTouchdownsLost && homeFieldGoalMade == that.homeFieldGoalMade && homeFieldGoalAttempts == that.homeFieldGoalAttempts && homeFieldGoalPercentage == that.homeFieldGoalPercentage && homeLongestFieldGoal == that.homeLongestFieldGoal && homeBlockedOpponentFieldGoals == that.homeBlockedOpponentFieldGoals && homeFieldGoalTouchdown == that.homeFieldGoalTouchdown && awayFieldGoalMade == that.awayFieldGoalMade && awayFieldGoalAttempts == that.awayFieldGoalAttempts && awayFieldGoalPercentage == that.awayFieldGoalPercentage && awayLongestFieldGoal == that.awayLongestFieldGoal && awayBlockedOpponentFieldGoals == that.awayBlockedOpponentFieldGoals && awayFieldGoalTouchdown == that.awayFieldGoalTouchdown && homePuntsAttempted == that.homePuntsAttempted && homeLongestPunt == that.homeLongestPunt && homeAveragePuntLength == that.homeAveragePuntLength && homeBlockedOpponentPunt == that.homeBlockedOpponentPunt && homePuntReturnTd == that.homePuntReturnTd && homePuntReturnTdPercentage == that.homePuntReturnTdPercentage && awayPuntsAttempted == that.awayPuntsAttempted && awayLongestPunt == that.awayLongestPunt && awayAveragePuntLength == that.awayAveragePuntLength && awayBlockedOpponentPunt == that.awayBlockedOpponentPunt && awayPuntReturnTd == that.awayPuntReturnTd && awayPuntReturnTdPercentage == that.awayPuntReturnTdPercentage && homeNumberOfKickoffs == that.homeNumberOfKickoffs && homeOnsideAttempts == that.homeOnsideAttempts && homeOnsideSuccess == that.homeOnsideSuccess && homeOnsideSuccessPercentage == that.homeOnsideSuccessPercentage && homeNormalKickoffAttempts == that.homeNormalKickoffAttempts && homeTouchbacks == that.homeTouchbacks && homeTouchbackPercentage == that.homeTouchbackPercentage && homeKickReturnTd == that.homeKickReturnTd && homeKickReturnTdPercentage == that.homeKickReturnTdPercentage && awayNumberOfKickoffs == that.awayNumberOfKickoffs && awayOnsideAttempts == that.awayOnsideAttempts && awayOnsideSuccess == that.awayOnsideSuccess && awayOnsideSuccessPercentage == that.awayOnsideSuccessPercentage && awayNormalKickoffAttempts == that.awayNormalKickoffAttempts && awayTouchbacks == that.awayTouchbacks && awayTouchbackPercentage == that.awayTouchbackPercentage && awayKickReturnTd == that.awayKickReturnTd && awayKickReturnTdPercentage == that.awayKickReturnTdPercentage && homeNumberOfDrives == that.homeNumberOfDrives && homeNumberOfTdDrives == that.homeNumberOfTdDrives && homeTdDrivePercentage == that.homeTdDrivePercentage && homeNumberOfFgDrives == that.homeNumberOfFgDrives && homeFgDrivePercentage == that.homeFgDrivePercentage && homeNumberOfPuntDrives == that.homeNumberOfPuntDrives && homePuntDrivePercentage == that.homePuntDrivePercentage && homeNumberOfTurnoverDrives == that.homeNumberOfTurnoverDrives && homeTurnoverDrivePercentage == that.homeTurnoverDrivePercentage && awayNumberOfDrives == that.awayNumberOfDrives && awayNumberOfTdDrives == that.awayNumberOfTdDrives && awayTdDrivePercentage == that.awayTdDrivePercentage && awayNumberOfFgDrives == that.awayNumberOfFgDrives && awayFgDrivePercentage == that.awayFgDrivePercentage && awayNumberOfPuntDrives == that.awayNumberOfPuntDrives && awayPuntDrivePercentage == that.awayPuntDrivePercentage && awayNumberOfTurnoverDrives == that.awayNumberOfTurnoverDrives && awayTurnoverDrivePercentage == that.awayTurnoverDrivePercentage && homeTimeOfPossession == that.homeTimeOfPossession && awayTimeOfPossession == that.awayTimeOfPossession && q1HomeScore == that.q1HomeScore && q2HomeScore == that.q2HomeScore && q3HomeScore == that.q3HomeScore && q4HomeScore == that.q4HomeScore && otHomeScore == that.otHomeScore && q1AwayScore == that.q1AwayScore && q2AwayScore == that.q2AwayScore && q3AwayScore == that.q3AwayScore && q4AwayScore == that.q4AwayScore && otAwayScore == that.otAwayScore && homeTouchdowns == that.homeTouchdowns && awayTouchdowns == that.awayTouchdowns && isBowl == that.isBowl && isPlayoffs == that.isPlayoffs && isNationalChampionship == that.isNationalChampionship && isConferenceGame == that.isConferenceGame && isDivisionGame == that.isDivisionGame && scorebug == that.scorebug && winProbabilityPlot == that.winProbabilityPlot && scorePlotGraph == that.scorePlotGraph && isFinal == that.isFinal && statsUpdated == that.statsUpdated && averageHomeOffensiveDiff == that.averageHomeOffensiveDiff && averageHomeDefensiveDiff == that.averageHomeDefensiveDiff && averageHomeSpecialteamsDiff == that.averageHomeSpecialteamsDiff && averageAwayOffensiveDiff == that.averageAwayOffensiveDiff && averageAwayDefensiveDiff == that.averageAwayDefensiveDiff && averageAwaySpecialteamsDiff == that.averageAwaySpecialteamsDiff && homeAverageYardsPerPlay == that.homeAverageYardsPerPlay && awayAverageYardsPerPlay == that.awayAverageYardsPerPlay && homeThirdDownConversionSuccess == that.homeThirdDownConversionSuccess && homeThirdDownConversionAttempts == that.homeThirdDownConversionAttempts && homeThirdDownConversionPercentage == that.homeThirdDownConversionPercentage && homeFourthDownConversionSuccess == that.homeFourthDownConversionSuccess && homeFourthDownConversionAttempts == that.homeFourthDownConversionAttempts && homeFourthDownConversionPercentage == that.homeFourthDownConversionPercentage && awayThirdDownConversionSuccess == that.awayThirdDownConversionSuccess && awayThirdDownConversionAttempts == that.awayThirdDownConversionAttempts && awayThirdDownConversionPercentage == that.awayThirdDownConversionPercentage && awayFourthDownConversionSuccess == that.awayFourthDownConversionSuccess && awayFourthDownConversionAttempts == that.awayFourthDownConversionAttempts && awayFourthDownConversionPercentage == that.awayFourthDownConversionPercentage && homeLargestLead == that.homeLargestLead && awayLargestLead == that.awayLargestLead && homePassTouchdowns == that.homePassTouchdowns && homeRushTouchdowns == that.homeRushTouchdowns && awayPassTouchdowns == that.awayPassTouchdowns && awayRushTouchdowns == that.awayRushTouchdowns && homeBlockedOpponentPuntTd == that.homeBlockedOpponentPuntTd && awayBlockedOpponentPuntTd == that.awayBlockedOpponentPuntTd && homeRecord == that.homeRecord && awayRecord == that.awayRecord && isScrimmage == that.isScrimmage
    }

    override fun hashCode(): Int {
        return Objects.hash(
            gameId,
            homeTeam,
            awayTeam,
            homeTeamRank,
            awayTeamRank,
            startTime,
            location,
            tvChannel,
            homeCoach,
            awayCoach,
            homeOffensivePlaybook,
            awayOffensivePlaybook,
            homeDefensivePlaybook,
            awayDefensivePlaybook,
            season,
            week,
            subdivision,
            homeScore,
            awayScore,
            homePassAttempts,
            homePassCompletions,
            homePassCompletionPercentage,
            homePassYards,
            awayPassAttempts,
            awayPassCompletions,
            awayPassCompletionPercentage,
            awayPassYards,
            homeRushAttempts,
            homeRush3YardsOrMore,
            homeRushSuccessPercentage,
            homeRushYards,
            awayRushAttempts,
            awayRush3YardsOrMore,
            awayRushSuccessPercentage,
            awayRushYards,
            homeTotalYards,
            awayTotalYards,
            homeInterceptionsLost,
            homeFumblesLost,
            homeTurnoversLost,
            homeTurnoverTouchdownsLost,
            awayInterceptionsLost,
            awayHomeFumblesLost,
            awayHomeTurnoversLost,
            awayTurnoverTouchdownsLost,
            homeFieldGoalMade,
            homeFieldGoalAttempts,
            homeFieldGoalPercentage,
            homeLongestFieldGoal,
            homeBlockedOpponentFieldGoals,
            homeFieldGoalTouchdown,
            awayFieldGoalMade,
            awayFieldGoalAttempts,
            awayFieldGoalPercentage,
            awayLongestFieldGoal,
            awayBlockedOpponentFieldGoals,
            awayFieldGoalTouchdown,
            homePuntsAttempted,
            homeLongestPunt,
            homeAveragePuntLength,
            homeBlockedOpponentPunt,
            homePuntReturnTd,
            homePuntReturnTdPercentage,
            awayPuntsAttempted,
            awayLongestPunt,
            awayAveragePuntLength,
            awayBlockedOpponentPunt,
            awayPuntReturnTd,
            awayPuntReturnTdPercentage,
            homeNumberOfKickoffs,
            homeOnsideAttempts,
            homeOnsideSuccess,
            homeOnsideSuccessPercentage,
            homeNormalKickoffAttempts,
            homeTouchbacks,
            homeTouchbackPercentage,
            homeKickReturnTd,
            homeKickReturnTdPercentage,
            awayNumberOfKickoffs,
            awayOnsideAttempts,
            awayOnsideSuccess,
            awayOnsideSuccessPercentage,
            awayNormalKickoffAttempts,
            awayTouchbacks,
            awayTouchbackPercentage,
            awayKickReturnTd,
            awayKickReturnTdPercentage,
            homeNumberOfDrives,
            homeNumberOfTdDrives,
            homeTdDrivePercentage,
            homeNumberOfFgDrives,
            homeFgDrivePercentage,
            homeNumberOfPuntDrives,
            homePuntDrivePercentage,
            homeNumberOfTurnoverDrives,
            homeTurnoverDrivePercentage,
            awayNumberOfDrives,
            awayNumberOfTdDrives,
            awayTdDrivePercentage,
            awayNumberOfFgDrives,
            awayFgDrivePercentage,
            awayNumberOfPuntDrives,
            awayPuntDrivePercentage,
            awayNumberOfTurnoverDrives,
            awayTurnoverDrivePercentage,
            homeTimeOfPossession,
            awayTimeOfPossession,
            q1HomeScore,
            q2HomeScore,
            q3HomeScore,
            q4HomeScore,
            otHomeScore,
            q1AwayScore,
            q2AwayScore,
            q3AwayScore,
            q4AwayScore,
            otAwayScore,
            homeTouchdowns,
            awayTouchdowns,
            isBowl,
            isPlayoffs,
            isNationalChampionship,
            isConferenceGame,
            isDivisionGame,
            scorebug,
            winProbabilityPlot,
            scorePlotGraph,
            isFinal,
            statsUpdated,
            averageHomeOffensiveDiff,
            averageHomeDefensiveDiff,
            averageHomeSpecialteamsDiff,
            averageAwayOffensiveDiff,
            averageAwayDefensiveDiff,
            averageAwaySpecialteamsDiff,
            homeAverageYardsPerPlay,
            awayAverageYardsPerPlay,
            homeThirdDownConversionSuccess,
            homeThirdDownConversionAttempts,
            homeThirdDownConversionPercentage,
            homeFourthDownConversionSuccess,
            homeFourthDownConversionAttempts,
            homeFourthDownConversionPercentage,
            awayThirdDownConversionSuccess,
            awayThirdDownConversionAttempts,
            awayThirdDownConversionPercentage,
            awayFourthDownConversionSuccess,
            awayFourthDownConversionAttempts,
            awayFourthDownConversionPercentage,
            homeLargestLead,
            awayLargestLead,
            homePassTouchdowns,
            homeRushTouchdowns,
            awayPassTouchdowns,
            awayRushTouchdowns,
            homeBlockedOpponentPuntTd,
            awayBlockedOpponentPuntTd,
            homeRecord,
            awayRecord,
            isScrimmage
        )
    }
}
