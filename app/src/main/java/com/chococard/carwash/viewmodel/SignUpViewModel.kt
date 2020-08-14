package com.chococard.carwash.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.ConnectionRepository
import com.chococard.carwash.util.extension.convertToMultipartBody
import com.chococard.carwash.util.extension.isVerifyIdentityCard
import com.chococard.carwash.util.extension.isVerifyPhone
import kotlinx.coroutines.launch

data class SignUpViewState(
    val username: String = "",
    val password: String = "",
    val rePassword: String = "",
    val fullName: String = "",
    val identityCard: String = "",
    val phoneNumber: String = "",
    val image: String = "",
    val isValidUsername: Boolean = false,
    val isValidPassword: Boolean = false,
    val isValidRePassword: Boolean = false,
    val isValidFullName: Boolean = false,
    val isValidIdentityCard: Boolean = false,
    val isValidPhoneNumber: Boolean = false,
    val isValidImage: Boolean = false,
    val loading: Boolean = false
)

enum class ValidateSignUp {
    USERNAME_EMPTY,
    USERNAME_INCORRECT,
    PASSWORD_EMPTY,
    PASSWORD_INCORRECT,
    RE_PASSWORD_EMPTY,
    RE_PASSWORD_INCORRECT,
    PASSWORD_NOT_MATCHED,
    FULL_NAME_EMPTY,
    IDENTITY_CARD_EMPTY,
    IDENTITY_CARD_TOTAL_13,
    IDENTITY_CARD_INCORRECT,
    PHONE_EMPTY,
    PHONE_TOTAL_10,
    PHONE_INCORRECT,
    IMAGE_EMPTY,
    VALIDATE_SUCCESS
}

class SignUpViewModel(
    private val context: Context,
    private val repository: ConnectionRepository
) : BaseViewModel<SignUpViewState>(SignUpViewState()) {

    private val signUpResponse = MutableLiveData<BaseResponse>()
    val getSignUp: LiveData<BaseResponse>
        get() = signUpResponse

    private val _onSignUp = MutableLiveData<ValidateSignUp>()
    val onSignUp: LiveData<ValidateSignUp>
        get() = _onSignUp

    fun onSignUp() {
        _onSignUp.value = when {
            state.value?.username.orEmpty().isBlank() -> ValidateSignUp.USERNAME_EMPTY
            state.value?.username?.length ?: 0 < 4 -> ValidateSignUp.USERNAME_INCORRECT
            state.value?.password.orEmpty().isBlank() -> ValidateSignUp.PASSWORD_EMPTY
            state.value?.password?.length ?: 0 < 8 -> ValidateSignUp.PASSWORD_INCORRECT
            state.value?.rePassword.orEmpty().isBlank() -> ValidateSignUp.RE_PASSWORD_EMPTY
            state.value?.rePassword?.length ?: 0 < 8 -> ValidateSignUp.RE_PASSWORD_INCORRECT
            state.value?.password != state.value?.rePassword -> ValidateSignUp.PASSWORD_NOT_MATCHED
            state.value?.fullName.orEmpty().isBlank() -> ValidateSignUp.FULL_NAME_EMPTY
            state.value?.identityCard.orEmpty().isBlank() -> ValidateSignUp.IDENTITY_CARD_EMPTY
            state.value?.identityCard?.length ?: 0 != 13 -> ValidateSignUp.IDENTITY_CARD_TOTAL_13
            state.value?.identityCard?.isVerifyIdentityCard()
                ?: true -> ValidateSignUp.IDENTITY_CARD_INCORRECT
            state.value?.phoneNumber.orEmpty().isBlank() -> ValidateSignUp.PHONE_EMPTY
            state.value?.phoneNumber?.length ?: 0 != 10 -> ValidateSignUp.PHONE_TOTAL_10
            state.value?.phoneNumber?.isVerifyPhone() ?: true -> ValidateSignUp.PHONE_INCORRECT
            state.value?.image.orEmpty().isBlank() -> ValidateSignUp.IMAGE_EMPTY
            else -> ValidateSignUp.VALIDATE_SUCCESS
        }
    }

    fun callSignUp() {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callSignUp(
                    state.value?.username,
                    state.value?.password,
                    state.value?.fullName,
                    state.value?.identityCard,
                    state.value?.phoneNumber,
                    context.convertToMultipartBody(Uri.parse(state.value?.image))
                )
                signUpResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
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

    fun setRePassword(rePassword: String) {
        setState {
            copy(
                rePassword = rePassword,
                isValidRePassword = rePassword.isNotBlank() && rePassword.length >= 8 && password == rePassword
            )
        }
    }

    fun setFullName(fullName: String) {
        setState {
            copy(
                fullName = fullName,
                isValidFullName = fullName.isNotBlank()
            )
        }
    }

    fun setIdentityCard(identityCard: String) {
        setState {
            copy(
                identityCard = identityCard,
                isValidIdentityCard = identityCard.isNotBlank() && identityCard.length == 13 && !identityCard.isVerifyIdentityCard()
            )
        }
    }

    fun setPhoneNumber(phoneNumber: String) {
        setState {
            copy(
                phoneNumber = phoneNumber,
                isValidPhoneNumber = phoneNumber.isNotBlank() && phoneNumber.length == 10 && !phoneNumber.isVerifyPhone()
            )
        }
    }

    fun setImage(uri: String) {
        setState {
            copy(
                image = uri,
                isValidImage = uri.isNotBlank()
            )
        }
    }

}
