package com.chococard.carwash.repositories

import android.content.Context
import android.net.Uri
import com.chococard.carwash.data.db.AppDatabase
import com.chococard.carwash.data.networks.ConnectionAppService
import com.chococard.carwash.data.networks.SafeApiRequest
import com.chococard.carwash.data.networks.request.SignInRequest
import com.chococard.carwash.data.networks.request.ValidatePhoneRequest
import com.chococard.carwash.data.networks.response.SignInResponse
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.convertToMultipartBody
import com.chococard.carwash.util.extension.toRequestBody
import com.chococard.carwash.util.extension.writePref

class ConnectionRepositoryImpl(
    private val api: ConnectionAppService,
    private val db: AppDatabase,
    private val context: Context
) : SafeApiRequest(), ConnectionRepository {

    override fun getJob() = db.getJobDao().getJob()

    override suspend fun callSignUp(
        username: String,
        password: String,
        fullName: String,
        identityCard: String,
        phone: String,
        file: Uri
    ) = apiRequest {
        api.callSignUp(
            username.toRequestBody(),
            password.toRequestBody(),
            fullName.toRequestBody(),
            identityCard.toRequestBody(),
            phone.toRequestBody(),
            FlagConstant.EMPLOYEE.toRequestBody(),
            context.convertToMultipartBody(file)
        )
    }

    override suspend fun callSignIn(signIn: SignInRequest): SignInResponse {
        val response = apiRequest { api.callSignIn(signIn) }
        if (response.success) {
            response.token?.let { context.writePref(CommonsConstant.ACCESS_TOKEN, it) }
            response.refreshToken?.let { context.writePref(CommonsConstant.REFRESH_TOKEN, it) }
        }
        return response
    }

    override suspend fun callValidatePhone(validatePhone: ValidatePhoneRequest) =
        apiRequest { api.callValidatePhone(validatePhone) }

}
