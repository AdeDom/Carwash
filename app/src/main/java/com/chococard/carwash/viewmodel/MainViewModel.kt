package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.JobRequest
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

    private val activeStatus = MutableLiveData<BaseResponse>()
    val getActiveStatus: LiveData<BaseResponse>
        get() = activeStatus

    private val logsActive = MutableLiveData<BaseResponse>()
    val getLogsActive: LiveData<BaseResponse>
        get() = logsActive

    private val logout = MutableLiveData<BaseResponse>()
    val getLogout: LiveData<BaseResponse>
        get() = logout

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
        response = { response ->
            jobRequest.value = response
            response?.job?.let { repository.saveJob(it) }
        }
    )

    fun callJobResponse(jobStatus: Int) = launchCallApi(
        request = { repository.callJobResponse(jobStatus) },
        response = { jobResponse.value = it }
    )

    fun callSetActiveState(activityState: Int) = launchCallApi(
        request = { repository.callSetActiveState(activityState) },
        response = { activeStatus.value = it }
    )

    fun callSetLogsActive(status: Int, keys: String) = launchCallApi(
        request = { repository.callSetLogsActive(status, keys) },
        response = { logsActive.value = it }
    )

    fun callLogout() = launchCallApi(
        request = { repository.callLogout() },
        response = { logout.value = it }
    )

}