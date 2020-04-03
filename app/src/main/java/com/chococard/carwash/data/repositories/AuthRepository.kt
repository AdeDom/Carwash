package com.chococard.carwash.data.repositories

import com.chococard.carwash.data.networks.AuthApi
import com.chococard.carwash.data.networks.SafeApiRequest

class AuthRepository(private val api: AuthApi) : SafeApiRequest() {

    suspend fun signUp(
        name: String,
        username: String,
        password: String,
        identityCard: String,
        phone: String
    ) = apiRequest { api.signUp(name, username, password, identityCard, phone) }

}