package com.chococard.carwash.data.repositories

import com.chococard.carwash.data.networks.ChangeApi

class ChangeRepository(private val api: ChangeApi) : BaseRepository(api) {

    suspend fun changeProfile(name: String, identityCard: String, phone: String) =
        apiRequest { api.changeProfile(name, identityCard, phone) }

    suspend fun changePassword(oldPassword: String, newPassword: String) =
        apiRequest { api.changePassword(oldPassword, newPassword) }

}
