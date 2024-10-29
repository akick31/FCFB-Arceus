package com.fcfb.arceus.models

class UserUnauthorizedException : Exception("User is unauthorized") {
    override fun toString(): String {
        return "UserUnauthorizedException: ${super.message}"
    }
}

class UserNotFoundException : Exception("User not found") {
    override fun toString(): String {
        return "UserNotFoundException: ${super.message}"
    }
}

class DiscordUserNotFoundException : Exception("Discord user not found") {
    override fun toString(): String {
        return "DiscordUserNotFoundException: ${super.message}"
    }
}

class UnableToCreateGameThreadException : Exception("Unable to create game thread in Discord") {
    override fun toString(): String {
        return "UnableToCreateGameThreadException: ${super.message}"
    }
}

class NoTeamFoundException : Exception("No team found") {
    override fun toString(): String {
        return "NoTeamFoundException: ${super.message}"
    }
}

class ResultNotFoundException : Exception("Result not found") {
    override fun toString(): String {
        return "ResultNotFoundException: ${super.message}"
    }
}

class InvalidScenarioException : Exception("Invalid outcome scenario") {
    override fun toString(): String {
        return "InvalidScenarioException: ${super.message}"
    }
}

class InvalidActualResultException : Exception("Invalid actual result") {
    override fun toString(): String {
        return "InvalidActualResultException: ${super.message}"
    }
}

class InvalidPlayTypeException : Exception("Invalid play type") {
    override fun toString(): String {
        return "InvalidPlayTypeException: ${super.message}"
    }
}

class InvalidHalfTimePossessionChangeException : Exception("Invalid half time possession change") {
    override fun toString(): String {
        return "InvalidHalfTimePossessionChangeException: ${super.message}"
    }
}