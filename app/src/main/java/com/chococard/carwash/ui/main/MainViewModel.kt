package com.chococard.carwash.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.JobRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.JobResponse
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.util.base.BaseViewModel

class MainViewModel(private val repository: MainRepository) : BaseViewModel(repository) {

    private val _jobRequest = MutableLiveData<JobRequest>()
    val jobRequest: LiveData<JobRequest>
        get() = _jobRequest

    private val _jobResponse = MutableLiveData<JobResponse>()
    val jobResponse: LiveData<JobResponse>
        get() = _jobResponse

    private val _activeStatus = MutableLiveData<BaseResponse>()
    val activeStatus: LiveData<BaseResponse>
        get() = _activeStatus

    fun jobRequest() = launch {
        _jobRequest.value = repository.jobRequest()
    }

    fun jobResponse(jobStatus: String) = launch {
        _jobResponse.value = repository.jobResponse(jobStatus)
    }

    fun setActiveStatus(activityStatus: String) = launch {
        _activeStatus.value = repository.setActiveStatus(activityStatus)
    }

}
