package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.ValidatePhoneRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.ConnectionRepository
import com.chococard.carwash.util.extension.isVerifyPhone

class RequestOtpViewModel(private val repository: ConnectionRepository) : BaseViewModel() {

    private val validatePhoneResponse = MutableLiveData<BaseResponse>()
    val getValidatePhone: LiveData<BaseResponse>
        get() = validatePhoneResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _validatePhone = MutableLiveData<Boolean>()
    val validatePhone: LiveData<Boolean>
        get() = _validatePhone

    fun callValidatePhone(phoneNumber: String) {
        when {
            phoneNumber.isEmpty() ->
                _errorMessage.value = "Please enter phone"
            phoneNumber.length != 10 ->
                _errorMessage.value = "Please enter a total of 10 characters"
            phoneNumber.isVerifyPhone() ->
                _errorMessage.value = "Please enter the correct phone number"
            else -> {
                val validatePhone = ValidatePhoneRequest(phoneNumber)
                launchCallApi(
                    request = { repository.callValidatePhone(validatePhone) },
                    response = { validatePhoneResponse.value = it }
                )
            }
        }
    }

    fun validatePhone(phoneNumber: String) {
        when {
            phoneNumber.isEmpty() -> _validatePhone.value = false
            phoneNumber.length != 10 -> _validatePhone.value = false
            phoneNumber.isVerifyPhone() -> _validatePhone.value = false
            else -> _validatePhone.value = true
        }
    }

}
