package com.chococard.carwash.data.repositories

import com.chococard.carwash.data.networks.HistoryApi

class HistoryRepository(private val api: HistoryApi) : BaseRepository(api) {

    suspend fun fetchHistory(dateBegin: String, dateEnd: String) =
        apiRequest { api.fetchHistory(dateBegin, dateEnd) }

}
