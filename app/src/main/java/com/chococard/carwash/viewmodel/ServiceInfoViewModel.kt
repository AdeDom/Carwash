package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.repositories.HeaderRepository

data class ServiceInfoViewState(
    val loading: Boolean = false
)

class ServiceInfoViewModel(
    private val repository: HeaderRepository
) : BaseViewModel<ServiceInfoViewState>(ServiceInfoViewState()) {

    val getDbJobLiveData = repository.getDbJobLiveData()

    private val _serviceNavigation = MutableLiveData<ServiceNavigation>()
    val serviceNavigation: LiveData<ServiceNavigation>
        get() = _serviceNavigation

    suspend fun setServiceNavigation(beginLatitude: Double, beginLongitude: Double) {
        val endLatitude = repository.getDbJob()?.latitude
        val endLongitude = repository.getDbJob()?.longitude
        val serviceNavigation = ServiceNavigation(
            beginLatitude,
            beginLongitude,
            endLatitude,
            endLongitude
        )
        _serviceNavigation.value = serviceNavigation
    }

}

data class ServiceNavigation(
    val beginLatitude: Double? = null,
    val beginLongitude: Double? = null,
    val endLatitude: Double? = null,
    val endLongitude: Double? = null
)
