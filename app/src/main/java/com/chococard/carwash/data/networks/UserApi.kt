package com.chococard.carwash.data.networks

import com.chococard.carwash.data.networks.response.UserResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {

    @GET("5e8ecb913000007c0064bd9d")
    suspend fun fetchUser(): Response<UserResponse>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): UserApi {
            return retrofitClient(networkConnectionInterceptor)
                .create(UserApi::class.java)
        }
    }

}
