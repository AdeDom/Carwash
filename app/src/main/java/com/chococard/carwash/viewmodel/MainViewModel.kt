package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.JobRequest
import com.chococard.carwash.data.networks.request.LogsActive
import com.chococard.carwash.data.networks.request.SetLocation
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.JobResponse
import com.chococard.carwash.data.networks.response.UserResponse
import com.chococard.carwash.repositories.HeaderRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val user = MutableLiveData<UserResponse>()
    val getUser: LiveData<UserResponse>
        get() = user

    private val jobRequest = MutableLiveData<JobRequest>()
    val getJobRequest: LiveData<JobRequest>
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

    fun callFetchUser() = launchCallApi(
        request = { repository.callFetchUser() },
        response = { response ->
            user.value = response
            response?.user?.let { repository.saveUser(it) }
        }
    )

    fun deleteUser() = launch {
        repository.deleteUser()
    }

    fun callJobRequest() = launchCallApi(
        request = { repository.callJobRequest() },
        response = { jobRequest.value = it }
    )

    fun callJobResponse(jobStatus: Int) = launchCallApi(
        request = { repository.callJobResponse(jobStatus) },
        response = { jobResponse.value = it }
    )

    fun callSetLogsActive(logsActive: LogsActive) = launchCallApi(
        request = { repository.callSetLogsActive(logsActive) },
        response = { userLogsActive.value = it }
    )

    fun callLogout() = launchCallApi(
        request = { repository.callLogout() },
        response = { logout.value = it }
    )

    fun callSetLocation(setLocation: SetLocation) = launchCallApi(
        request = { repository.callSetLocation(setLocation) },
        response = { location.value = it }
    )

}
