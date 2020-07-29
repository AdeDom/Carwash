package com.chococard.carwash.repositories

import androidx.lifecycle.LiveData
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.db.entities.User
import com.chococard.carwash.data.networks.request.*
import com.chococard.carwash.data.networks.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface HeaderRepositoryV2 {

    suspend fun callFetchUserInfo(): UserResponse

    suspend fun getDbUser(): User?

    fun getDbUserLiveData(): LiveData<User>

    suspend fun callChangeImageProfile(file: MultipartBody.Part): BaseResponse

    suspend fun callLogout(): BaseResponse

    suspend fun callChangePhone(changePhone: ChangePhoneRequest): BaseResponse

    suspend fun callChangePassword(changePassword: ChangePasswordRequest): BaseResponse

    suspend fun callSetLocation(setLocation: SetLocationRequest): BaseResponse

    suspend fun callFetchHistory(dateBegin: Long, dateEnd: Long): HistoryResponse

    suspend fun callJobAnswer(jobAnswer: JobAnswerRequest): JobResponse

    fun getJob(): LiveData<Job>

    suspend fun callPaymentJob(): BaseResponse

    suspend fun callReportJob(report: ReportRequest): BaseResponse

    suspend fun callLogsActive(logsActive: LogsActiveRequest): BaseResponse

    suspend fun callSwitchSystem(switchSystem: SwitchSystemRequest): BaseResponse

    suspend fun callValidatePhone(validatePhone: ValidatePhoneRequest): BaseResponse

    suspend fun callSetNavigation(setNavigation: SetNavigationRequest): NavigationResponse

    suspend fun callJobStatusService(): BaseResponse

    suspend fun callHomeScore(): HomeScoreResponse

    suspend fun callUploadImageService(
        file: MultipartBody.Part,
        statusService: RequestBody?
    ): ServiceImageResponse

    suspend fun callFetchImageService(): ServiceImageResponse

    suspend fun callDeleteServiceImage(deleteImageService: DeleteImageServiceRequest): ServiceImageResponse

    suspend fun callDeleteServiceOtherImage(deleteImageService: DeleteImageServiceRequest): ServiceImageResponse

}