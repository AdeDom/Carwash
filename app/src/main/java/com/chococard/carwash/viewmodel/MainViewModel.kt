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

    private val userInfo = MutableLiveData<UserResponse>()
    val getUserInfo: LiveData<UserResponse>
        get() = userInfo

    private val jobRequest = MutableLiveData<JobResponse>()
    val getJobRequest: LiveData<JobResponse>
        get() = jobRequest

    private val jobResponse = MutableLiveData<JobResponse>()
    val getJobResponse: LiveData<JobResponse>
        get() = jobResponse

    private val userLogsActive = MutableLiveData<BaseResponse>()
    val getLogsActive: LiveData<BaseResponse>
        get() = userLogsActive

    private val logout = MutableLiveData<BaseResponse>()
    val getLogout: LiveData<BaseResponse>
        get() = logout

    private val location = MutableLiveData<BaseResponse>()
    val getLocation: LiveData<BaseResponse>
        get() = location

    fun callFetchUserInfo() = launchCallApi(
        request = { repository.callFetchUserInfo() },
        response = { response ->
            userInfo.value = response
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

    fun callSetLogsActive(logsActive: LogsActiveRequest) = launchCallApi(
        request = { repository.callSetLogsActive(logsActive) },
        response = { userLogsActive.value = it }
    )

    fun callLogout() = launchCallApi(
        request = { repository.callLogout() },
        response = { response ->
            if (response != null && response.success) repository.deleteUser()
            logout.value = response
        }
    )

    fun callSetLocation(setLocation: SetLocationRequest) = launchCallApi(
        request = { repository.callSetLocation(setLocation) },
        response = { location.value = it }
    )

}
