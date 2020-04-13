package com.chococard.carwash.data.networks

import android.content.Context
import com.chococard.carwash.data.networks.response.HistoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HistoryApi : BaseApi {

    @GET("5e905085330000218b27d695")
    suspend fun fetchHistory(
        @Query("date_begin") dateBegin: String,
        @Query("date_end") dateEnd: String
    ): Response<HistoryResponse>

    companion object {
        operator fun invoke(
            context: Context
        ): HistoryApi {
            return RetrofitClient.instance(context)
                .create(HistoryApi::class.java)
        }
    }

}
