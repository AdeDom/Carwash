package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.ReportRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository

class ReportViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val reportJobResponse = MutableLiveData<BaseResponse>()
    val getReportJob: LiveData<BaseResponse>
        get() = reportJobResponse

    fun callReportJob(report: ReportRequest) = launchCallApi(
        request = { repository.callReportJob(report) },
        response = { response ->
            if (response != null && response.success) repository.deleteJob()
            reportJobResponse.value = response
        }
    )

}
