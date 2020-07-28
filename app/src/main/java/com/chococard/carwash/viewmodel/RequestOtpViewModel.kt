package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.ValidatePhoneRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.ConnectionRepository
import com.chococard.carwash.util.extension.isVerifyPhone
import kotlinx.coroutines.launch

data class RequestOtpViewState(
    val phoneNumber: String = "",
    val isValidPhoneNumber: Boolean = false,
    val loading: Boolean = false
)

enum class ValidatePhoneNumber {
    PHONE_EMPTY,
    PHONE_TOTAL_10,
    PHONE_INCORRECT,
    VALIDATE_SUCCESS
}

class RequestOtpViewModel(
    private val repository: ConnectionRepository
) : BaseViewModelV2<RequestOtpViewState>(RequestOtpViewState()) {

    private val validatePhoneResponse = MutableLiveData<BaseResponse>()
    val getValidatePhone: LiveData<BaseResponse>
        get() = validatePhoneResponse

    private val _onValidatePhone = MutableLiveData<ValidatePhoneNumber>()
    val onValidatePhone: LiveData<ValidatePhoneNumber>
        get() = _onValidatePhone

    fun onValidatePhone() {
        _onValidatePhone.value = when {
            state.value?.phoneNumber.orEmpty().isBlank() -> ValidatePhoneNumber.PHONE_EMPTY
            state.value?.phoneNumber?.length != 10 -> ValidatePhoneNumber.PHONE_TOTAL_10
            state.value?.phoneNumber.orEmpty().isVerifyPhone() -> ValidatePhoneNumber.PHONE_INCORRECT
            else -> ValidatePhoneNumber.VALIDATE_SUCCESS
        }
    }

    fun callValidatePhone() {
        launch {
            try {
                setState { copy(loading = true) }
                val request = ValidatePhoneRequest(state.value?.phoneNumber)
                validatePhoneResponse.value = repository.callValidatePhone(request)
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
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

}
