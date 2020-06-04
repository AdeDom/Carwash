package com.chococard.carwash.data.networks.request

data class ChangePasswordRequest(
    val oldPassword: String? = null,
    val newPassword: String? = null
)
