package com.chococard.carwash.ui.change

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.data.repositories.ChangeRepository

@Suppress("UNCHECKED_CAST")
class ChangeFactory(
    private val repository: ChangeRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChangeViewModel(repository) as T
    }
}
