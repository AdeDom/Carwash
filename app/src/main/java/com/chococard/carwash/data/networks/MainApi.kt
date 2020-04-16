package com.chococard.carwash.data.networks

import android.content.Context
import com.chococard.carwash.data.networks.request.JobRequest
import com.chococard.carwash.data.networks.response.HistoryResponse
import com.chococard.carwash.data.networks.response.LocationResponse
import com.chococard.carwash.data.networks.response.WalletResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MainApi : BaseApi {

    @FormUrlEncoded
    @POST("5e96ae473000005100b6d655")
    suspend fun setLocation(
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    ): Response<LocationResponse>

    @FormUrlEncoded
    @POST("5e94072631000082005e2d04")
    suspend fun fetchWallet(
        @Field("date_begin") dateBegin: String,
        @Field("date_end") dateEnd: String
    ): Response<WalletResponse>

    @FormUrlEncoded
    @POST("5e905085330000218b27d695")
    suspend fun fetchHistory(
        @Field("date_begin") dateBegin: String,
        @Field("date_end") dateEnd: String
    ): Response<HistoryResponse>

    @POST("5e97ff1d3500001100c47d1b")
    suspend fun jobRequest(): Response<JobRequest>

    companion object {
        operator fun invoke(context: Context): MainApi {
            return RetrofitClient.instance(context)
                .create(MainApi::class.java)
        }
    }

}
