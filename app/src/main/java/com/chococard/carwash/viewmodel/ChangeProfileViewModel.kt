package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.UserResponse
import com.chococard.carwash.repositories.BaseRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

class ChangeProfileViewModel(private val repository: BaseRepository) : BaseViewModel() {

    val getDbUser = repository.getUser()

    private val user = MutableLiveData<UserResponse>()
    val getUser: LiveData<UserResponse>
        get() = user

    private val upload = MutableLiveData<ResponseBody>()
    val getUpload: LiveData<ResponseBody>
        get() = upload

    private val changeProfile = MutableLiveData<BaseResponse>()
    val getChangeProfile: LiveData<BaseResponse>
        get() = changeProfile

    fun callFetchUser() = launchCallApi(
        request = { repository.callFetchUser() },
        response = { response ->
            user.value = response
            response?.user?.let { repository.saveUser(it) }
        }
    )

    fun callUploadImageFile(file: MultipartBody.Part, description: RequestBody) = launchCallApi(
        request = { repository.callUploadImageFile(file, description) },
        response = { upload.value = it }
    )

    fun deleteUser() = launch {
        repository.deleteUser()
    }

    fun callChangeProfile(name: String, identityCard: String, phone: String) = launchCallApi(
        request = { repository.callChangeProfile(name, identityCard, phone) },
        response = { changeProfile.value = it }
    )

}
