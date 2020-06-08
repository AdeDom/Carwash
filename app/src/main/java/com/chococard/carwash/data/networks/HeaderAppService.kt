package com.chococard.carwash.data.networks

import com.chococard.carwash.data.networks.request.*
import com.chococard.carwash.data.networks.response.*
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
    @POST("api/account/changephone")
    suspend fun callChangeProfile(@Body changePhone: ChangePhoneRequest): Response<BaseResponse>

    //change password of username for security.
    @POST("api/account/changepassword")
    suspend fun callChangePassword(@Body changePassword: ChangePasswordRequest): Response<BaseResponse>

    //set my location now to server and response location other employee around.
    @POST("api/location")
    suspend fun callSetLocation(@Body setLocation: SetLocationRequest): Response<BaseResponse>

    //get old history ever service customer and filter by date begin & end.
    @GET("api/history")
    suspend fun callFetchHistory(
        @Query(ApiConstant.DATE_BEGIN) dateBegin: Long,
        @Query(ApiConstant.DATE_END) dateEnd: Long
    ): Response<HistoryResponse>

    //mock job request from server when customer call using application.
    @POST("api/jobrequest")
    suspend fun callJobRequest(): Response<JobResponse>

    //answer job request from customer also send flag cancel job or confirm job to server.
    @POST("api/jobresponse")
    suspend fun callJobResponse(@Body jobAnswer: JobAnswerRequest): Response<JobResponse>

    //payment fee of application by send flag report or confirm to server.
    @POST("api/payment")
    suspend fun callPayment(): Response<BaseResponse>

    @POST("api/report")
    suspend fun callReport(@Body report: ReportRequest): Response<BaseResponse>

    //set user logs active & inactive using application.
    @POST("api/account/userlogs")
    suspend fun callSetLogsActive(@Body logsActive: LogsActiveRequest): Response<BaseResponse>

    @POST("api/account/switchsystem")
    suspend fun callSwitchSystem(@Body switchSystem: SwitchSystemRequest): Response<BaseResponse>

    @POST("api/navigation")
    suspend fun callSetNavigation(@Body setNavigation: SetNavigationRequest): Response<NavigationResponse>

    @POST("api/setjobstastusname")
    suspend fun callSetJobStatusName(): Response<BaseResponse>

    companion object {
        operator fun invoke(networkHeaderInterceptor: NetworkHeaderInterceptor) =
            RetrofitClient.instant(networkHeaderInterceptor)
                .create(HeaderAppService::class.java)
    }

}
