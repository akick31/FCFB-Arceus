package com.fcfb.arceus.domain

import java.util.*
import jakarta.persistence.*

@Entity
@Table(name = "game_stats", schema = "arceus")
class GameStatsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "game_id")
    var gameId: String? = null

    @Basic
    @Column(name = "home_team")
    var homeTeam: String? = null

    @Basic
    @Column(name = "away_team")
    var awayTeam: String? = null

    @Basic
    @Column(name = "home_team_rank")
    var homeTeamRank: Int? = null

    @Basic
    @Column(name = "away_team_rank")
    var awayTeamRank: Int? = null

    @Basic
    @Column(name = "start_time")
    var startTime: String? = null

    @Basic
    @Column(name = "location")
    var location: String? = null

    @Basic
    @Column(name = "tv_channel")
    var tvChannel: String? = null

    @Basic
    @Column(name = "home_coach")
    var homeCoach: String? = null

    @Basic
    @Column(name = "away_coach")
    var awayCoach: String? = null

    @Basic
    @Column(name = "home_offensive_playbook")
    var homeOffensivePlaybook: String? = null

    @Basic
    @Column(name = "away_offensive_playbook")
    var awayOffensivePlaybook: String? = null

    @Basic
    @Column(name = "home_defensive_playbook")
    var homeDefensivePlaybook: String? = null

    @Basic
    @Column(name = "away_defensive_playbook")
    var awayDefensivePlaybook: String? = null

    @Basic
    @Column(name = "season")
    var season: Int? = null

    @Basic
    @Column(name = "week")
    var week: Int? = null

    @Basic
    @Column(name = "subdivision")
    var subdivision: String? = null

    @Basic
    @Column(name = "home_score")
    var homeScore: Int? = null

    @Basic
    @Column(name = "away_score")
    var awayScore: Int? = null

    @Basic
    @Column(name = "home_pass_attempts")
    var homePassAttempts: Int? = null

    @Basic
    @Column(name = "home_pass_completions")
    var homePassCompletions: Int? = null

    @Basic
    @Column(name = "home_pass_completion_percentage")
    var homePassCompletionPercentage: Double? = null

    @Basic
    @Column(name = "home_pass_yards")
    var homePassYards: Int? = null

    @Basic
    @Column(name = "away_pass_attempts")
    var awayPassAttempts: Int? = null

    @Basic
    @Column(name = "away_pass_completions")
    var awayPassCompletions: Int? = null

    @Basic
    @Column(name = "away_pass_completion_percentage")
    var awayPassCompletionPercentage: Double? = null

    @Basic
    @Column(name = "away_pass_yards")
    var awayPassYards: Int? = null

    @Basic
    @Column(name = "home_rush_attempts")
    var homeRushAttempts: Int? = null

    @Basic
    @Column(name = "home_rush_3_yards_or_more")
    var homeRush3YardsOrMore: Int? = null

    @Basic
    @Column(name = "home_rush_success_percentage")
    var homeRushSuccessPercentage: Double? = null

    @Basic
    @Column(name = "home_rush_yards")
    var homeRushYards: Int? = null

    @Basic
    @Column(name = "away_rush_attempts")
    var awayRushAttempts: Int? = null

    @Basic
    @Column(name = "away_rush_3_yards_or_more")
    var awayRush3YardsOrMore: Int? = null

    @Basic
    @Column(name = "away_rush_success_percentage")
    var awayRushSuccessPercentage: Double? = null

    @Basic
    @Column(name = "away_rush_yards")
    var awayRushYards: Int? = null

    @Basic
    @Column(name = "home_total_yards")
    var homeTotalYards: Int? = null

    @Basic
    @Column(name = "away_total_yards")
    var awayTotalYards: Int? = null

    @Basic
    @Column(name = "home_interceptions_lost")
    var homeInterceptionsLost: Int? = null

    @Basic
    @Column(name = "home_fumbles_lost")
    var homeFumblesLost: Int? = null

    @Basic
    @Column(name = "home_turnovers_lost")
    var homeTurnoversLost: Int? = null

    @Basic
    @Column(name = "home_turnover_touchdowns_lost")
    var homeTurnoverTouchdownsLost: Int? = null

    @Basic
    @Column(name = "away_interceptions_lost")
    var awayInterceptionsLost: Int? = null

    @Basic
    @Column(name = "away_home_fumbles_lost")
    var awayHomeFumblesLost: Int? = null

    @Basic
    @Column(name = "away_home_turnovers_lost")
    var awayHomeTurnoversLost: Int? = null

    @Basic
    @Column(name = "away_turnover_touchdowns_lost")
    var awayTurnoverTouchdownsLost: Int? = null

    @Basic
    @Column(name = "home_field_goal_made")
    var homeFieldGoalMade: Int? = null

    @Basic
    @Column(name = "home_field_goal_attempts")
    var homeFieldGoalAttempts: Int? = null

    @Basic
    @Column(name = "home_field_goal_percentage")
    var homeFieldGoalPercentage: Double? = null

    @Basic
    @Column(name = "home_longest_field_goal")
    var homeLongestFieldGoal: Int? = null

    @Basic
    @Column(name = "home_blocked_opponent_field_goals")
    var homeBlockedOpponentFieldGoals: Int? = null

    @Basic
    @Column(name = "home_field_goal_touchdown")
    var homeFieldGoalTouchdown: Int? = null

    @Basic
    @Column(name = "away_field_goal_made")
    var awayFieldGoalMade: Int? = null

    @Basic
    @Column(name = "away_field_goal_attempts")
    var awayFieldGoalAttempts: Int? = null

    @Basic
    @Column(name = "away_field_goal_percentage")
    var awayFieldGoalPercentage: Double? = null

    @Basic
    @Column(name = "away_longest_field_goal")
    var awayLongestFieldGoal: Int? = null

    @Basic
    @Column(name = "away_blocked_opponent_field_goals")
    var awayBlockedOpponentFieldGoals: Int? = null

    @Basic
    @Column(name = "away_field_goal_touchdown")
    var awayFieldGoalTouchdown: Int? = null

    @Basic
    @Column(name = "home_punts_attempted")
    var homePuntsAttempted: Int? = null

    @Basic
    @Column(name = "home_longest_punt")
    var homeLongestPunt: Int? = null

    @Basic
    @Column(name = "home_average_punt_length")
    var homeAveragePuntLength: Double? = null

    @Basic
    @Column(name = "home_blocked_opponent_punt")
    var homeBlockedOpponentPunt: Int? = null

    @Basic
    @Column(name = "home_punt_return_td")
    var homePuntReturnTd: Int? = null

    @Basic
    @Column(name = "home_punt_return_td_percentage")
    var homePuntReturnTdPercentage: Double? = null

    @Basic
    @Column(name = "away_punts_attempted")
    var awayPuntsAttempted: Int? = null

    @Basic
    @Column(name = "away_longest_punt")
    var awayLongestPunt: Int? = null

    @Basic
    @Column(name = "away_average_punt_length")
    var awayAveragePuntLength: Double? = null

    @Basic
    @Column(name = "away_blocked_opponent_punt")
    var awayBlockedOpponentPunt: Int? = null

    @Basic
    @Column(name = "away_punt_return_td")
    var awayPuntReturnTd: Int? = null

    @Basic
    @Column(name = "away_punt_return_td_percentage")
    var awayPuntReturnTdPercentage: Double? = null

    @Basic
    @Column(name = "home_number_of_kickoffs")
    var homeNumberOfKickoffs: Int? = null

    @Basic
    @Column(name = "home_onside_attempts")
    var homeOnsideAttempts: Int? = null

    @Basic
    @Column(name = "home_onside_success")
    var homeOnsideSuccess: Int? = null

    @Basic
    @Column(name = "home_onside_success_percentage")
    var homeOnsideSuccessPercentage: Double? = null

    @Basic
    @Column(name = "home_normal_kickoff_attempts")
    var homeNormalKickoffAttempts: Int? = null

    @Basic
    @Column(name = "home_touchbacks")
    var homeTouchbacks: Int? = null

    @Basic
    @Column(name = "home_touchback_percentage")
    var homeTouchbackPercentage: Double? = null

    @Basic
    @Column(name = "home_kick_return_td")
    var homeKickReturnTd: Int? = null

    @Basic
    @Column(name = "home_kick_return_td_percentage")
    var homeKickReturnTdPercentage: Double? = null

    @Basic
    @Column(name = "away_number_of_kickoffs")
    var awayNumberOfKickoffs: Int? = null

    @Basic
    @Column(name = "away_onside_attempts")
    var awayOnsideAttempts: Int? = null

    @Basic
    @Column(name = "away_onside_success")
    var awayOnsideSuccess: Int? = null

    @Basic
    @Column(name = "away_onside_success_percentage")
    var awayOnsideSuccessPercentage: Double? = null

    @Basic
    @Column(name = "away_normal_kickoff_attempts")
    var awayNormalKickoffAttempts: Int? = null

    @Basic
    @Column(name = "away_touchbacks")
    var awayTouchbacks: Int? = null

    @Basic
    @Column(name = "away_touchback_percentage")
    var awayTouchbackPercentage: Double? = null

    @Basic
    @Column(name = "away_kick_return_td")
    var awayKickReturnTd: Int? = null

    @Basic
    @Column(name = "away_kick_return_td_percentage")
    var awayKickReturnTdPercentage: Double? = null

    @Basic
    @Column(name = "home_number_of_drives")
    var homeNumberOfDrives: Int? = null

    @Basic
    @Column(name = "home_number_of_td_drives")
    var homeNumberOfTdDrives: Int? = null

    @Basic
    @Column(name = "home_td_drive_percentage")
    var homeTdDrivePercentage: Double? = null

    @Basic
    @Column(name = "home_number_of_fg_drives")
    var homeNumberOfFgDrives: Int? = null

    @Basic
    @Column(name = "home_fg_drive_percentage")
    var homeFgDrivePercentage: Double? = null

    @Basic
    @Column(name = "home_number_of_punt_drives")
    var homeNumberOfPuntDrives: Int? = null

    @Basic
    @Column(name = "home_punt_drive_percentage")
    var homePuntDrivePercentage: Double? = null

    @Basic
    @Column(name = "home_number_of_turnover_drives")
    var homeNumberOfTurnoverDrives: Int? = null

    @Basic
    @Column(name = "home_turnover_drive_percentage")
    var homeTurnoverDrivePercentage: Double? = null

    @Basic
    @Column(name = "away_number_of_drives")
    var awayNumberOfDrives: Int? = null

    @Basic
    @Column(name = "away_number_of_td_drives")
    var awayNumberOfTdDrives: Int? = null

    @Basic
    @Column(name = "away_td_drive_percentage")
    var awayTdDrivePercentage: Double? = null

    @Basic
    @Column(name = "away_number_of_fg_drives")
    var awayNumberOfFgDrives: Int? = null

    @Basic
    @Column(name = "away_fg_drive_percentage")
    var awayFgDrivePercentage: Double? = null

    @Basic
    @Column(name = "away_number_of_punt_drives")
    var awayNumberOfPuntDrives: Int? = null

    @Basic
    @Column(name = "away_punt_drive_percentage")
    var awayPuntDrivePercentage: Double? = null

    @Basic
    @Column(name = "away_number_of_turnover_drives")
    var awayNumberOfTurnoverDrives: Int? = null

    @Basic
    @Column(name = "away_turnover_drive_percentage")
    var awayTurnoverDrivePercentage: Double? = null

    @Basic
    @Column(name = "home_time_of_possession")
    var homeTimeOfPossession: String? = null

    @Basic
    @Column(name = "away_time_of_possession")
    var awayTimeOfPossession: String? = null

    @Basic
    @Column(name = "q1_home_score")
    var q1HomeScore: Int? = null

    @Basic
    @Column(name = "q2_home_score")
    var q2HomeScore: Int? = null

    @Basic
    @Column(name = "q3_home_score")
    var q3HomeScore: Int? = null

    @Basic
    @Column(name = "q4_home_score")
    var q4HomeScore: Int? = null

    @Basic
    @Column(name = "ot_home_score")
    var otHomeScore: Int? = null

    @Basic
    @Column(name = "q1_away_score")
    var q1AwayScore: Int? = null

    @Basic
    @Column(name = "q2_away_score")
    var q2AwayScore: Int? = null

    @Basic
    @Column(name = "q3_away_score")
    var q3AwayScore: Int? = null

    @Basic
    @Column(name = "q4_away_score")
    var q4AwayScore: Int? = null

    @Basic
    @Column(name = "ot_away_score")
    var otAwayScore: Int? = null

    @Basic
    @Column(name = "home_touchdowns")
    var homeTouchdowns: Int? = null

    @Basic
    @Column(name = "away_touchdowns")
    var awayTouchdowns: Int? = null

    @Basic
    @Column(name = "is_bowl")
    var isBowl: Boolean? = null

    @Basic
    @Column(name = "is_playoffs")
    var isPlayoffs: Boolean? = null

    @Basic
    @Column(name = "is_national_championship")
    var isNationalChampionship: Boolean? = null

    @Basic
    @Column(name = "is_conference_game")
    var isConferenceGame: Boolean? = null

    @Basic
    @Column(name = "is_division_game")
    var isDivisionGame: Boolean? = null

    @Basic
    @Column(name = "scorebug")
    var scorebug: String? = null

    @Basic
    @Column(name = "win_probability_plot")
    var winProbabilityPlot: String? = null

    @Basic
    @Column(name = "score_plot_graph")
    var scorePlotGraph: String? = null

    @Basic
    @Column(name = "stats_updated")
    var statsUpdated: Boolean? = null

    @Basic
    @Column(name = "average_home_offensive_diff")
    var averageHomeOffensiveDiff: Double? = null

    @Basic
    @Column(name = "average_home_defensive_diff")
    var averageHomeDefensiveDiff: Double? = null

    @Basic
    @Column(name = "average_home_specialteams_diff")
    var averageHomeSpecialteamsDiff: Double? = null

    @Basic
    @Column(name = "average_away_offensive_diff")
    var averageAwayOffensiveDiff: Double? = null

    @Basic
    @Column(name = "average_away_defensive_diff")
    var averageAwayDefensiveDiff: Double? = null

    @Basic
    @Column(name = "average_away_specialteams_diff")
    var averageAwaySpecialteamsDiff: Double? = null

    @Basic
    @Column(name = "home_average_yards_per_play")
    var homeAverageYardsPerPlay: Double? = null

    @Basic
    @Column(name = "away_average_yards_per_play")
    var awayAverageYardsPerPlay: Double? = null

    @Basic
    @Column(name = "home_third_down_conversion_success")
    var homeThirdDownConversionSuccess: Int? = null

    @Basic
    @Column(name = "home_third_down_conversion_attempts")
    var homeThirdDownConversionAttempts: Int? = null

    @Basic
    @Column(name = "home_third_down_conversion_percentage")
    var homeThirdDownConversionPercentage: Double? = null

    @Basic
    @Column(name = "home_fourth_down_conversion_success")
    var homeFourthDownConversionSuccess: Int? = null

    @Basic
    @Column(name = "home_fourth_down_conversion_attempts")
    var homeFourthDownConversionAttempts: Int? = null

    @Basic
    @Column(name = "home_fourth_down_conversion_percentage")
    var homeFourthDownConversionPercentage: Double? = null

    @Basic
    @Column(name = "away_third_down_conversion_success")
    var awayThirdDownConversionSuccess: Int? = null

    @Basic
    @Column(name = "away_third_down_conversion_attempts")
    var awayThirdDownConversionAttempts: Int? = null

    @Basic
    @Column(name = "away_third_down_conversion_percentage")
    var awayThirdDownConversionPercentage: Double? = null

    @Basic
    @Column(name = "away_fourth_down_conversion_success")
    var awayFourthDownConversionSuccess: Int? = null

    @Basic
    @Column(name = "away_fourth_down_conversion_attempts")
    var awayFourthDownConversionAttempts: Int? = null

    @Basic
    @Column(name = "away_fourth_down_conversion_percentage")
    var awayFourthDownConversionPercentage: Double? = null

    @Basic
    @Column(name = "home_largest_lead")
    var homeLargestLead: Int? = null

    @Basic
    @Column(name = "away_largest_lead")
    var awayLargestLead: Int? = null

    @Basic
    @Column(name = "home_pass_touchdowns")
    var homePassTouchdowns: Int? = null

    @Basic
    @Column(name = "home_rush_touchdowns")
    var homeRushTouchdowns: Int? = null

    @Basic
    @Column(name = "away_pass_touchdowns")
    var awayPassTouchdowns: Int? = null

    @Basic
    @Column(name = "away_rush_touchdowns")
    var awayRushTouchdowns: Int? = null

    @Basic
    @Column(name = "home_blocked_opponent_punt_td")
    var homeBlockedOpponentPuntTd: Int? = null

    @Basic
    @Column(name = "away_blocked_opponent_punt_td")
    var awayBlockedOpponentPuntTd: Int? = null

    @Basic
    @Column(name = "home_record")
    var homeRecord: String? = null

    @Basic
    @Column(name = "away_record")
    var awayRecord: String? = null

    @Basic
    @Column(name = "is_scrimmage")
    var isScrimmage: Boolean? = null

    @Basic
    @Column(name = "is_final")
    var isFinal: Boolean? = null

    constructor(
        gameId: String?, homeTeam: String?, awayTeam: String?, homeTeamRank: Int?, awayTeamRank: Int?,
        startTime: String?, location: String?, tvChannel: String?, homeCoach: String?, awayCoach: String?,
        homeOffensivePlaybook: String?, awayOffensivePlaybook: String?, homeDefensivePlaybook: String?,
        awayDefensivePlaybook: String?, season: Int?, week: Int?, subdivision: String?,
        homeScore: Int?, awayScore: Int?, homePassAttempts: Int?, homePassCompletions: Int?,
        homePassCompletionPercentage: Double?, homePassYards: Int?,
        awayPassAttempts: Int?, awayPassCompletions: Int?, awayPassCompletionPercentage: Double?,
        awayPassYards: Int?, homeRushAttempts: Int?, homeRush3YardsOrMore: Int?,
        homeRushSuccessPercentage: Double?, homeRushYards: Int?, awayRushAttempts: Int?,
        awayRush3YardsOrMore: Int?, awayRushSuccessPercentage: Double?, awayRushYards: Int?,
        homeTotalYards: Int?, awayTotalYards: Int?, homeInterceptionsLost: Int?,
        homeFumblesLost: Int?, homeTurnoversLost: Int?, homeTurnoverTouchdownsLost: Int?,
        awayInterceptionsLost: Int?, awayHomeFumblesLost: Int?, awayHomeTurnoversLost: Int?,
        awayTurnoverTouchdownsLost: Int?, homeFieldGoalMade: Int?, homeFieldGoalAttempts: Int?,
        homeFieldGoalPercentage: Double?, homeLongestFieldGoal: Int?,
        homeBlockedOpponentFieldGoals: Int?, homeFieldGoalTouchdown: Int?,
        awayFieldGoalMade: Int?, awayFieldGoalAttempts: Int?, awayFieldGoalPercentage: Double?,
        awayLongestFieldGoal: Int?, awayBlockedOpponentFieldGoals: Int?,
        awayFieldGoalTouchdown: Int?, homePuntsAttempted: Int?, homeLongestPunt: Int?,
        homeAveragePuntLength: Double?, homeBlockedOpponentPunt: Int?, homePuntReturnTd: Int?,
        homePuntReturnTdPercentage: Double?, awayPuntsAttempted: Int?, awayLongestPunt: Int?,
        awayAveragePuntLength: Double?, awayBlockedOpponentPunt: Int?, awayPuntReturnTd: Int?,
        awayPuntReturnTdPercentage: Double?, homeNumberOfKickoffs: Int?, homeOnsideAttempts: Int?,
        homeOnsideSuccess: Int?, homeOnsideSuccessPercentage: Double?,
        homeNormalKickoffAttempts: Int?, homeTouchbacks: Int?, homeTouchbackPercentage: Double?,
        homeKickReturnTd: Int?, homeKickReturnTdPercentage: Double?, awayNumberOfKickoffs: Int?,
        awayOnsideAttempts: Int?, awayOnsideSuccess: Int?, awayOnsideSuccessPercentage: Double?,
        awayNormalKickoffAttempts: Int?, awayTouchbacks: Int?, awayTouchbackPercentage: Double?,
        awayKickReturnTd: Int?, awayKickReturnTdPercentage: Double?, homeNumberOfDrives: Int?,
        homeNumberOfTdDrives: Int?, homeTdDrivePercentage: Double?, homeNumberOfFgDrives: Int?,
        homeFgDrivePercentage: Double?, homeNumberOfPuntDrives: Int?, homePuntDrivePercentage: Double?,
        homeNumberOfTurnoverDrives: Int?, homeTurnoverDrivePercentage: Double?,
        awayNumberOfDrives: Int?, awayNumberOfTdDrives: Int?, awayTdDrivePercentage: Double?,
        awayNumberOfFgDrives: Int?, awayFgDrivePercentage: Double?, awayNumberOfPuntDrives: Int?,
        awayPuntDrivePercentage: Double?, awayNumberOfTurnoverDrives: Int?,
        awayTurnoverDrivePercentage: Double?, homeTimeOfPossession: String?, awayTimeOfPossession: String?,
        q1HomeScore: Int?, q2HomeScore: Int?, q3HomeScore: Int?, q4HomeScore: Int?,
        otHomeScore: Int?, q1AwayScore: Int?, q2AwayScore: Int?, q3AwayScore: Int?,
        q4AwayScore: Int?, otAwayScore: Int?, homeTouchdowns: Int?, awayTouchdowns: Int?,
        isBowl: Boolean?, isPlayoffs: Boolean?, isNationalChampionship: Boolean?, isConferenceGame: Boolean?,
        isDivisionGame: Boolean?, scorebug: String?, winProbabilityPlot: String?, scorePlotGraph: String?,
        statsUpdated: Boolean?, averageHomeOffensiveDiff: Double?,
        averageHomeDefensiveDiff: Double?, averageHomeSpecialteamsDiff: Double?,
        averageAwayOffensiveDiff: Double?, averageAwayDefensiveDiff: Double?,
        averageAwaySpecialteamsDiff: Double?, homeAverageYardsPerPlay: Double?,
        awayAverageYardsPerPlay: Double?, homeThirdDownConversionSuccess: Int?,
        homeThirdDownConversionAttempts: Int?, homeThirdDownConversionPercentage: Double?,
        homeFourthDownConversionSuccess: Int?, homeFourthDownConversionAttempts: Int?,
        homeFourthDownConversionPercentage: Double?, awayThirdDownConversionSuccess: Int?,
        awayThirdDownConversionAttempts: Int?, awayThirdDownConversionPercentage: Double?,
        awayFourthDownConversionSuccess: Int?, awayFourthDownConversionAttempts: Int?,
        awayFourthDownConversionPercentage: Double?, homeLargestLead: Int?, awayLargestLead: Int?,
        homePassTouchdowns: Int?, homeRushTouchdowns: Int?, awayPassTouchdowns: Int?,
        awayRushTouchdowns: Int?, homeBlockedOpponentPuntTd: Int?,
        awayBlockedOpponentPuntTd: Int?, homeRecord: String?, awayRecord: String?, isScrimmage: Boolean?,
        isFinal: Boolean?
    ) {
        this.gameId = gameId
        this.homeTeam = homeTeam
        this.awayTeam = awayTeam
        this.homeTeamRank = homeTeamRank
        this.awayTeamRank = awayTeamRank
        this.startTime = startTime
        this.location = location
        this.tvChannel = tvChannel
        this.homeCoach = homeCoach
        this.awayCoach = awayCoach
        this.homeOffensivePlaybook = homeOffensivePlaybook
        this.awayOffensivePlaybook = awayOffensivePlaybook
        this.homeDefensivePlaybook = homeDefensivePlaybook
        this.awayDefensivePlaybook = awayDefensivePlaybook
        this.season = season
        this.week = week
        this.subdivision = subdivision
        this.homeScore = homeScore
        this.awayScore = awayScore
        this.homePassAttempts = homePassAttempts
        this.homePassCompletions = homePassCompletions
        this.homePassCompletionPercentage = homePassCompletionPercentage
        this.homePassYards = homePassYards
        this.awayPassAttempts = awayPassAttempts
        this.awayPassCompletions = awayPassCompletions
        this.awayPassCompletionPercentage = awayPassCompletionPercentage
        this.awayPassYards = awayPassYards
        this.homeRushAttempts = homeRushAttempts
        this.homeRush3YardsOrMore = homeRush3YardsOrMore
        this.homeRushSuccessPercentage = homeRushSuccessPercentage
        this.homeRushYards = homeRushYards
        this.awayRushAttempts = awayRushAttempts
        this.awayRush3YardsOrMore = awayRush3YardsOrMore
        this.awayRushSuccessPercentage = awayRushSuccessPercentage
        this.awayRushYards = awayRushYards
        this.homeTotalYards = homeTotalYards
        this.awayTotalYards = awayTotalYards
        this.homeInterceptionsLost = homeInterceptionsLost
        this.homeFumblesLost = homeFumblesLost
        this.homeTurnoversLost = homeTurnoversLost
        this.homeTurnoverTouchdownsLost = homeTurnoverTouchdownsLost
        this.awayInterceptionsLost = awayInterceptionsLost
        this.awayHomeFumblesLost = awayHomeFumblesLost
        this.awayHomeTurnoversLost = awayHomeTurnoversLost
        this.awayTurnoverTouchdownsLost = awayTurnoverTouchdownsLost
        this.homeFieldGoalMade = homeFieldGoalMade
        this.homeFieldGoalAttempts = homeFieldGoalAttempts
        this.homeFieldGoalPercentage = homeFieldGoalPercentage
        this.homeLongestFieldGoal = homeLongestFieldGoal
        this.homeBlockedOpponentFieldGoals = homeBlockedOpponentFieldGoals
        this.homeFieldGoalTouchdown = homeFieldGoalTouchdown
        this.awayFieldGoalMade = awayFieldGoalMade
        this.awayFieldGoalAttempts = awayFieldGoalAttempts
        this.awayFieldGoalPercentage = awayFieldGoalPercentage
        this.awayLongestFieldGoal = awayLongestFieldGoal
        this.awayBlockedOpponentFieldGoals = awayBlockedOpponentFieldGoals
        this.awayFieldGoalTouchdown = awayFieldGoalTouchdown
        this.homePuntsAttempted = homePuntsAttempted
        this.homeLongestPunt = homeLongestPunt
        this.homeAveragePuntLength = homeAveragePuntLength
        this.homeBlockedOpponentPunt = homeBlockedOpponentPunt
        this.homePuntReturnTd = homePuntReturnTd
        this.homePuntReturnTdPercentage = homePuntReturnTdPercentage
        this.awayPuntsAttempted = awayPuntsAttempted
        this.awayLongestPunt = awayLongestPunt
        this.awayAveragePuntLength = awayAveragePuntLength
        this.awayBlockedOpponentPunt = awayBlockedOpponentPunt
        this.awayPuntReturnTd = awayPuntReturnTd
        this.awayPuntReturnTdPercentage = awayPuntReturnTdPercentage
        this.homeNumberOfKickoffs = homeNumberOfKickoffs
        this.homeOnsideAttempts = homeOnsideAttempts
        this.homeOnsideSuccess = homeOnsideSuccess
        this.homeOnsideSuccessPercentage = homeOnsideSuccessPercentage
        this.homeNormalKickoffAttempts = homeNormalKickoffAttempts
        this.homeTouchbacks = homeTouchbacks
        this.homeTouchbackPercentage = homeTouchbackPercentage
        this.homeKickReturnTd = homeKickReturnTd
        this.homeKickReturnTdPercentage = homeKickReturnTdPercentage
        this.awayNumberOfKickoffs = awayNumberOfKickoffs
        this.awayOnsideAttempts = awayOnsideAttempts
        this.awayOnsideSuccess = awayOnsideSuccess
        this.awayOnsideSuccessPercentage = awayOnsideSuccessPercentage
        this.awayNormalKickoffAttempts = awayNormalKickoffAttempts
        this.awayTouchbacks = awayTouchbacks
        this.awayTouchbackPercentage = awayTouchbackPercentage
        this.awayKickReturnTd = awayKickReturnTd
        this.awayKickReturnTdPercentage = awayKickReturnTdPercentage
        this.homeNumberOfDrives = homeNumberOfDrives
        this.homeNumberOfTdDrives = homeNumberOfTdDrives
        this.homeTdDrivePercentage = homeTdDrivePercentage
        this.homeNumberOfFgDrives = homeNumberOfFgDrives
        this.homeFgDrivePercentage = homeFgDrivePercentage
        this.homeNumberOfPuntDrives = homeNumberOfPuntDrives
        this.homePuntDrivePercentage = homePuntDrivePercentage
        this.homeNumberOfTurnoverDrives = homeNumberOfTurnoverDrives
        this.homeTurnoverDrivePercentage = homeTurnoverDrivePercentage
        this.awayNumberOfDrives = awayNumberOfDrives
        this.awayNumberOfTdDrives = awayNumberOfTdDrives
        this.awayTdDrivePercentage = awayTdDrivePercentage
        this.awayNumberOfFgDrives = awayNumberOfFgDrives
        this.awayFgDrivePercentage = awayFgDrivePercentage
        this.awayNumberOfPuntDrives = awayNumberOfPuntDrives
        this.awayPuntDrivePercentage = awayPuntDrivePercentage
        this.awayNumberOfTurnoverDrives = awayNumberOfTurnoverDrives
        this.awayTurnoverDrivePercentage = awayTurnoverDrivePercentage
        this.homeTimeOfPossession = homeTimeOfPossession
        this.awayTimeOfPossession = awayTimeOfPossession
        this.q1HomeScore = q1HomeScore
        this.q2HomeScore = q2HomeScore
        this.q3HomeScore = q3HomeScore
        this.q4HomeScore = q4HomeScore
        this.otHomeScore = otHomeScore
        this.q1AwayScore = q1AwayScore
        this.q2AwayScore = q2AwayScore
        this.q3AwayScore = q3AwayScore
        this.q4AwayScore = q4AwayScore
        this.otAwayScore = otAwayScore
        this.homeTouchdowns = homeTouchdowns
        this.awayTouchdowns = awayTouchdowns
        this.isBowl = isBowl
        this.isPlayoffs = isPlayoffs
        this.isNationalChampionship = isNationalChampionship
        this.isConferenceGame = isConferenceGame
        this.isDivisionGame = isDivisionGame
        this.scorebug = scorebug
        this.winProbabilityPlot = winProbabilityPlot
        this.scorePlotGraph = scorePlotGraph
        this.statsUpdated = statsUpdated
        this.averageHomeOffensiveDiff = averageHomeOffensiveDiff
        this.averageHomeDefensiveDiff = averageHomeDefensiveDiff
        this.averageHomeSpecialteamsDiff = averageHomeSpecialteamsDiff
        this.averageAwayOffensiveDiff = averageAwayOffensiveDiff
        this.averageAwayDefensiveDiff = averageAwayDefensiveDiff
        this.averageAwaySpecialteamsDiff = averageAwaySpecialteamsDiff
        this.homeAverageYardsPerPlay = homeAverageYardsPerPlay
        this.awayAverageYardsPerPlay = awayAverageYardsPerPlay
        this.homeThirdDownConversionSuccess = homeThirdDownConversionSuccess
        this.homeThirdDownConversionAttempts = homeThirdDownConversionAttempts
        this.homeThirdDownConversionPercentage = homeThirdDownConversionPercentage
        this.homeFourthDownConversionSuccess = homeFourthDownConversionSuccess
        this.homeFourthDownConversionAttempts = homeFourthDownConversionAttempts
        this.homeFourthDownConversionPercentage = homeFourthDownConversionPercentage
        this.awayThirdDownConversionSuccess = awayThirdDownConversionSuccess
        this.awayThirdDownConversionAttempts = awayThirdDownConversionAttempts
        this.awayThirdDownConversionPercentage = awayThirdDownConversionPercentage
        this.awayFourthDownConversionSuccess = awayFourthDownConversionSuccess
        this.awayFourthDownConversionAttempts = awayFourthDownConversionAttempts
        this.awayFourthDownConversionPercentage = awayFourthDownConversionPercentage
        this.homeLargestLead = homeLargestLead
        this.awayLargestLead = awayLargestLead
        this.homePassTouchdowns = homePassTouchdowns
        this.homeRushTouchdowns = homeRushTouchdowns
        this.awayPassTouchdowns = awayPassTouchdowns
        this.awayRushTouchdowns = awayRushTouchdowns
        this.homeBlockedOpponentPuntTd = homeBlockedOpponentPuntTd
        this.awayBlockedOpponentPuntTd = awayBlockedOpponentPuntTd
        this.homeRecord = homeRecord
        this.awayRecord = awayRecord
        this.isScrimmage = isScrimmage
        this.isFinal = isFinal
    }

    constructor()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as GameStatsEntity
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
