package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.models.User

data class UserResponse(
    val success: Boolean = false,
    val message: String? = null,
    val user: User? = null
)
