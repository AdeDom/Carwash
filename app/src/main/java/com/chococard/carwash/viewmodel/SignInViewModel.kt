package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.SignInRequest
import com.chococard.carwash.data.networks.response.SignInResponse
import com.chococard.carwash.repositories.ConnectionRepository
import kotlinx.coroutines.launch

data class SignInViewState(
    val loading: Boolean = false
)

class SignInViewModel(
    private val repository: ConnectionRepository
) : BaseViewModelV2<SignInViewState>(SignInViewState()) {

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
                launch {
                    try {
                        setState { copy(loading = true) }
                        val response = repository.callSignIn(SignInRequest(username, password))
                        signInResponse.value = response
                        setState { copy(loading = false) }
                    } catch (e: Throwable) {
                        setState { copy(loading = false) }
                        setError(e)
                    }
                }
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
