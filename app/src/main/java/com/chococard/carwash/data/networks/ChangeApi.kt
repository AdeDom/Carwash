package com.chococard.carwash.data.networks

import com.chococard.carwash.data.networks.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ChangeApi : BaseApi {

    @FormUrlEncoded
    @POST("5e8eec303000007e0064bef9")
    suspend fun changeProfile(
        @Field("name") name: String,
        @Field("identityCard") identityCard: String,
        @Field("phone") phone: String
    ): Response<BaseResponse>

    @FormUrlEncoded
    @POST("5e8f01383000007e0064c01b")
    suspend fun changePassword(
        @Field("old_password") oldPassword: String,
        @Field("new_password") newPassword: String
    ): Response<BaseResponse>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): ChangeApi {
            return RetrofitClient.instance(networkConnectionInterceptor)
                .create(ChangeApi::class.java)
        }
    }

}
