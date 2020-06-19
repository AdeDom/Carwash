package com.chococard.carwash.repositories

import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.networks.ConnectionAppService
import com.chococard.carwash.data.networks.SafeApiRequest
import com.chococard.carwash.data.networks.request.SignInRequest
import com.chococard.carwash.data.networks.request.ValidatePhoneRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ConnectionRepositoryImpl(
    private val api: ConnectionAppService,
    private val db: AppDatabase
) : SafeApiRequest(), ConnectionRepository {

    override fun getJob() = db.getJobDao().getJob()

    override suspend fun callSignUp(
        username: RequestBody,
        password: RequestBody,
        fullName: RequestBody,
        identityCard: RequestBody,
        phone: RequestBody,
        role: RequestBody,
        file: MultipartBody.Part
    ) = apiRequest { api.callSignUp(username, password, fullName, identityCard, phone, role, file) }

    override suspend fun callSignIn(signIn: SignInRequest) = apiRequest { api.callSignIn(signIn) }

    override suspend fun callValidatePhone(validatePhone: ValidatePhoneRequest) =
        apiRequest { api.callValidatePhone(validatePhone) }

}
