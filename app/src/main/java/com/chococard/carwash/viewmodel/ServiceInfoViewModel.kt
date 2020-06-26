package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.repositories.HeaderRepository

class ServiceInfoViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    val getDbJob = repository.getJob()

    private val serviceNavigation = MutableLiveData<Pair<Double, Double>>()
    val getServiceNavigation: LiveData<Pair<Double, Double>>
        get() = serviceNavigation

    fun setValueServiceNavigation(navigation: Pair<Double, Double>) {
        serviceNavigation.value = navigation
    }

}
