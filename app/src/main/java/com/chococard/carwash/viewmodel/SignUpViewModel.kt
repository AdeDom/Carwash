package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.BaseRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

class SignUpViewModel(private val repository: BaseRepository) : BaseViewModel() {

    private val upload = MutableLiveData<ResponseBody>()
    val getUpload: LiveData<ResponseBody>
        get() = upload

    private val signUp = MutableLiveData<BaseResponse>()
    val getSignUp: LiveData<BaseResponse>
        get() = signUp

    fun callUploadImageFile(file: MultipartBody.Part, description: RequestBody) = launch {
        upload.value = repository.callUploadImageFile(file, description)
    }

    fun callSignUp(
        name: String,
        username: String,
        password: String,
        identityCard: String,
        phone: String
    ) = launch {
        signUp.value = repository.callSignUp(name, username, password, identityCard, phone)
    }

}
