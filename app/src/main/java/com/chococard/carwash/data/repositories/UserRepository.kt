package com.chococard.carwash.data.repositories

import com.chococard.carwash.data.networks.UserApi

class UserRepository(private val api: UserApi) : BaseRepository(api) {

    suspend fun fetchUser() = apiRequest { api.fetchUser() }

}
