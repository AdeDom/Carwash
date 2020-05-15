package com.chococard.carwash.repositories

import com.chococard.carwash.data.models.SignIn
import com.chococard.carwash.data.networks.ConnectionAppService
import com.chococard.carwash.data.networks.SafeApiRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ConnectionRepository(
    private val api: ConnectionAppService
) : SafeApiRequest() {

    suspend fun callUploadImageFile(file: MultipartBody.Part, description: RequestBody) =
        apiRequest { api.callUploadImageFile(file, description) }

    suspend fun callSignUp(
        fullName: String,
        username: String,
        password: String,
        identityCard: String,
        phone: String
    ) = apiRequest { api.callSignUp(fullName, username, password, identityCard, phone) }

    suspend fun callSignIn(signIn: SignIn) = apiRequest { api.callSignIn(signIn) }

}
