package com.chococard.carwash.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.repositories.BaseRepository
import com.chococard.carwash.viewmodel.AddWalletViewModel

@Suppress("UNCHECKED_CAST")
class AddWalletFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddWalletViewModel(repository) as T
    }
}
