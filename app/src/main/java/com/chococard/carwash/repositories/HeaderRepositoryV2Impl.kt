package com.chococard.carwash.repositories

import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.db.entities.User
import com.chococard.carwash.data.networks.HeaderAppServiceV2
import com.chococard.carwash.data.networks.request.*
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.JobResponse
import com.chococard.carwash.data.networks.response.UserResponse
import com.chococard.carwash.data.sharedpreference.SharedPreference
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MultipartBody
import okhttp3.RequestBody

class HeaderRepositoryV2Impl(
    private val api: HeaderAppServiceV2,
    private val db: AppDatabase,
    private val sharedPreference: SharedPreference
) : HeaderRepositoryV2 {

    // user
    override suspend fun callFetchUserInfo(): UserResponse {
        val response = api.callFetchUserInfo()
        if (response.success && response.user != null) saveUser(response.user)
        return response
    }

    private suspend fun saveUser(user: User) = db.getUserDao().saveUser(user)
    override suspend fun getDbUser(): User? = db.getUserDao().getDbUser()
    private suspend fun deleteUser() = db.getUserDao().deleteUser()
    // user

    override suspend fun callChangeImageProfile(file: MultipartBody.Part): BaseResponse {
        val response = api.callChangeImageProfile(file)
        if (response.success) callFetchUserInfo()
        return response
    }

    override suspend fun callLogout(): BaseResponse {
        val response = api.callLogout()
        if (response.success) clearUserAndToken()
        return response
    }

    override suspend fun callChangePhone(changePhone: ChangePhoneRequest): BaseResponse {
        val response = api.callChangePhone(changePhone)
        if (response.success) callFetchUserInfo()
        return response
    }

    override suspend fun callChangePassword(changePassword: ChangePasswordRequest): BaseResponse {
        val response = api.callChangePassword(changePassword)
        if (response.success) clearUserAndToken()
        return response
    }

    private suspend fun clearUserAndToken() {
        deleteUser()
        FirebaseAuth.getInstance().signOut()
        sharedPreference.accessToken = ""
        sharedPreference.refreshToken = ""
    }

    override suspend fun callSetLocation(setLocation: SetLocationRequest) =
        api.callSetLocation(setLocation)

    override suspend fun callFetchHistory(dateBegin: Long, dateEnd: Long) =
        api.callFetchHistory(dateBegin, dateEnd)

    // job
    override suspend fun callJobAnswer(jobAnswer: JobAnswerRequest): JobResponse {
        val response = api.callJobAnswer(jobAnswer)
        if (response.success && response.job != null) saveJob(response.job)
        return response
    }

    private suspend fun saveJob(job: Job) = db.getJobDao().saveJob(job)
    override fun getJob() = db.getJobDao().getJob()
    private suspend fun deleteJob() = db.getJobDao().deleteJob()
    //job

    override suspend fun callPaymentJob(): BaseResponse {
        val response = api.callPaymentJob()
        if (response.success) deleteJob()
        return response
    }

    override suspend fun callReportJob(report: ReportRequest): BaseResponse {
        val response = api.callReportJob(report)
        if (response.success) deleteJob()
        return response
    }

    override suspend fun callLogsActive(logsActive: LogsActiveRequest) =
        api.callLogsActive(logsActive)

    override suspend fun callSwitchSystem(switchSystem: SwitchSystemRequest) =
        api.callSwitchSystem(switchSystem)

    override suspend fun callValidatePhone(validatePhone: ValidatePhoneRequest) =
        api.callValidatePhone(validatePhone)

    override suspend fun callSetNavigation(setNavigation: SetNavigationRequest) =
        api.callSetNavigation(setNavigation)

    override suspend fun callJobStatusService() = api.callJobStatusService()

    override suspend fun callHomeScore() = api.callHomeScore()

    override suspend fun callUploadImageService(
        file: MultipartBody.Part,
        statusService: RequestBody?
    ) = api.callUploadImageService(file, statusService)

    override suspend fun callFetchImageService() = api.callFetchImageService()

    override suspend fun callDeleteServiceImage(deleteImageService: DeleteImageServiceRequest) =
        api.callDeleteServiceImage(deleteImageService)

    override suspend fun callDeleteServiceOtherImage(deleteImageService: DeleteImageServiceRequest) =
        api.callDeleteServiceOtherImage(deleteImageService)

}
