package com.chococard.carwash.ui.main.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.util.base.BaseViewModel

class MapViewModel(private val repository: MainRepository) : BaseViewModel(repository) {

    private val _setLocation = MutableLiveData<BaseResponse>()
    val setLocation: LiveData<BaseResponse>
        get() = _setLocation

    fun setLocation(latitude: Double, longitude: Double) = launch {
        _setLocation.value = repository.setLocation(latitude, longitude)
    }

}
