package com.chococard.carwash.data.repositories

import com.chococard.carwash.data.networks.MainApi

class MainRepository(private val api: MainApi) : BaseRepository(api) {

    suspend fun fetchWallet(dateBegin: String, dateEnd: String) =
        apiRequest { api.fetchWallet(dateBegin, dateEnd) }

}
