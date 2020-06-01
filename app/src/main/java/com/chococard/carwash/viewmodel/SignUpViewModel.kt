package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.ConnectionRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SignUpViewModel(private val repository: ConnectionRepository) : BaseViewModel() {

    private val signUp = MutableLiveData<BaseResponse>()
    val getSignUp: LiveData<BaseResponse>
        get() = signUp

    fun callSignUp(
        username: RequestBody,
        password: RequestBody,
        fullName: RequestBody,
        identityCard: RequestBody,
        phone: RequestBody,
        role: RequestBody,
        file: MultipartBody.Part
    ) = launchCallApi(
        request = {
            repository.callSignUp(
                username,
                password,
                fullName,
                identityCard,
                phone,
                role,
                file
            )
        },
        response = { signUp.value = it }
    )

}
