package com.fcfb.arceus.models

enum class ExceptionType(val description: String) {
    INVALID_HOME_SCORE("The home score is invalid"),
    INVALID_AWAY_SCORE("The away score is invalid"),
    INVALID_QUARTER("The quarter is invalid"),
    INVALID_DOWN("The down is invalid"),
    INVALID_YARDS_TO_GO("The yards to go is invalid"),
    INVALID_PLAY_TIME("The play time is invalid"),
    INVALID_CLOCK("The clock is invalid"),
    INVALID_RESULT("The result is invalid"),
    INVALID_ACTUAL_RESULT("The actual result is invalid"),
    INVALID_BALL_LOCATION("The ball location is invalid"),
    HOME_USER_NOT_FOUND("The home user was not found"),
    AWAY_USER_NOT_FOUND("The away user was not found"),
    RESULT_NOT_FOUND("The result was not found in the ranges")
}

fun handleException(exceptionType: ExceptionType): Nothing {
    throw IllegalArgumentException(exceptionType.description)
}
