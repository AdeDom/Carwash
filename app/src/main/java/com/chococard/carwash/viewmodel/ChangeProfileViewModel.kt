package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.ChangePhoneRequest
import com.chococard.carwash.data.networks.request.ValidatePhoneRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.UserResponse
import com.chococard.carwash.repositories.HeaderRepository
import okhttp3.MultipartBody
import okhttp3.ResponseBody

class ChangeProfileViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    val getDbUser = repository.getUser()

    private val userInfoResponse = MutableLiveData<UserResponse>()
    val getUserInfo: LiveData<UserResponse>
        get() = userInfoResponse

    private val changeImageProfileResponse = MutableLiveData<ResponseBody>()
    val getChangeImageProfile: LiveData<ResponseBody>
        get() = changeImageProfileResponse

    private val changePhoneResponse = MutableLiveData<BaseResponse>()
    val getChangePhone: LiveData<BaseResponse>
        get() = changePhoneResponse

    private val logoutResponse = MutableLiveData<BaseResponse>()
    val getLogout: LiveData<BaseResponse>
        get() = logoutResponse

    private val validatePhoneResponse = MutableLiveData<BaseResponse>()
    val getValidatePhone: LiveData<BaseResponse>
        get() = validatePhoneResponse

    fun callChangeImageProfile(file: MultipartBody.Part) = launchCallApi(
        request = { repository.callChangeImageProfile(file) },
        response = { changeImageProfileResponse.value = it }
    )

    fun callChangePhone(changePhone: ChangePhoneRequest) = launchCallApi(
        request = { repository.callChangePhone(changePhone) },
        response = { response ->
            if (response != null && response.success) callFetchUserInfo()
            changePhoneResponse.value = response
        }
    )

    private fun callFetchUserInfo() = launchCallApi(
        request = { repository.callFetchUserInfo() },
        response = { response ->
            userInfoResponse.value = response
            response?.user?.let { repository.saveUser(it) }
        }
    )

    fun callLogout() = launchCallApi(
        request = { repository.callLogout() },
        response = { logoutResponse.value = it }
    )

    fun callValidatePhone(validatePhone: ValidatePhoneRequest) = launchCallApi(
        request = { repository.callValidatePhone(validatePhone) },
        response = { validatePhoneResponse.value = it }
    )

}
