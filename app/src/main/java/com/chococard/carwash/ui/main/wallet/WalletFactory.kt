package com.chococard.carwash.ui.main.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.data.repositories.MainRepository

@Suppress("UNCHECKED_CAST")
class WalletFactory(
    private val repository: MainRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WalletViewModel(repository) as T
    }
}
