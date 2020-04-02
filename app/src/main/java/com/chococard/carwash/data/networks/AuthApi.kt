package com.chococard.carwash.data.networks

import com.chococard.carwash.data.networks.response.SignUpResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("5e858029300000c62997b072")
    suspend fun signUp(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("identityCard") identityCard: String,
        @Field("phone") phone: String,
        @Field("image") image: String
    ): Response<SignUpResponse>

    companion object {
        operator fun invoke(): AuthApi {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(AuthApi::class.java)
        }
    }

}