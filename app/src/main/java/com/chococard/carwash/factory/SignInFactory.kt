package com.chococard.carwash.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.repositories.BaseRepository
import com.chococard.carwash.viewmodel.SignInViewModel

@Suppress("UNCHECKED_CAST")
class SignInFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignInViewModel(repository) as T
    }
}
