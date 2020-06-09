package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.ReportRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository

class ReportViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val reportResponse = MutableLiveData<BaseResponse>()
    val getReportResponse: LiveData<BaseResponse>
        get() = reportResponse

    fun callReport(report: ReportRequest) = launchCallApi(
        request = { repository.callReport(report) },
        response = { response ->
            if (response != null && response.success) repository.deleteJob()
            reportResponse.value = response
        }
    )

}
