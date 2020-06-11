package com.chococard.carwash.repositories

import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.db.entities.User
import com.chococard.carwash.data.networks.HeaderAppService
import com.chococard.carwash.data.networks.SafeApiRequest
import com.chococard.carwash.data.networks.request.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class HeaderRepository(
    private val api: HeaderAppService,
    private val db: AppDatabase
) : SafeApiRequest() {

    // user
    suspend fun callFetchUserInfo() = apiRequest { api.callFetchUserInfo() }
    suspend fun saveUser(user: User) = db.getUserDao().saveUser(user)
    fun getUser() = db.getUserDao().getUser()
    suspend fun deleteUser() = db.getUserDao().deleteUser()

    suspend fun callChangeImageProfile(file: MultipartBody.Part) =
        apiRequest { api.callChangeImageProfile(file) }

    suspend fun callLogout() = apiRequest { api.callLogout() }

    suspend fun callChangePhone(changePhone: ChangePhoneRequest) =
        apiRequest { api.callChangePhone(changePhone) }

    suspend fun callChangePassword(changePassword: ChangePasswordRequest) =
        apiRequest { api.callChangePassword(changePassword) }

    suspend fun callSetLocation(setLocation: SetLocationRequest) =
        apiRequest { api.callSetLocation(setLocation) }

    suspend fun callFetchHistory(dateBegin: Long, dateEnd: Long) =
        apiRequest { api.callFetchHistory(dateBegin, dateEnd) }

    suspend fun callJobRequest() = apiRequest { api.callJobRequest() }

    // job
    suspend fun callJobResponse(jobAnswer: JobAnswerRequest) =
        apiRequest { api.callJobResponse(jobAnswer) }

    suspend fun saveJob(job: Job) = db.getJobDao().saveJob(job)
    fun getJob() = db.getJobDao().getJob()
    suspend fun deleteJob() = db.getJobDao().deleteJob()
    //job

    suspend fun callPaymentJob() = apiRequest { api.callPaymentJob() }

    suspend fun callReportJob(report: ReportRequest) = apiRequest { api.callReportJob(report) }

    suspend fun callLogsActive(logsActive: LogsActiveRequest) =
        apiRequest { api.callLogsActive(logsActive) }

    suspend fun callSwitchSystem(switchSystem: SwitchSystemRequest) =
        apiRequest { api.callSwitchSystem(switchSystem) }

    suspend fun callSetNavigation(setNavigation: SetNavigationRequest) =
        apiRequest { api.callSetNavigation(setNavigation) }

    suspend fun callSetJobStatusName() = apiRequest { api.callSetJobStatusName() }

    suspend fun callHomeScore() = apiRequest { api.callHomeScore() }

    suspend fun callUploadImageService(file: MultipartBody.Part, statusService: RequestBody) =
        apiRequest { api.callUploadImageService(file, statusService) }

    suspend fun callFetchImageService() = apiRequest { api.callFetchImageService() }

}
