package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepositoryV2
import kotlinx.coroutines.launch

data class PaymentViewState(
    val loading: Boolean = false
)

class PaymentViewModel(
    private val repository: HeaderRepositoryV2
) : BaseViewModelV2<PaymentViewState>(PaymentViewState()) {

    val getDbJobLiveData = repository.getDbJobLiveData()

    private val paymentJobResponse = MutableLiveData<BaseResponse>()
    val getPaymentJob: LiveData<BaseResponse>
        get() = paymentJobResponse

    fun callPaymentJob() {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callPaymentJob()
                paymentJobResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

}
