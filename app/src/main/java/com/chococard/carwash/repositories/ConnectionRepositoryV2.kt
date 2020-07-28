package com.chococard.carwash.repositories

import androidx.lifecycle.LiveData
import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.networks.request.SignInRequest
import com.chococard.carwash.data.networks.request.ValidatePhoneRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.SignInResponse
import okhttp3.MultipartBody

interface ConnectionRepositoryV2 {

    fun getJob(): LiveData<Job>

    suspend fun callSignUp(
        username: String,
        password: String,
        fullName: String,
        identityCard: String,
        phone: String,
        part: MultipartBody.Part?
    ): BaseResponse

    suspend fun callSignIn(signIn: SignInRequest): SignInResponse

    suspend fun callValidatePhone(validatePhone: ValidatePhoneRequest): BaseResponse

}
