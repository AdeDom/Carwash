package com.chococard.carwash.data.networks

import android.content.Context
import com.chococard.carwash.data.networks.response.WalletResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MainApi : BaseApi {

    @FormUrlEncoded
    @POST("5e94072631000082005e2d04")
    suspend fun fetchWallet(
        @Field("date_begin") dateBegin: String,
        @Field("date_end") dateEnd: String
    ): Response<WalletResponse>

    companion object {
        operator fun invoke(
            context: Context
        ): MainApi {
            return RetrofitClient.instance(context)
                .create(MainApi::class.java)
        }
    }

}
