package com.chococard.carwash.data.networks

import android.content.Context
import com.chococard.carwash.data.networks.response.HistoryResponse
import com.chococard.carwash.data.networks.response.LocationResponse
import com.chococard.carwash.data.networks.response.UserResponse
import com.chococard.carwash.data.networks.response.WalletResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface MainApi : BaseApi {

    @GET("5e8ef2dc30000066bf64bf82")
    suspend fun fetchUser(): Response<UserResponse>

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

    companion object {
        operator fun invoke(context: Context): MainApi {
            return RetrofitClient.instance(context)
                .create(MainApi::class.java)
        }
    }

}
