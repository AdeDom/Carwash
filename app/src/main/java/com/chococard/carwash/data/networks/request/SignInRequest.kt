package com.chococard.carwash.data.networks.request

import com.chococard.carwash.util.FlagConstant

data class SignInRequest(
    val username: String,
    val password: String,
    val role: Int = FlagConstant.EMPLOYEE
)
