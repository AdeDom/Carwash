package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.repositories.HeaderRepositoryV2

data class ServiceInfoViewState(
    val loading: Boolean = false
)

class ServiceInfoViewModel(
    private val repository: HeaderRepositoryV2
) : BaseViewModelV2<ServiceInfoViewState>(ServiceInfoViewState()) {

    val getDbJobLiveData = repository.getDbJobLiveData()

    private val serviceNavigation = MutableLiveData<Pair<Double, Double>>()
    val getServiceNavigation: LiveData<Pair<Double, Double>>
        get() = serviceNavigation

    fun setValueServiceNavigation(navigation: Pair<Double, Double>) {
        serviceNavigation.value = navigation
    }

}
