package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.JobAnswerRequest
import com.chococard.carwash.data.networks.request.LogsActiveRequest
import com.chococard.carwash.data.networks.request.SetLocationRequest
import com.chococard.carwash.data.networks.request.SwitchSystemRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.JobResponse
import com.chococard.carwash.data.sharedpreference.SharedPreference
import com.chococard.carwash.repositories.HeaderRepository
import com.chococard.carwash.signalr.SignalREmployeeHub
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MainViewState(
    val loading: Boolean = false
)

class MainViewModel(
    private val repository: HeaderRepository,
    private val sharedPreference: SharedPreference
) : BaseViewModel<MainViewState>(MainViewState()),
    SignalREmployeeHub.SignalRListener {

    val getDbUserInfoLiveData = repository.getDbUserInfoLiveData()

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

    fun callJobAnswer(jobAnswer: JobAnswerRequest) {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callJobAnswer(jobAnswer)
                jobAnswerResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

    fun callLogsActive(logsActive: LogsActiveRequest) {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callLogsActive(logsActive)
                logsActiveResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

    fun callLogout() {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callLogout()
                logoutResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

    fun callSetLocation(setLocation: SetLocationRequest) {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callSetLocation(setLocation)
                locationResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

    fun callSwitchSystem(switchSystem: SwitchSystemRequest) {
        sharedPreference.switchFlag = switchSystem.state ?: 0
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callSwitchSystem(switchSystem)
                switchSystemResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

    fun setCounterExit() {
        launch {
            _counterExit.value = (_counterExit.value ?: 0).plus(1)
            delay(2_000)
            _counterExit.value = 0
        }
    }

    fun initSignalR() {
        launch {
            SignalREmployeeHub(repository.getDbUserInfo()?.userId, this@MainViewModel)
        }
    }

    override fun onReceive(job: JobResponse) = jobQuestionResponse.postValue(job)

}
