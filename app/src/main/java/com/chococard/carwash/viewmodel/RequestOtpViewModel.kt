package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.ValidatePhone
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.ConnectionRepository

class RequestOtpViewModel(private val repository: ConnectionRepository) : BaseViewModel() {

    private val validatePhoneResponse = MutableLiveData<BaseResponse>()
    val getValidatePhone: LiveData<BaseResponse>
        get() = validatePhoneResponse

    fun callValidatePhone(validatePhone: ValidatePhone) = launchCallApi(
        request = { repository.callValidatePhone(validatePhone) },
        response = { validatePhoneResponse.value = it }
    )

}
