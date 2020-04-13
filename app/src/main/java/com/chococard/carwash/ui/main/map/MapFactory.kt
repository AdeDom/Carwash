package com.chococard.carwash.ui.main.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.data.repositories.MainRepository

@Suppress("UNCHECKED_CAST")
class MapFactory(
    private val repository: MainRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MapViewModel(repository) as T
    }
}
