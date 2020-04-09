package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.models.User

data class UserResponse(
    val message: String? = null,
    val success: Boolean = false,
    val user: User? = null
)
