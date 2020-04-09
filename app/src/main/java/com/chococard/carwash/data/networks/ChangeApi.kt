package com.chococard.carwash.data.networks

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ChangeApi {

    @Multipart
    @POST("upload.php")
    suspend fun uploadImageFile(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<ResponseBody>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): ChangeApi {
            return retrofitClient(networkConnectionInterceptor)
                .create(ChangeApi::class.java)
        }
    }

}
