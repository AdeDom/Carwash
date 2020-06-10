package com.chococard.carwash.data.networks

import com.chococard.carwash.data.networks.request.SignInRequest
import com.chococard.carwash.data.networks.request.ValidatePhoneRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.SignInResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ConnectionAppService {

    //register for entire to system car wash.
    @Multipart
    @POST("api/account/register")
    suspend fun callSignUp(
        @Part(ApiConstant.USERNAME) username: RequestBody,
        @Part(ApiConstant.PASSWORD) password: RequestBody,
        @Part(ApiConstant.FULL_NAME) fullName: RequestBody,
        @Part(ApiConstant.ID_CARD_NUMBER) identityCard: RequestBody,
        @Part(ApiConstant.PHONE) phone: RequestBody,
        @Part(ApiConstant.ROLE) role: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<BaseResponse>

    //login for want token from server.
    @POST("api/account/login")
    suspend fun callSignIn(@Body signIn: SignInRequest): Response<SignInResponse>

    //verify phone before authentication firebase
    @POST("api/account/checkphone")
    suspend fun callValidatePhone(@Body validatePhone: ValidatePhoneRequest): Response<BaseResponse>

    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor) =
            RetrofitClient.instant(networkConnectionInterceptor)
                .create(ConnectionAppService::class.java)
    }

}
