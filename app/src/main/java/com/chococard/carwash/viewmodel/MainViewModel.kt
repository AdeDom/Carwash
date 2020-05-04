package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.JobRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.JobResponse
import com.chococard.carwash.data.networks.response.UserResponse
import com.chococard.carwash.repositories.BaseRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: BaseRepository) : BaseViewModel() {

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

    fun callFetchUser() = ioThenMain(
        { repository.callFetchUser() },
        { response ->
            user.value = response
            response?.user?.let { repository.saveUser(it) }
        }
    )

    fun deleteUser() = launch {
        repository.deleteUser()
    }

    fun callJobRequest() = ioThenMain(
        { repository.callJobRequest() },
        { jobRequest.value = it }
    )

    fun callJobResponse(
        jobStatus: Int
    ) = ioThenMain(
        { repository.callJobResponse(jobStatus) },
        { jobResponse.value = it }
    )

    fun callSetActiveState(
        activityState: Int
    ) = ioThenMain(
        { repository.callSetActiveState(activityState) },
        { activeStatus.value = it }
    )

    fun callSetLogsActive(
        status: Int,
        keys: String
    ) = ioThenMain(
        { repository.callSetLogsActive(status, keys) },
        { logsActive.value = it }
    )

}
