package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.JobRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.JobResponse
import com.chococard.carwash.repositories.BaseRepository

class MainViewModel(private val repository: BaseRepository) : BaseViewModel() {

    private val jobRequest = MutableLiveData<JobRequest>()
    val getJobRequest: LiveData<JobRequest>
        get() = jobRequest

    private val jobResponse = MutableLiveData<JobResponse>()
    val getJobResponse: LiveData<JobResponse>
        get() = jobResponse

    private val activeStatus = MutableLiveData<BaseResponse>()
    val getActiveStatus: LiveData<BaseResponse>
        get() = activeStatus

    fun callJobRequest() = launch {
        jobRequest.value = repository.callJobRequest()
    }

    fun callJobResponse(jobStatus: Int) = launch {
        jobResponse.value = repository.callJobResponse(jobStatus)
    }

    fun callSetActiveState(activityState: Int) = launch {
        activeStatus.value = repository.callSetActiveState(activityState)
    }

}
