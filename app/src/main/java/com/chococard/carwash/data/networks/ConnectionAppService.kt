package com.chococard.carwash.data.networks

import com.chococard.carwash.data.models.SignIn
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.SignInResponse
import com.chococard.carwash.util.FlagConstant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

//todo re-check method get or post
//TODO User, History, Job keep to room database
interface ConnectionAppService {

    //TODO concern header & no header
    //upload image from android to server when selected image.
    @Multipart
    @POST("upload.php")
    suspend fun callUploadImageFile(
        @Part file: MultipartBody.Part,
        @Part(ApiConstant.DESCRIPTION) description: RequestBody
    ): Response<ResponseBody>

    //register for entire to system car wash.
    @FormUrlEncoded
    @POST("v2/5e9eacad34000099b46eee54")
//    @POST("api/account/register/")
    suspend fun callSignUp(
        @Field(ApiConstant.FULL_NAME) fullName: String,
        @Field(ApiConstant.USERNAME) username: String,
        @Field(ApiConstant.PASSWORD) password: String,
        @Field(ApiConstant.ID_CARD_NUMBER) identityCard: String,
        @Field(ApiConstant.PHONE) phone: String,
        @Field(ApiConstant.ROLE) role: Int = FlagConstant.EMPLOYEE
    ): Response<BaseResponse>

    //login for want token from server.
    @POST("v2/5ebb9c0d36000026def7e7e2")
//    @POST("api/account/login/")
    suspend fun callSignIn(@Body signIn: SignIn): Response<SignInResponse>

    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor) =
            RetrofitClient.instant(networkConnectionInterceptor)
                .create(ConnectionAppService::class.java)
    }

}
