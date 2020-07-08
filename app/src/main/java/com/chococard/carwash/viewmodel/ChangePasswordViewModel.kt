package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.ChangePasswordRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository

class ChangePasswordViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val changePasswordResponse = MutableLiveData<BaseResponse>()
    val getChangePassword: LiveData<BaseResponse>
        get() = changePasswordResponse

    private val logoutResponse = MutableLiveData<BaseResponse>()
    val getLogout: LiveData<BaseResponse>
        get() = logoutResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun callChangePassword(oldPassword: String, newPassword: String, rePassword: String) {
        when {
            oldPassword.isEmpty() ->
                _errorMessage.value = "Please enter old password"
            oldPassword.length < 8 ->
                _errorMessage.value = "Please enter an old password of at least 8 characters"
            newPassword.isEmpty() ->
                _errorMessage.value = "Please enter new password"
            newPassword.length < 8 ->
                _errorMessage.value = "Please enter an new password of at least 8 characters"
            rePassword.isEmpty() ->
                _errorMessage.value = "Please enter re-password"
            rePassword.length < 8 ->
                _errorMessage.value = "Please enter an re-password of at least 8 characters"
            newPassword != rePassword ->
                _errorMessage.value = "Please enter the password to match"
            else -> {
                val changePassword = ChangePasswordRequest(oldPassword, newPassword)
                launchCallApi(
                    request = { repository.callChangePassword(changePassword) },
                    response = { changePasswordResponse.value = it }
                )
            }
        }
    }

    fun callLogout() = launchCallApi(
        request = { repository.callLogout() },
        response = { logoutResponse.value = it }
    )

}
