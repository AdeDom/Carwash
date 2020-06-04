package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.ChangePasswordRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository
import kotlinx.coroutines.launch

class ChangePasswordViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val changePasswordResponse = MutableLiveData<BaseResponse>()
    val getChangePassword: LiveData<BaseResponse>
        get() = changePasswordResponse

    private val logout = MutableLiveData<BaseResponse>()
    val getLogout: LiveData<BaseResponse>
        get() = logout

    fun deleteUser() = launch {
        repository.deleteUser()
    }

    fun callChangePassword(changePassword: ChangePasswordRequest) = launchCallApi(
        request = { repository.callChangePassword(changePassword) },
        response = { changePasswordResponse.value = it }
    )

    fun callLogout() = launchCallApi(
        request = { repository.callLogout() },
        response = { logout.value = it }
    )

}
