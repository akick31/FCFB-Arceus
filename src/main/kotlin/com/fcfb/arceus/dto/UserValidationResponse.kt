package com.fcfb.arceus.dto

data class UserValidationResponse(
    var discordIdExists: Boolean,
    var discordTagExists: Boolean,
    var usernameExists: Boolean,
    var emailExists: Boolean,
)
