package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.SignInRequest
import com.chococard.carwash.data.networks.response.SignInResponse
import com.chococard.carwash.repositories.ConnectionRepository

class SignInViewModel(private val repository: ConnectionRepository) : BaseViewModel() {

    private val signInResponse = MutableLiveData<SignInResponse>()
    val getSignIn: LiveData<SignInResponse>
        get() = signInResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _validateSignIn = MutableLiveData<Boolean>()
    val validateSignIn: LiveData<Boolean>
        get() = _validateSignIn

    fun callSignIn(username: String, password: String) {
        when {
            username.isEmpty() -> _errorMessage.value = "Please enter username"
            username.length < 4 -> _errorMessage.value = "Please enter at least 4 characters"
            password.isEmpty() -> _errorMessage.value = "Please enter password"
            password.length < 8 -> _errorMessage.value = "Please enter at least 8 characters"
            else -> {
                val signIn = SignInRequest(username, password)
                launchCallApi(
                    request = { repository.callSignIn(signIn) },
                    response = { signInResponse.value = it }
                )
            }
        }
    }

    fun validateSignIn(username: String, password: String) {
        when {
            username.isEmpty() -> _validateSignIn.value = false
            username.length < 4 -> _validateSignIn.value = false
            password.isEmpty() -> _validateSignIn.value = false
            password.length < 8 -> _validateSignIn.value = false
            else -> _validateSignIn.value = true
        }
    }

}
