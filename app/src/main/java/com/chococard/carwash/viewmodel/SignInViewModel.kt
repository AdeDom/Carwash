package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.SignInRequest
import com.chococard.carwash.data.networks.response.SignInResponse
import com.chococard.carwash.repositories.ConnectionRepository
import kotlinx.coroutines.launch

data class SignInViewState(
    val username: String = "",
    val password: String = "",
    val isValidUsername: Boolean = false,
    val isValidPassword: Boolean = false,
    val loading: Boolean = false
)

enum class ValidateSignIn {
    USERNAME_EMPTY,
    USERNAME_INCORRECT,
    PASSWORD_EMPTY,
    PASSWORD_INCORRECT,
    VALIDATE_SUCCESS
}

class SignInViewModel(
    private val repository: ConnectionRepository
) : BaseViewModel<SignInViewState>(SignInViewState()) {

    private val signInResponse = MutableLiveData<SignInResponse>()
    val getSignIn: LiveData<SignInResponse>
        get() = signInResponse

    private val _onSignIn = MutableLiveData<ValidateSignIn>()
    val onSignIn: LiveData<ValidateSignIn>
        get() = _onSignIn

    fun onSignIn() {
        _onSignIn.value = when {
            state.value?.username.orEmpty().isBlank() -> ValidateSignIn.USERNAME_EMPTY
            state.value?.username?.length ?: 0 < 4 -> ValidateSignIn.USERNAME_INCORRECT
            state.value?.password.orEmpty().isBlank() -> ValidateSignIn.PASSWORD_EMPTY
            state.value?.password?.length ?: 0 < 8 -> ValidateSignIn.PASSWORD_INCORRECT
            else -> ValidateSignIn.VALIDATE_SUCCESS
        }
    }

    fun setUsername(username: String) {
        setState {
            copy(
                username = username,
                isValidUsername = username.isNotBlank() && username.length >= 4
            )
        }
    }

    fun setPassword(password: String) {
        setState {
            copy(
                password = password,
                isValidPassword = password.isNotBlank() && password.length >= 8
            )
        }
    }

    fun callSignIn() {
        launch {
            try {
                setState { copy(loading = true) }
                val request = SignInRequest(state.value?.username, state.value?.password)
                signInResponse.value = repository.callSignIn(request)
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

}
