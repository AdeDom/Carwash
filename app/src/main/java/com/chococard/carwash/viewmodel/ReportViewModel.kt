package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.ReportRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository
import kotlinx.coroutines.launch

class ReportViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val reportResponse = MutableLiveData<BaseResponse>()
    val getReportResponse: LiveData<BaseResponse>
        get() = reportResponse

    fun deleteDbJob() = launch {
        repository.deleteJob()
    }

    fun callReport(report: ReportRequest) = launchCallApi(
        request = { repository.callReport(report) },
        response = { reportResponse.value = it }
    )

}
