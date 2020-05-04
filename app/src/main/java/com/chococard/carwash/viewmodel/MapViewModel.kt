package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.LocationResponse
import com.chococard.carwash.repositories.BaseRepository

class MapViewModel(private val repository: BaseRepository) : BaseViewModel() {

    val getDbUser = repository.getUser()

    private val employeeLocation = MutableLiveData<LocationResponse>()
    val getEmployeeLocation: LiveData<LocationResponse>
        get() = employeeLocation

    fun callSetLocation(
        latitude: Double,
        longitude: Double
    ) = ioThenMain(
        { repository.callSetLocation(latitude, longitude) },
        { employeeLocation.value = it }
    )

}
