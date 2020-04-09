package com.chococard.carwash.data.repositories

import com.chococard.carwash.data.networks.SafeApiRequest
import com.chococard.carwash.data.networks.UserApi

class UserRepository(private val api: UserApi) : SafeApiRequest() {

    suspend fun fetchUser() = apiRequest { api.fetchUser() }

}
