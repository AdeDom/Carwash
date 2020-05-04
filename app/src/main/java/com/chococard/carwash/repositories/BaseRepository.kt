package com.chococard.carwash.repositories

import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.db.entities.User
import com.chococard.carwash.data.networks.AppService
import com.chococard.carwash.data.networks.SafeApiRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BaseRepository(
    private val api: AppService,
    private val db: AppDatabase
) : SafeApiRequest() {

    // user
    suspend fun callFetchUser() = apiRequest { api.callFetchUser() }
    suspend fun saveUser(user: User) = db.getUserDao().saveUser(user)
    fun getUser() = db.getUserDao().getUser()
    suspend fun deleteUser() = db.getUserDao().deleteUser()

    suspend fun callUploadImageFile(file: MultipartBody.Part, description: RequestBody) =
        apiRequest { api.callUploadImageFile(file, description) }

    suspend fun callSignUp(
        name: String,
        username: String,
        password: String,
        identityCard: String,
        phone: String
    ) = apiRequest { api.callSignUp(name, username, password, identityCard, phone) }

    suspend fun callSignIn(username: String, password: String) =
        apiRequest { api.callSignIn(username, password) }

    suspend fun callChangeProfile(name: String, identityCard: String, phone: String) =
        apiRequest { api.callChangeProfile(name, identityCard, phone) }

    suspend fun callChangePassword(oldPassword: String, newPassword: String) =
        apiRequest { api.callChangePassword(oldPassword, newPassword) }

    suspend fun callSetLocation(latitude: Double, longitude: Double) =
        apiRequest { api.callSetLocation(latitude, longitude) }

    suspend fun callFetchWallet(dateBegin: String, dateEnd: String) =
        apiRequest { api.callFetchWallet(dateBegin, dateEnd) }

    suspend fun callFetchHistory(dateBegin: String, dateEnd: String) =
        apiRequest { api.callFetchHistory(dateBegin, dateEnd) }

    suspend fun callJobRequest() = apiRequest { api.callJobRequest() }

    suspend fun callJobResponse(jobStatus: Int) = apiRequest { api.callJobResponse(jobStatus) }

    suspend fun callPayment(paymentStatus: Int) = apiRequest { api.callPayment(paymentStatus) }

    suspend fun callSetActiveState(activityState: Int) =
        apiRequest { api.callSetActiveState(activityState) }

    suspend fun callSetLogsActive(status: Int, keys: String) =
        apiRequest { api.callSetLogsActive(status, keys) }

}
