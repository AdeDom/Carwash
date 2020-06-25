package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.ChangePhoneRequest
import com.chococard.carwash.data.networks.request.ValidatePhoneRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository
import okhttp3.MultipartBody

class ChangeProfileViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    val getDbUser = repository.getUser()

    private val changeImageProfileResponse = MutableLiveData<BaseResponse>()
    val getChangeImageProfile: LiveData<BaseResponse>
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
        response = { changePhoneResponse.value = it }
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
