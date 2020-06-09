package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository

class PaymentViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    val getDbJob = repository.getJob()

    private val payment = MutableLiveData<BaseResponse>()
    val getPayment: LiveData<BaseResponse>
        get() = payment

    fun callPayment() = launchCallApi(
        request = { repository.callPayment() },
        response = { response ->
            if (response != null && response.success) repository.deleteJob()
            payment.value = response
        }
    )

}
