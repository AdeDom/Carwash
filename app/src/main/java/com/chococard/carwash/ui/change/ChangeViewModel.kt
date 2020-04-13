package com.chococard.carwash.ui.change

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.repositories.ChangeRepository
import com.chococard.carwash.util.base.BaseViewModel

class ChangeViewModel(private val repository: ChangeRepository) : BaseViewModel(repository) {

    private val _changeProfile = MutableLiveData<BaseResponse>()
    val changeProfile: LiveData<BaseResponse>
        get() = _changeProfile

    private val _changePassword = MutableLiveData<BaseResponse>()
    val changePassword: LiveData<BaseResponse>
        get() = _changePassword

    fun changeProfile(name: String, identityCard: String, phone: String) = launch {
        _changeProfile.value = repository.changeProfile(name, identityCard, phone)
    }

    fun changePassword(oldPassword: String, newPassword: String) = launch {
        _changePassword.value = repository.changePassword(oldPassword, newPassword)
    }

}
