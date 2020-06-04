package com.chococard.carwash.data.networks.request

data class ChangePassword(
    val oldPassword: String? = null,
    val newPassword: String? = null
)
