package com.chococard.carwash.data.networks

import android.content.Context
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.HistoryResponse
import com.chococard.carwash.data.networks.response.UserResponse
import com.chococard.carwash.data.networks.response.WalletResponse
import retrofit2.Response
import retrofit2.http.*

interface MainApi : BaseApi {

    @GET("5e8ef2dc30000066bf64bf82")
    suspend fun fetchUser(): Response<UserResponse>

    @FormUrlEncoded
    @POST("5e966d732f00006900025a1e")
    suspend fun setLocation(
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    ): Response<BaseResponse>

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
        operator fun invoke(
            context: Context
        ): MainApi {
            return RetrofitClient.instance(context)
                .create(MainApi::class.java)
        }
    }

}
