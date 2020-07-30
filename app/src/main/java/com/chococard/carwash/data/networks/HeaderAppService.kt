package com.chococard.carwash.data.networks

import com.chococard.carwash.data.networks.request.*
import com.chococard.carwash.data.networks.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface HeaderAppService {

    //upload image from android to server when selected image.
    @Multipart
    @POST("api/account/changeprofile")
    suspend fun callChangeImageProfile(@Part file: MultipartBody.Part): ChangeImageProfileResponse

    //logout
    @POST("api/account/logout")
    suspend fun callLogout(): BaseResponse

    //change data profile name, id card, phone etc.
    @POST("api/account/changephone")
    suspend fun callChangePhone(@Body changePhone: ChangePhoneRequest): ChangePhoneNumberResponse

    //change password of username for security.
    @POST("api/account/changepassword")
    suspend fun callChangePassword(@Body changePassword: ChangePasswordRequest): BaseResponse

    //set my location now to server and response location other employee around.
    @POST("api/account/location")
    suspend fun callSetLocation(@Body setLocation: SetLocationRequest): BaseResponse

    //set user logs active & inactive using application.
    @POST("api/account/userlogs")
    suspend fun callLogsActive(@Body logsActive: LogsActiveRequest): BaseResponse

    //switch on-off (receive & reject) of job
    @POST("api/account/switchsystem")
    suspend fun callSwitchSystem(@Body switchSystem: SwitchSystemRequest): BaseResponse

    //verify phone before authentication firebase
    @POST("api/account/checkphone")
    suspend fun callValidatePhone(@Body validatePhone: ValidatePhoneRequest): BaseResponse

    //fetch data from server to set widget score or ratings
    @GET("api/account/homescore")
    suspend fun callHomeScore(): HomeScoreResponse

    //get old history ever service customer and filter by date begin & end.
    @GET("api/job/history")
    suspend fun callFetchHistory(
        @Query(ApiConstant.DATE_BEGIN) dateBegin: Long,
        @Query(ApiConstant.DATE_END) dateEnd: Long
    ): HistoryResponse

    //answer job request from customer also send flag cancel job or confirm job to server.
    @POST("api/job/jobanswer")
    suspend fun callJobAnswer(@Body jobAnswer: JobAnswerRequest): JobResponse

    //payment fee of application by send flag report or confirm to server.
    @POST("api/job/paymentjob")
    suspend fun callPaymentJob(): BaseResponse

    //report job service when have problem
    @POST("api/job/reportjob")
    suspend fun callReportJob(@Body report: ReportRequest): BaseResponse

    //send location employee and response location customer
    @POST("api/job/navigation")
    suspend fun callSetNavigation(@Body setNavigation: SetNavigationRequest): NavigationResponse

    //set status name of table job
    @POST("api/job/statusservice")
    suspend fun callJobStatusService(): BaseResponse

    //upload image service job 4 side and other image
    @Multipart
    @POST("api/job/uploadimageservice")
    suspend fun callUploadImageService(
        @Part file: MultipartBody.Part,
        @Part(ApiConstant.STATUS_SERVICE) statusService: RequestBody?,
        @Part(ApiConstant.IMAGE_ID) imageId: RequestBody?
    ): ServiceImageResponse

    // fetch data old image job service
    @GET("api/job/imageservice")
    suspend fun callFetchImageService(): ServiceImageResponse

    // update image 4 side to null
    @POST("api/job/deleteserviceimage")
    suspend fun callDeleteServiceImage(@Body deleteImageService: DeleteImageServiceRequest): ServiceImageResponse

    @POST("api/job/deleteserviceohterimage")
    suspend fun callDeleteServiceOtherImage(@Body deleteImageService: DeleteImageServiceRequest): ServiceImageResponse

    companion object {
        operator fun invoke(networkHeaderInterceptor: NetworkHeaderInterceptor): HeaderAppService {
            return RetrofitClient.instant(networkHeaderInterceptor)
                .create(HeaderAppService::class.java)
        }
    }

}
