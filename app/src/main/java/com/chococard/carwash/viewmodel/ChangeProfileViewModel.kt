package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.ChangePhoneRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.UserResponse
import com.chococard.carwash.repositories.HeaderRepository
import okhttp3.MultipartBody
import okhttp3.ResponseBody

class ChangeProfileViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    val getDbUser = repository.getUser()

    private val userInfo = MutableLiveData<UserResponse>()
    val getUserInfo: LiveData<UserResponse>
        get() = userInfo

    private val upload = MutableLiveData<ResponseBody>()
    val getUpload: LiveData<ResponseBody>
        get() = upload

    private val changeProfile = MutableLiveData<BaseResponse>()
    val getChangeProfile: LiveData<BaseResponse>
        get() = changeProfile

    private val logout = MutableLiveData<BaseResponse>()
    val getLogout: LiveData<BaseResponse>
        get() = logout

    fun callUploadImageFile(file: MultipartBody.Part) = launchCallApi(
        request = { repository.callUploadImageFile(file) },
        response = { upload.value = it }
    )

    fun callChangeProfile(changePhone: ChangePhoneRequest) = launchCallApi(
        request = { repository.callChangeProfile(changePhone) },
        response = { response ->
            if (response != null && response.success) callFetchUserInfo()
            changeProfile.value = response
        }
    )

    private fun callFetchUserInfo() = launchCallApi(
        request = { repository.callFetchUserInfo() },
        response = { response ->
            userInfo.value = response
            response?.user?.let { repository.saveUser(it) }
        }
    )

    fun callLogout() = launchCallApi(
        request = { repository.callLogout() },
        response = { response ->
            if (response != null && response.success) repository.deleteUser()
            logout.value = response
        }
    )

}
