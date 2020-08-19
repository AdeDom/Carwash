package com.chococard.carwash.repositories

import androidx.lifecycle.LiveData
import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.db.entities.UserInfo
import com.chococard.carwash.data.networks.HeaderAppService
import com.chococard.carwash.data.networks.request.*
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.ChangeImageProfileResponse
import com.chococard.carwash.data.networks.response.ChangePhoneNumberResponse
import com.chococard.carwash.data.networks.response.JobResponse
import com.chococard.carwash.data.sharedpreference.SharedPreference
import com.chococard.carwash.util.extension.toRequestBody
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MultipartBody

class HeaderRepositoryImpl(
    private val api: HeaderAppService,
    private val db: AppDatabase,
    private val sharedPreference: SharedPreference
) : HeaderRepository {

    // user info
    private suspend fun saveUserInfo(userInfo: UserInfo) = db.getUserInfoDao().saveUserInfo(userInfo)
    override suspend fun getDbUserInfo(): UserInfo? = db.getUserInfoDao().getDbUserInfo()
    override fun getDbUserInfoLiveData(): LiveData<UserInfo> = db.getUserInfoDao().getDbUserInfoLiveData()
    private suspend fun deleteUserInfo() = db.getUserInfoDao().deleteUserInfo()
    // user info

    override suspend fun callChangeImageProfile(file: MultipartBody.Part): ChangeImageProfileResponse {
        val response = api.callChangeImageProfile(file)
        if (response.success && response.userInfo != null) saveUserInfo(response.userInfo)
        return response
    }

    override suspend fun callLogout(): BaseResponse {
        val response = api.callLogout()
        if (response.success) clearUserAndToken()
        return response
    }

    override suspend fun callChangePhone(changePhone: ChangePhoneRequest): ChangePhoneNumberResponse {
        val response = api.callChangePhone(changePhone)
        if (response.success && response.userInfo != null) saveUserInfo(response.userInfo)
        return response
    }

    override suspend fun callChangePassword(changePassword: ChangePasswordRequest): BaseResponse {
        val response = api.callChangePassword(changePassword)
        if (response.success) clearUserAndToken()
        return response
    }

    private suspend fun clearUserAndToken() {
        deleteUserInfo()
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
    override fun getDbJobLiveData() = db.getJobDao().getJob()
    override suspend fun getDbJob(): Job? = db.getJobDao().getDbJob()
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
        statusService: Int,
        imageId: Int
    ) = api.callUploadImageService(file, statusService.toRequestBody(), imageId.toRequestBody())

    override suspend fun callFetchImageService() = api.callFetchImageService()

    override suspend fun callDeleteServiceImage(deleteImageService: DeleteImageServiceRequest) =
        api.callDeleteServiceImage(deleteImageService)

    override suspend fun callDeleteServiceOtherImage(deleteImageService: DeleteImageServiceRequest) =
        api.callDeleteServiceOtherImage(deleteImageService)

}
