package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.ChangePasswordRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository

class ChangePasswordViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val changePassword = MutableLiveData<BaseResponse>()
    val getChangePassword: LiveData<BaseResponse>
        get() = changePassword

    private val logout = MutableLiveData<BaseResponse>()
    val getLogout: LiveData<BaseResponse>
        get() = logout

    fun callChangePassword(changePassword: ChangePasswordRequest) = launchCallApi(
        request = { repository.callChangePassword(changePassword) },
        response = { response ->
            if (response != null && response.success) repository.deleteUser()
            this.changePassword.value = response
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
