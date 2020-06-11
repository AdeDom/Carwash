package com.chococard.carwash.data.networks

import com.chococard.carwash.data.networks.request.*
import com.chococard.carwash.data.networks.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface HeaderAppService {

    //get user info from data base keep to shared preferences.
    @GET("api/account/userinfo")
    suspend fun callFetchUserInfo(): Response<UserResponse>

    //upload image from android to server when selected image.
    @Multipart
    @POST("api/account/changeprofile")
    suspend fun callUploadImageFile(@Part file: MultipartBody.Part): Response<ResponseBody>

    //logout
    @POST("api/account/logout")
    suspend fun callLogout(): Response<BaseResponse>

    //change data profile name, id card, phone etc.
    @POST("api/account/changephone")
    suspend fun callChangeProfile(@Body changePhone: ChangePhoneRequest): Response<BaseResponse>

    //change password of username for security.
    @POST("api/account/changepassword")
    suspend fun callChangePassword(@Body changePassword: ChangePasswordRequest): Response<BaseResponse>

    //set my location now to server and response location other employee around.
    @POST("api/account/location")
    suspend fun callSetLocation(@Body setLocation: SetLocationRequest): Response<BaseResponse>

    //set user logs active & inactive using application.
    @POST("api/account/userlogs")
    suspend fun callSetLogsActive(@Body logsActive: LogsActiveRequest): Response<BaseResponse>

    //switch on-off (receive & reject) of job
    @POST("api/account/switchsystem")
    suspend fun callSwitchSystem(@Body switchSystem: SwitchSystemRequest): Response<BaseResponse>

    //get old history ever service customer and filter by date begin & end.
    @GET("api/job/history")
    suspend fun callFetchHistory(
        @Query(ApiConstant.DATE_BEGIN) dateBegin: Long,
        @Query(ApiConstant.DATE_END) dateEnd: Long
    ): Response<HistoryResponse>

    //mock job request from server when customer call using application.
    @POST("api/job/jobrequest")
    suspend fun callJobRequest(): Response<JobResponse>

    //answer job request from customer also send flag cancel job or confirm job to server.
    @POST("api/job/jobresponse")
    suspend fun callJobResponse(@Body jobAnswer: JobAnswerRequest): Response<JobResponse>

    //payment fee of application by send flag report or confirm to server.
    @POST("api/job/paymentjob")
    suspend fun callPayment(): Response<BaseResponse>

    //report job service when have problem
    @POST("api/job/reportjob")
    suspend fun callReport(@Body report: ReportRequest): Response<BaseResponse>

    //send location employee and response location customer
    @POST("api/job/navigation")
    suspend fun callSetNavigation(@Body setNavigation: SetNavigationRequest): Response<NavigationResponse>

    //set status name of table job
    @POST("api/job/statusservice")
    suspend fun callSetJobStatusName(): Response<BaseResponse>

    //fetch data from server to set widget score or ratings
    @GET("api/job/homescore")
    suspend fun callHomeScore(): Response<HomeScoreResponse>

    //upload image service job 4 side and other image
    @Multipart
    @POST("api/job/uploadimageservice")
    suspend fun callUploadImageService(
        @Part file: MultipartBody.Part,
        @Part(ApiConstant.STATUS_SERVICE) statusService: RequestBody
    ): Response<ServiceImageResponse>

    // fetch data old image job service
    @GET("api/job/imageservice")
    suspend fun callFetchImageService(): Response<ServiceImageResponse>

    companion object {
        operator fun invoke(networkHeaderInterceptor: NetworkHeaderInterceptor) =
            RetrofitClient.instant(networkHeaderInterceptor)
                .create(HeaderAppService::class.java)
    }

}
