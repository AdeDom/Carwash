package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.JobAnswerRequest
import com.chococard.carwash.data.networks.request.LogsActiveRequest
import com.chococard.carwash.data.networks.request.SetLocationRequest
import com.chococard.carwash.data.networks.request.SwitchSystemRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.JobResponse
import com.chococard.carwash.data.networks.response.UserResponse
import com.chococard.carwash.data.sharedpreference.SharedPreference
import com.chococard.carwash.repositories.HeaderRepository
import com.chococard.carwash.signalr.SignalREmployeeHub
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: HeaderRepository,
    private val sharedPreference: SharedPreference
) : BaseViewModel(),
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

    private val switchSystemResponse = MutableLiveData<BaseResponse>()
    val callSwitchSystem: LiveData<BaseResponse>
        get() = switchSystemResponse

    private val _counterExit = MutableLiveData<Int>()
    val counterExit: LiveData<Int>
        get() = _counterExit

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

    fun callSwitchSystem(switchSystem: SwitchSystemRequest) {
        sharedPreference.switchFlag = switchSystem.state ?: 0
        launchCallApi(
            request = { repository.callSwitchSystem(switchSystem) },
            response = { switchSystemResponse.value = it }
        )
    }

    fun setCounterExit() {
        launch {
            _counterExit.value = (_counterExit.value ?: 0).plus(1)
            delay(2_000)
            _counterExit.value = 0
        }
    }

    fun initSignalR(employeeId: Int) = SignalREmployeeHub(employeeId, this)

    override fun onReceive(job: JobResponse) = jobQuestionResponse.postValue(job)

}
