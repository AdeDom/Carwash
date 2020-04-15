package com.chococard.carwash.ui.main.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.LocationResponse
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.util.base.BaseViewModel

class MapViewModel(private val repository: MainRepository) : BaseViewModel(repository) {

    private val _employeeLocation = MutableLiveData<LocationResponse>()
    val employeeLocation: LiveData<LocationResponse>
        get() = _employeeLocation

    fun setLocation(latitude: Double, longitude: Double) = launch {
        _employeeLocation.value = repository.setLocation(latitude, longitude)
    }

}
