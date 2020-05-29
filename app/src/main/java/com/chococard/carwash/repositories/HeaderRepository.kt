package com.chococard.carwash.repositories

import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.db.entities.User
import com.chococard.carwash.data.networks.HeaderAppService
import com.chococard.carwash.data.networks.SafeApiRequest
import com.chococard.carwash.data.networks.request.ChangePhone
import com.chococard.carwash.data.networks.request.LogsActive
import com.chococard.carwash.data.networks.request.SetLocation
import com.chococard.carwash.data.networks.request.SwitchSystem
import okhttp3.MultipartBody

class HeaderRepository(
    private val api: HeaderAppService,
    private val db: AppDatabase
) : SafeApiRequest() {

    // user
    suspend fun callFetchUser() = apiRequest { api.callFetchUser() }
    suspend fun saveUser(user: User) = db.getUserDao().saveUser(user)
    fun getUser() = db.getUserDao().getUser()
    suspend fun deleteUser() = db.getUserDao().deleteUser()

    suspend fun callUploadImageFile(file: MultipartBody.Part) =
        apiRequest { api.callUploadImageFile(file) }

    suspend fun callLogout() = apiRequest { api.callLogout() }

    suspend fun callChangeProfile(changePhone: ChangePhone) =
        apiRequest { api.callChangeProfile(changePhone) }

    suspend fun callChangePassword(oldPassword: String, newPassword: String) =
        apiRequest { api.callChangePassword(oldPassword, newPassword) }

    suspend fun callSetLocation(setLocation: SetLocation) =
        apiRequest { api.callSetLocation(setLocation) }

    suspend fun callFetchHistory(dateBegin: String, dateEnd: String) =
        apiRequest { api.callFetchHistory(dateBegin, dateEnd) }

    // job
    suspend fun callJobRequest() = apiRequest { api.callJobRequest() }
    suspend fun saveJob(job: Job) = db.getJobDao().saveJob(job)
    fun getJob() = db.getJobDao().getJob()
    suspend fun deleteJob() = db.getJobDao().deleteJob()

    suspend fun callJobResponse(jobStatus: Int) = apiRequest { api.callJobResponse(jobStatus) }

    suspend fun callPayment(paymentStatus: Int) = apiRequest { api.callPayment(paymentStatus) }

    suspend fun callSetLogsActive(logsActive: LogsActive) =
        apiRequest { api.callSetLogsActive(logsActive) }

    suspend fun callSwitchSystem(switchSystem: SwitchSystem) =
        apiRequest { api.callSwitchSystem(switchSystem) }

}
