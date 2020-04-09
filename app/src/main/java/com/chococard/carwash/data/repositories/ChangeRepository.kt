package com.chococard.carwash.data.repositories

import com.chococard.carwash.data.networks.ChangeApi
import com.chococard.carwash.data.networks.SafeApiRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ChangeRepository(private val api: ChangeApi) : SafeApiRequest() {

    suspend fun uploadImageFile(file: MultipartBody.Part, description: RequestBody) =
        apiRequest { api.uploadImageFile(file, description) }

    suspend fun changeProfile(name: String, identityCard: String, phone: String) =
        apiRequest { api.changeProfile(name, identityCard, phone) }

}
