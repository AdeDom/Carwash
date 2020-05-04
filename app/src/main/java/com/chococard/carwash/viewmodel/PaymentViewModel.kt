package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.BaseRepository
import kotlinx.coroutines.launch

class PaymentViewModel(private val repository: BaseRepository) : BaseViewModel() {

    val getDbJob = repository.getJob()

    private val payment = MutableLiveData<BaseResponse>()
    val getPayment: LiveData<BaseResponse>
        get() = payment

    fun callPayment(
        paymentStatus: Int
    ) = ioThenMain(
        { repository.callPayment(paymentStatus) },
        { payment.value = it }
    )

    fun deleteJob() = launch {
        repository.deleteJob()
    }

}
