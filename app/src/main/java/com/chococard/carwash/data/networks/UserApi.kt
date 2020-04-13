package com.chococard.carwash.data.networks

import android.content.Context
import com.chococard.carwash.data.networks.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserApi : BaseApi {

    @GET("5e8ef2dc30000066bf64bf82")
    suspend fun fetchUser(): Response<UserResponse>

    companion object {
        operator fun invoke(
            context: Context
        ): UserApi {
            return RetrofitClient.instance(context)
                .create(UserApi::class.java)
        }
    }

}
