package com.chococard.carwash.data.networks.response

data class SignInResponse(
    val success: Boolean = false,
    val message: String? = null,
    val token: String? = null
)
