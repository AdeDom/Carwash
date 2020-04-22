package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.SignInResponse
import com.chococard.carwash.data.networks.response.UserResponse
import com.chococard.carwash.repositories.BaseRepository

class SignInViewModel(private val repository: BaseRepository) : BaseViewModel() {

    private val user = MutableLiveData<UserResponse>()
    val getUser: LiveData<UserResponse>
        get() = user

    private val signIn = MutableLiveData<SignInResponse>()
    val getSignIn: LiveData<SignInResponse>
        get() = signIn

    fun callFetchUser() = launch {
        user.value = repository.callFetchUser()
    }

    fun callSignIn(username: String, password: String) = launch {
        signIn.value = repository.callSignIn(username, password)
    }

}
