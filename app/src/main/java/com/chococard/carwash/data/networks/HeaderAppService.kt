package com.chococard.carwash.data.networks

import com.chococard.carwash.data.networks.request.JobRequest
import com.chococard.carwash.data.networks.request.LogsActive
import com.chococard.carwash.data.networks.request.SetLocation
import com.chococard.carwash.data.networks.request.SwitchSystem
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.HistoryResponse
import com.chococard.carwash.data.networks.response.JobResponse
import com.chococard.carwash.data.networks.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

//todo re-check method get or post
interface HeaderAppService {

    //get user info from data base keep to shared preferences.
    @GET("api/account/userinfo")
    suspend fun callFetchUser(): Response<UserResponse>

    //upload image from android to server when selected image.
    @Multipart
    @POST("api/changeprofile")
    suspend fun callUploadImageFile(@Part file: MultipartBody.Part): Response<ResponseBody>

    //logout
    @POST("api/account/logout/")
    suspend fun callLogout(): Response<BaseResponse>

    //change data profile name, id card, phone etc.
    @FormUrlEncoded
    @POST("v2/5e9eadde340000452b6eee61")
    suspend fun callChangeProfile(
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
    @POST("api/location")
    suspend fun callSetLocation(@Body setLocation: SetLocation): Response<BaseResponse>

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

    //set user logs active & inactive using application.
    @POST("api/account/userlogs")
    suspend fun callSetLogsActive(@Body logsActive: LogsActive): Response<BaseResponse>

    @POST("api/account/switchsystem")
    suspend fun callSwitchSystem(@Body switchSystem: SwitchSystem): Response<BaseResponse>

    companion object {
        operator fun invoke(networkHeaderInterceptor: NetworkHeaderInterceptor) =
            RetrofitClient.instant(networkHeaderInterceptor)
                .create(HeaderAppService::class.java)
    }

}
