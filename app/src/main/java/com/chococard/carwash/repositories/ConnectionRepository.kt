package com.chococard.carwash.repositories

import androidx.lifecycle.LiveData
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.networks.request.SignInRequest
import com.chococard.carwash.data.networks.request.ValidatePhoneRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.SignInResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ConnectionRepository {

    fun getJob(): LiveData<Job>

    suspend fun callSignUp(
        username: RequestBody,
        password: RequestBody,
        fullName: RequestBody,
        identityCard: RequestBody,
        phone: RequestBody,
        role: RequestBody,
        file: MultipartBody.Part
    ): BaseResponse

    suspend fun callSignIn(signIn: SignInRequest): SignInResponse

    suspend fun callValidatePhone(validatePhone: ValidatePhoneRequest): BaseResponse

}
