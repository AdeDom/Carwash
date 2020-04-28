package com.chococard.carwash.data.networks

import com.chococard.carwash.data.networks.request.JobRequest
import com.chococard.carwash.data.networks.response.*
import com.chococard.carwash.util.FlagConstant
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

//todo re-check method get or post
//TODO User, History, Job keep to room database
interface AppService {

    //TODO concern header & no header
    //get user info from data base keep to shared preferences.
    @GET("v2/5e9eb5eb3400002ab56eeec4")
    suspend fun callFetchUser(): Response<UserResponse>

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
//    @POST("api/account/Register/")
    suspend fun callSignUp(
        @Field(ApiConstant.FULL_NAME) name: String,
        @Field(ApiConstant.USERNAME) username: String,
        @Field(ApiConstant.PASSWORD) password: String,
        @Field(ApiConstant.ID_CARD_NUMBER) identityCard: String,
        @Field(ApiConstant.PHONE) phone: String,
        @Field(ApiConstant.ROLE) role: Int = FlagConstant.EMPLOYEE
    ): Response<BaseResponse>

    //login for want token from server.
    @FormUrlEncoded
    @POST("v2/5e9ebaab2d00006900cb767f")
    suspend fun callSignIn(
        @Field(ApiConstant.USERNAME) username: String,
        @Field(ApiConstant.PASSWORD) password: String
    ): Response<SignInResponse>

    //change data profile name, id card, phone etc.
    @FormUrlEncoded
    @POST("v2/5e9eadde340000452b6eee61")
    suspend fun callChangeProfile(
        @Field(ApiConstant.FULL_NAME) name: String,
        @Field(ApiConstant.ID_CARD_NUMBER) identityCard: String,
        @Field(ApiConstant.PHONE) phone: String
    ): Response<BaseResponse>

    //change password of username for security.
    @FormUrlEncoded
    @POST("v2/5e9eadfe34000083b56eee64")
    suspend fun callChangePassword(
        @Field(ApiConstant.OLD_PASSWORD) oldPassword: String,
        @Field(ApiConstant.NEW_PASSWORD) newPassword: String
    ): Response<BaseResponse>

    //set my location now to server and response location other employee around.
    @FormUrlEncoded
    @POST("v2/5e9eb321340000442b6eeea8")
    suspend fun callSetLocation(
        @Field(ApiConstant.LATITUDE) latitude: Double,
        @Field(ApiConstant.LONGITUDE) longitude: Double
    ): Response<LocationResponse>

    //get e-wallet amount wallet & wallet logs is list and filter by date begin & end.
    @FormUrlEncoded
    @POST("v2/5e9eb7662d00002700cb7652")
    suspend fun callFetchWallet(
        @Field(ApiConstant.DATE_BEGIN) dateBegin: String,
        @Field(ApiConstant.DATE_END) dateEnd: String
    ): Response<WalletResponse>

    //get old history ever service customer and filter by date begin & end.
    @FormUrlEncoded
    @POST("v2/5e9eb0e9340000b81a6eee8a")
    suspend fun callFetchHistory(
        @Field(ApiConstant.DATE_BEGIN) dateBegin: String,
        @Field(ApiConstant.DATE_END) dateEnd: String
    ): Response<HistoryResponse>

    //mock job request from server when customer call using application.
    @POST("v2/5e9eb8cf2d00007000cb7660")
    suspend fun callJobRequest(): Response<JobRequest>

    //answer job request from customer also send flag cancel job or confirm job to server.
    @FormUrlEncoded
    @POST("v2/5e9ebb042d00004b00cb7683")
    suspend fun callJobResponse(
        @Field(ApiConstant.JOB_STATUS) jobStatus: Int
    ): Response<JobResponse>

    //payment fee of application by send flag report or confirm to server.
    @FormUrlEncoded
    @POST("v2/5e9eae5e340000452b6eee67")
    suspend fun callPayment(
        @Field(ApiConstant.PAYMENT_STATUS) paymentStatus: Int
    ): Response<BaseResponse>

    //TODO re-check call api onPause
    //set state already send flag online or offline to server.
    @FormUrlEncoded
    @POST("v2/5e9eae80340000442b6eee68")
    suspend fun callSetActiveState(
        @Field(ApiConstant.ACTIVITY_STATE) activityState: Int
    ): Response<BaseResponse>

    //set user logs active & inactive using application.
    @FormUrlEncoded
    @POST("v2/5ea025ac320000700094ac16")
    suspend fun callSetLogsActive(
        @Field(ApiConstant.LOGS_STATUS) status: Int,
        @Field(ApiConstant.LOGS_KEYS) keys: String
    ): Response<BaseResponse>

    companion object {
        const val BASE_URL = "http://www.mocky.io/"
//        const val BASE_URL = "http://192.168.1.15/upload/"
//        const val BASE_URL = "https://sncarwash.azurewebsites.net/"

        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor): AppService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder().apply {
                baseUrl(BASE_URL)
                client(okHttpClient)
                addConverterFactory(GsonConverterFactory.create())
            }.build().create(AppService::class.java)
        }

        operator fun invoke(networkHeaderInterceptor: NetworkHeaderInterceptor): AppService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkHeaderInterceptor)
                .build()

            return Retrofit.Builder().apply {
                baseUrl(BASE_URL)
                client(okHttpClient)
                addConverterFactory(GsonConverterFactory.create())
            }.build().create(AppService::class.java)
        }
    }

}
