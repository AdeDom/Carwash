package com.chococard.carwash.data.networks

import com.chococard.carwash.data.networks.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserApi : BaseApi {

    @GET("5e8ef2dc30000066bf64bf82")
    suspend fun fetchUser(): Response<UserResponse>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): UserApi {
            return RetrofitClient.instance(networkConnectionInterceptor)
                .create(UserApi::class.java)
        }
    }

}
