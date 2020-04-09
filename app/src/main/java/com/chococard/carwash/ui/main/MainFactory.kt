package com.chococard.carwash.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class MainFactory(
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}
