package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.JobAnswerRequest
import com.chococard.carwash.data.networks.request.LogsActiveRequest
import com.chococard.carwash.data.networks.request.SetLocationRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.JobResponse
import com.chococard.carwash.data.networks.response.UserResponse
import com.chococard.carwash.repositories.HeaderRepository

class MainViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val userInfoResponse = MutableLiveData<UserResponse>()
    val getUserInfo: LiveData<UserResponse>
        get() = userInfoResponse

    private val jobRequest = MutableLiveData<JobResponse>()
    val getJobRequest: LiveData<JobResponse>
        get() = jobRequest

    private val jobResponse = MutableLiveData<JobResponse>()
    val getJobResponse: LiveData<JobResponse>
        get() = jobResponse

    private val logsActiveResponse = MutableLiveData<BaseResponse>()
    val getLogsActive: LiveData<BaseResponse>
        get() = logsActiveResponse

    private val logoutResponse = MutableLiveData<BaseResponse>()
    val getLogout: LiveData<BaseResponse>
        get() = logoutResponse

    private val locationResponse = MutableLiveData<BaseResponse>()
    val getLocation: LiveData<BaseResponse>
        get() = locationResponse

    fun callFetchUserInfo() = launchCallApi(
        request = { repository.callFetchUserInfo() },
        response = { response ->
            userInfoResponse.value = response
            response?.user?.let { repository.saveUser(it) }
        }
    )

    fun callJobRequest() = launchCallApi(
        request = { repository.callJobRequest() },
        response = { jobRequest.value = it }
    )

    fun callJobResponse(jobAnswer: JobAnswerRequest) = launchCallApi(
        request = { repository.callJobResponse(jobAnswer) },
        response = { response ->
            response?.job?.let { repository.saveJob(it) }
            jobResponse.value = response
        }
    )

    fun callLogsActive(logsActive: LogsActiveRequest) = launchCallApi(
        request = { repository.callLogsActive(logsActive) },
        response = { logsActiveResponse.value = it }
    )

    fun callLogout() = launchCallApi(
        request = { repository.callLogout() },
        response = { response ->
            if (response != null && response.success) repository.deleteUser()
            logoutResponse.value = response
        }
    )

    fun callSetLocation(setLocation: SetLocationRequest) = launchCallApi(
        request = { repository.callSetLocation(setLocation) },
        response = { locationResponse.value = it }
    )

}
