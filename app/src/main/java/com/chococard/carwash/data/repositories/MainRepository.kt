package com.chococard.carwash.data.repositories

import com.chococard.carwash.data.networks.MainApi

class MainRepository(private val api: MainApi) : BaseRepository(api) {

    suspend fun setLocation(latitude: Double, longitude: Double) =
        apiRequest { api.setLocation(latitude, longitude) }

    suspend fun fetchWallet(dateBegin: String, dateEnd: String) =
        apiRequest { api.fetchWallet(dateBegin, dateEnd) }

    suspend fun fetchHistory(dateBegin: String, dateEnd: String) =
        apiRequest { api.fetchHistory(dateBegin, dateEnd) }

    suspend fun jobRequest() = apiRequest { api.jobRequest() }

    suspend fun jobResponse(jobStatus: String) = apiRequest { api.jobResponse(jobStatus) }

    suspend fun payment(paymentStatus: String) = apiRequest { api.payment(paymentStatus) }

    suspend fun setActiveStatus(activityStatus: String) =
        apiRequest { api.setActiveStatus(activityStatus) }

}
