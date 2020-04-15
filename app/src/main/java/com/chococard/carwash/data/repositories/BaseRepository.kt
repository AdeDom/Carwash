package com.chococard.carwash.data.repositories

import com.chococard.carwash.data.networks.BaseApi
import com.chococard.carwash.data.networks.SafeApiRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

abstract class BaseRepository(private val api: BaseApi) : SafeApiRequest() {

    suspend fun fetchUser() = apiRequest { api.fetchUser() }

    suspend fun uploadImageFile(file: MultipartBody.Part, description: RequestBody) =
        apiRequest { api.uploadImageFile(file, description) }

}
