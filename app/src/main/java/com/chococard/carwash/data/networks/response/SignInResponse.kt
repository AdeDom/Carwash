package com.chococard.carwash.data.networks.response

data class SignInResponse(
    val message: String? = null,
    val success: Boolean = false,
    val token: String? = null
)
