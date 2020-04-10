package com.chococard.carwash.data.networks

import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.SignInResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi : BaseApi {

    @FormUrlEncoded
    @POST("5e901a07330000541827d4b2")
    suspend fun signUp(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("identityCard") identityCard: String,
        @Field("phone") phone: String
    ): Response<BaseResponse>

    @FormUrlEncoded
    @POST("5e9018b1330000af1727d4a8")
    suspend fun signIn(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<SignInResponse>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): AuthApi {
            return RetrofitClient.instance(networkConnectionInterceptor)
                .create(AuthApi::class.java)
        }
    }

}
