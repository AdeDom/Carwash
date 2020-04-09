package com.chococard.carwash.data.networks

import com.chococard.carwash.data.networks.response.SignInResponse
import com.chococard.carwash.data.networks.response.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @FormUrlEncoded
    @POST("5e86ead831000092888140f3")
    suspend fun signUp(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("identityCard") identityCard: String,
        @Field("phone") phone: String
    ): Response<SignUpResponse>

    @FormUrlEncoded
    @POST("5e86ecb431000011d8814a43")
    suspend fun signIn(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<SignInResponse>

    @Multipart
    @POST("upload.php")
    suspend fun uploadImageFile(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<ResponseBody>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): AuthApi {
            return retrofitClient(networkConnectionInterceptor)
                .create(AuthApi::class.java)
        }
    }

}
