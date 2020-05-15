package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.models.SignIn
import com.chococard.carwash.data.networks.response.SignInResponse
import com.chococard.carwash.repositories.ConnectionRepository

class SignInViewModel(private val repository: ConnectionRepository) : BaseViewModel() {

    private val signInResponse = MutableLiveData<SignInResponse>()
    val getSignIn: LiveData<SignInResponse>
        get() = signInResponse

    fun callSignIn(signIn: SignIn) = launchCallApi(
        request = { repository.callSignIn(signIn) },
        response = { signInResponse.value = it }
    )

}
