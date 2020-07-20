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
import com.chococard.carwash.signalr.SignalREmployeeHub

class MainViewModel(private val repository: HeaderRepository) : BaseViewModel(),
    SignalREmployeeHub.SignalRListener {

    val getDbUser = repository.getUser()

    private val userInfoResponse = MutableLiveData<UserResponse>()
    val getUserInfo: LiveData<UserResponse>
        get() = userInfoResponse

    private val jobAnswerResponse = MutableLiveData<JobResponse>()
    val getJobAnswer: LiveData<JobResponse>
        get() = jobAnswerResponse

    private val logsActiveResponse = MutableLiveData<BaseResponse>()
    val getLogsActive: LiveData<BaseResponse>
        get() = logsActiveResponse

    private val logoutResponse = MutableLiveData<BaseResponse>()
    val getLogout: LiveData<BaseResponse>
        get() = logoutResponse

    private val locationResponse = MutableLiveData<BaseResponse>()
    val getLocation: LiveData<BaseResponse>
        get() = locationResponse

    private val jobQuestionResponse = MutableLiveData<JobResponse>()
    val getJobQuestion: LiveData<JobResponse>
        get() = jobQuestionResponse

    fun callFetchUserInfo() = launchCallApi(
        request = { repository.callFetchUserInfo() },
        response = { userInfoResponse.value = it }
    )

    fun callJobAnswer(jobAnswer: JobAnswerRequest) = launchCallApi(
        request = { repository.callJobAnswer(jobAnswer) },
        response = { jobAnswerResponse.value = it }
    )

    fun callLogsActive(logsActive: LogsActiveRequest) = launchCallApi(
        request = { repository.callLogsActive(logsActive) },
        response = { logsActiveResponse.value = it }
    )

    fun callLogout() = launchCallApi(
        request = { repository.callLogout() },
        response = { logoutResponse.value = it }
    )

    fun callSetLocation(setLocation: SetLocationRequest) = launchCallApi(
        request = { repository.callSetLocation(setLocation) },
        response = { locationResponse.value = it }
    )

    fun initSignalR(employeeId: Int) = SignalREmployeeHub(employeeId, this)

    override fun onReceive(job: JobResponse) = jobQuestionResponse.postValue(job)

}
