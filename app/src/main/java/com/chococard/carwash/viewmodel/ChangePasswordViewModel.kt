package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.BaseRepository
import kotlinx.coroutines.launch

class ChangePasswordViewModel(private val repository: BaseRepository) : BaseViewModel() {

    private val changePassword = MutableLiveData<BaseResponse>()
    val getChangePassword: LiveData<BaseResponse>
        get() = changePassword

    private val logout = MutableLiveData<BaseResponse>()
    val getLogout: LiveData<BaseResponse>
        get() = logout

    fun deleteUser() = launch {
        repository.deleteUser()
    }

    fun callChangePassword(oldPassword: String, newPassword: String) = launchCallApi(
        request = { repository.callChangePassword(oldPassword, newPassword) },
        response = { changePassword.value = it }
    )

    fun callLogout() = launchCallApi(
        request = { repository.callLogout() },
        response = { logout.value = it }
    )

}
