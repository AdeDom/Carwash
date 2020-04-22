package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.BaseRepository

class ChangePasswordViewModel (private val repository: BaseRepository) : BaseViewModel() {

    private val changePassword = MutableLiveData<BaseResponse>()
    val getChangePassword: LiveData<BaseResponse>
        get() = changePassword

    fun callChangePassword(oldPassword: String, newPassword: String) = launch {
        changePassword.value = repository.callChangePassword(oldPassword, newPassword)
    }

}
