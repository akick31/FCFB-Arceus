package com.fcfb.arceus.models.website

import com.fcfb.arceus.domain.User

data class LoginResponse(
    val token: String,
    val userId: Long,
    val role: User.Role,
)
