package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepositoryV2
import kotlinx.coroutines.launch

data class HistoryDetailViewState(
    val loading: Boolean = false
)

class HistoryDetailViewModel(
    private val repository: HeaderRepositoryV2
) : BaseViewModelV2<HistoryDetailViewState>(HistoryDetailViewState()) {

    private val logoutResponse = MutableLiveData<BaseResponse>()
    val getLogout: LiveData<BaseResponse>
        get() = logoutResponse

    fun callLogout() {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callLogout()
                logoutResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

}
