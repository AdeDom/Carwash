package com.chococard.carwash.data.networks

import com.chococard.carwash.data.networks.response.ChangePasswordResponse
import com.chococard.carwash.data.networks.response.ChangeProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ChangeApi {

    @Multipart
    @POST("upload.php")
    suspend fun uploadImageFile(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<ResponseBody>

    @FormUrlEncoded
    @POST("5e8eec303000007e0064bef9")
    suspend fun changeProfile(
        @Field("name") name: String,
        @Field("identityCard") identityCard: String,
        @Field("phone") phone: String
    ): Response<ChangeProfileResponse>

    @FormUrlEncoded
    @POST("5e8f01383000007e0064c01b")
    suspend fun changePassword(
        @Field("old_password") oldPassword: String,
        @Field("new_password") newPassword: String
    ): Response<ChangePasswordResponse>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): ChangeApi {
            return retrofitClient(networkConnectionInterceptor)
                .create(ChangeApi::class.java)
        }
    }

}
