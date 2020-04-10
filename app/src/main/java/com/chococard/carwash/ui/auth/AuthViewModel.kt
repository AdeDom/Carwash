package com.chococard.carwash.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.SignInResponse
import com.chococard.carwash.data.repositories.AuthRepository
import com.chococard.carwash.util.BaseViewModel

class AuthViewModel(private val repository: AuthRepository) : BaseViewModel(repository) {

    private val _signUp = MutableLiveData<BaseResponse>()
    val signUp: LiveData<BaseResponse>
        get() = _signUp

    private val _signIn = MutableLiveData<SignInResponse>()
    val signIn: LiveData<SignInResponse>
        get() = _signIn

    fun signUp(
        name: String,
        username: String,
        password: String,
        identityCard: String,
        phone: String
    ) = launch {
        _signUp.value = repository.signUp(name, username, password, identityCard, phone)
    }

    fun signIn(username: String, password: String) = launch {
        _signIn.value = repository.signIn(username, password)
    }

}
