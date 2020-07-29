package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.ChangePasswordRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository
import kotlinx.coroutines.launch

data class ChangePasswordViewState(
    val loading: Boolean = false
)

class ChangePasswordViewModel(
    private val repository: HeaderRepository
) : BaseViewModel<ChangePasswordViewState>(ChangePasswordViewState()) {

    private val changePasswordResponse = MutableLiveData<BaseResponse>()
    val getChangePassword: LiveData<BaseResponse>
        get() = changePasswordResponse

    private val logoutResponse = MutableLiveData<BaseResponse>()
    val getLogout: LiveData<BaseResponse>
        get() = logoutResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _validateChangePassword = MutableLiveData<Boolean>()
    val validateChangePassword: LiveData<Boolean>
        get() = _validateChangePassword

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
                launch {
                    try {
                        setState { copy(loading = true) }
                        val request = ChangePasswordRequest(oldPassword, newPassword)
                        val response = repository.callChangePassword(request)
                        changePasswordResponse.value = response
                        setState { copy(loading = false) }
                    } catch (e: Throwable) {
                        setState { copy(loading = false) }
                        setError(e)
                    }
                }
            }
        }
    }

    fun callLogout() {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callLogout()
                logoutResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

    fun setValueValidateChangePassword(oldPassword: String, newPassword: String, rePassword: String) {
        when {
            oldPassword.isEmpty() -> _validateChangePassword.value = false
            oldPassword.length < 8 -> _validateChangePassword.value = false
            newPassword.isEmpty() -> _validateChangePassword.value = false
            newPassword.length < 8 -> _validateChangePassword.value = false
            rePassword.isEmpty() -> _validateChangePassword.value = false
            rePassword.length < 8 -> _validateChangePassword.value = false
            newPassword != rePassword -> _validateChangePassword.value = false
            else -> _validateChangePassword.value = true
        }
    }

}
