package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.UserResponse
import com.chococard.carwash.repositories.BaseRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

class ChangeProfileViewModel(private val repository: BaseRepository) : BaseViewModel() {

    private val user = MutableLiveData<UserResponse>()
    val getUser: LiveData<UserResponse>
        get() = user

    private val upload = MutableLiveData<ResponseBody>()
    val getUpload: LiveData<ResponseBody>
        get() = upload

    private val changeProfile = MutableLiveData<BaseResponse>()
    val getChangeProfile: LiveData<BaseResponse>
        get() = changeProfile

    fun callFetchUser() = launch {
        user.value = repository.callFetchUser()
    }

    fun callUploadImageFile(file: MultipartBody.Part, description: RequestBody) = launch {
        upload.value = repository.callUploadImageFile(file, description)
    }

    fun callChangeProfile(name: String, identityCard: String, phone: String) = launch {
        changeProfile.value = repository.callChangeProfile(name, identityCard, phone)
    }

}
