package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.LocationResponse
import com.chococard.carwash.repositories.BaseRepository

class MapViewModel(private val repository: BaseRepository) : BaseViewModel() {

    private val employeeLocation = MutableLiveData<LocationResponse>()
    val getEmployeeLocation: LiveData<LocationResponse>
        get() = employeeLocation

    fun callSetLocation(latitude: Double, longitude: Double) = launch {
        employeeLocation.value = repository.callSetLocation(latitude, longitude)
    }

}
