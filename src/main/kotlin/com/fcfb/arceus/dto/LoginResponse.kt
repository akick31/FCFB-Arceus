package com.fcfb.arceus.dto

import com.fcfb.arceus.enums.user.UserRole

data class LoginResponse(
    val token: String,
    val userId: Long,
    val role: UserRole,
)
