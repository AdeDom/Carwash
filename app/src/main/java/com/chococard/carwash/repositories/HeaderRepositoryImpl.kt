package com.chococard.carwash.repositories

import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.db.entities.User
import com.chococard.carwash.data.networks.HeaderAppService
import com.chococard.carwash.data.networks.SafeApiRequest
import com.chococard.carwash.data.networks.request.*
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class HeaderRepositoryImpl(
    private val api: HeaderAppService,
    private val db: AppDatabase
) : SafeApiRequest(), HeaderRepository {

    // user
    override suspend fun callFetchUserInfo(): UserResponse {
        val response = apiRequest { api.callFetchUserInfo() }
        if (response.success && response.user != null) saveUser(response.user)
        return response
    }

    override suspend fun saveUser(user: User) = db.getUserDao().saveUser(user)
    override fun getUser() = db.getUserDao().getUser()
    private suspend fun deleteUser() = db.getUserDao().deleteUser()
    // user

    override suspend fun callChangeImageProfile(file: MultipartBody.Part) =
        apiRequest { api.callChangeImageProfile(file) }

    override suspend fun callLogout(): BaseResponse {
        val response = apiRequest { api.callLogout() }
        if (response.success) deleteUser()
        return response
    }

    override suspend fun callChangePhone(changePhone: ChangePhoneRequest): BaseResponse {
        val response = apiRequest { api.callChangePhone(changePhone) }
        if (response.success) callFetchUserInfo()
        return response
    }

    override suspend fun callChangePassword(changePassword: ChangePasswordRequest): BaseResponse {
        val response = apiRequest { api.callChangePassword(changePassword) }
        if (response.success) deleteUser()
        return response
    }

    override suspend fun callSetLocation(setLocation: SetLocationRequest) =
        apiRequest { api.callSetLocation(setLocation) }

    override suspend fun callFetchHistory(dateBegin: Long, dateEnd: Long) =
        apiRequest { api.callFetchHistory(dateBegin, dateEnd) }

    override suspend fun callJobQuestion() = apiRequest { api.callJobQuestion() }

    // job
    override suspend fun callJobAnswer(jobAnswer: JobAnswerRequest) =
        apiRequest { api.callJobAnswer(jobAnswer) }

    override suspend fun saveJob(job: Job) = db.getJobDao().saveJob(job)
    override fun getJob() = db.getJobDao().getJob()
    override suspend fun deleteJob() = db.getJobDao().deleteJob()
    //job

    override suspend fun callPaymentJob() = apiRequest { api.callPaymentJob() }

    override suspend fun callReportJob(report: ReportRequest) =
        apiRequest { api.callReportJob(report) }

    override suspend fun callLogsActive(logsActive: LogsActiveRequest) =
        apiRequest { api.callLogsActive(logsActive) }

    override suspend fun callSwitchSystem(switchSystem: SwitchSystemRequest) =
        apiRequest { api.callSwitchSystem(switchSystem) }

    override suspend fun callValidatePhone(validatePhone: ValidatePhoneRequest) =
        apiRequest { api.callValidatePhone(validatePhone) }

    override suspend fun callSetNavigation(setNavigation: SetNavigationRequest) =
        apiRequest { api.callSetNavigation(setNavigation) }

    override suspend fun callJobStatusService() = apiRequest { api.callJobStatusService() }

    override suspend fun callHomeScore() = apiRequest { api.callHomeScore() }

    override suspend fun callUploadImageService(
        file: MultipartBody.Part,
        statusService: RequestBody
    ) = apiRequest { api.callUploadImageService(file, statusService) }

    override suspend fun callFetchImageService() = apiRequest { api.callFetchImageService() }

    override suspend fun callDeleteServiceImage(deleteImageService: DeleteImageServiceRequest) =
        apiRequest { api.callDeleteServiceImage(deleteImageService) }

    override suspend fun callDeleteServiceOtherImage(deleteImageService: DeleteImageServiceRequest) =
        apiRequest { api.callDeleteServiceOtherImage(deleteImageService) }

}
