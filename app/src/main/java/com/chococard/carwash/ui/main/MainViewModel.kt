package com.chococard.carwash.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.JobRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.util.base.BaseViewModel

class MainViewModel(private val repository: MainRepository) : BaseViewModel(repository) {

    private val _jobRequest = MutableLiveData<JobRequest>()
    val jobRequest: LiveData<JobRequest>
        get() = _jobRequest

    private val _setStatus = MutableLiveData<BaseResponse>()
    val setStatus: LiveData<BaseResponse>
        get() = _setStatus

    fun jobRequest() = launch {
        _jobRequest.value = repository.jobRequest()
    }

    fun setStatus(status: String) = launch {
        _setStatus.value = repository.setStatus(status)
    }

}
