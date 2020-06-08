package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository
import kotlinx.coroutines.launch

class PaymentViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    val getDbJob = repository.getJob()

    fun deleteDbJob() = launch {
        repository.deleteJob()
    }

    private val payment = MutableLiveData<BaseResponse>()
    val getPayment: LiveData<BaseResponse>
        get() = payment

    fun callPayment() = launchCallApi(
        request = { repository.callPayment() },
        response = { payment.value = it }
    )

}
