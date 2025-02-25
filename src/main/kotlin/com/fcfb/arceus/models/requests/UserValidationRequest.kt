package com.fcfb.arceus.models.requests

data class UserValidationRequest(
    var discordId: String,
    var discordTag: String,
    var username: String,
    var email: String,
)
