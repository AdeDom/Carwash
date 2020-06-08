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

    fun callPayment(paymentStatus: Int) = launchCallApi(
        request = { repository.callPayment(paymentStatus) },
        response = { payment.value = it }
    )

}
