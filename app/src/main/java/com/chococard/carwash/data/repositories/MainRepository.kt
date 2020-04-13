package com.chococard.carwash.data.repositories

import com.chococard.carwash.data.networks.MainApi

class MainRepository(private val api: MainApi) : BaseRepository(api) {

    suspend fun fetchUser() = apiRequest { api.fetchUser() }

    suspend fun fetchWallet(dateBegin: String, dateEnd: String) =
        apiRequest { api.fetchWallet(dateBegin, dateEnd) }

    suspend fun fetchHistory(dateBegin: String, dateEnd: String) =
        apiRequest { api.fetchHistory(dateBegin, dateEnd) }

}
