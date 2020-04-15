package com.chococard.carwash.data.networks

import com.chococard.carwash.data.networks.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface BaseApi {

    @GET("5e8ef2dc30000066bf64bf82")
    suspend fun fetchUser(): Response<UserResponse>

    @Multipart
    @POST("upload.php")
    suspend fun uploadImageFile(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<ResponseBody>

}
