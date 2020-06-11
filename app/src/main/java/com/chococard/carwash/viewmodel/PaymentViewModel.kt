package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository

class PaymentViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    val getDbJob = repository.getJob()

    private val paymentJobResponse = MutableLiveData<BaseResponse>()
    val getPaymentJob: LiveData<BaseResponse>
        get() = paymentJobResponse

    fun callPaymentJob() = launchCallApi(
        request = { repository.callPaymentJob() },
        response = { response ->
            if (response != null && response.success) repository.deleteJob()
            paymentJobResponse.value = response
        }
    )

}
