package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.ReportRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository
import kotlinx.coroutines.launch

data class ReportViewState(
    val loading: Boolean = false
)

class ReportViewModel(
    private val repository: HeaderRepository
) : BaseViewModel<ReportViewState>(ReportViewState()) {

    private val reportJobResponse = MutableLiveData<BaseResponse>()
    val getReportJob: LiveData<BaseResponse>
        get() = reportJobResponse

    fun callReportJob(report: ReportRequest) {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callReportJob(report)
                reportJobResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

}
