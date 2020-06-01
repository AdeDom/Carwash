package com.chococard.carwash.repositories

import com.chococard.carwash.data.networks.ConnectionAppService
import com.chococard.carwash.data.networks.SafeApiRequest
import com.chococard.carwash.data.networks.request.SignIn
import com.chococard.carwash.data.networks.request.ValidatePhone
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ConnectionRepository(
    private val api: ConnectionAppService
) : SafeApiRequest() {

    suspend fun callSignUp(
        username: RequestBody,
        password: RequestBody,
        fullName: RequestBody,
        identityCard: RequestBody,
        phone: RequestBody,
        role: RequestBody,
        file: MultipartBody.Part
    ) = apiRequest { api.callSignUp(username, password, fullName, identityCard, phone, role, file) }

    suspend fun callSignIn(signIn: SignIn) = apiRequest { api.callSignIn(signIn) }

    suspend fun callValidatePhone(validatePhone: ValidatePhone) =
        apiRequest { api.callValidatePhone(validatePhone) }

}
