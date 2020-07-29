package com.chococard.carwash.repositories

import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.db.entities.User
import com.chococard.carwash.data.networks.ConnectionAppService
import com.chococard.carwash.data.networks.request.SignInRequest
import com.chococard.carwash.data.networks.request.ValidatePhoneRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.SignInResponse
import com.chococard.carwash.data.sharedpreference.SharedPreference
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.toRequestBody
import okhttp3.MultipartBody

class ConnectionRepositoryImpl(
    private val api: ConnectionAppService,
    private val db: AppDatabase,
    private val sharedPreference: SharedPreference
) : ConnectionRepository {

    override suspend fun getDbUser(): User? = db.getUserDao().getDbUser()

    override suspend fun getDbJob(): Job? = db.getJobDao().getDbJob()

    override suspend fun callSignUp(
        username: String?,
        password: String?,
        fullName: String?,
        identityCard: String?,
        phone: String?,
        part: MultipartBody.Part?
    ): BaseResponse {
        return api.callSignUp(
            username.toRequestBody(),
            password.toRequestBody(),
            fullName.toRequestBody(),
            identityCard.toRequestBody(),
            phone.toRequestBody(),
            FlagConstant.EMPLOYEE.toRequestBody(),
            part
        )
    }

    override suspend fun callSignIn(signIn: SignInRequest): SignInResponse {
        val response = api.callSignIn(signIn)
        if (response.success) {
            sharedPreference.accessToken = response.token.orEmpty()
            sharedPreference.refreshToken = response.refreshToken.orEmpty()
        }
        return response
    }

    override suspend fun callValidatePhone(validatePhone: ValidatePhoneRequest): BaseResponse {
        return api.callValidatePhone(validatePhone)
    }

}
