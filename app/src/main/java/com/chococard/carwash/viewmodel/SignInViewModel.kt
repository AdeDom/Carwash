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

    fun callFetchUser() = ioThenMain(
        { repository.callFetchUser() },
        { user.value = it }
    )

    fun callSignIn(
        username: String,
        password: String
    ) = ioThenMain(
        { repository.callSignIn(username, password) },
        { signIn.value = it }
    )

}
