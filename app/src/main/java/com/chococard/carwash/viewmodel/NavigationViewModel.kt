package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.SetNavigationRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.NavigationResponse
import com.chococard.carwash.repositories.HeaderRepository
import kotlinx.coroutines.launch

data class NavigationViewState(
    val loading: Boolean = false
)

class NavigationViewModel(
    private val repository: HeaderRepository
) : BaseViewModel<NavigationViewState>(NavigationViewState()) {

    private val navigationResponse = MutableLiveData<NavigationResponse>()
    val getNavigation: LiveData<NavigationResponse>
        get() = navigationResponse

    private val jobStatusServiceResponse = MutableLiveData<BaseResponse>()
    val getJobStatusService: LiveData<BaseResponse>
        get() = jobStatusServiceResponse

    val getDbUserLiveData = repository.getDbUserInfoLiveData()

    val getDbJobLiveData = repository.getDbJobLiveData()

    fun callSetNavigation(setNavigation: SetNavigationRequest) {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callSetNavigation(setNavigation)
                navigationResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

    fun callJobStatusService() {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callJobStatusService()
                jobStatusServiceResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

}
