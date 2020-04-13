package com.chococard.carwash.ui.main.wallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.WalletResponse
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.util.base.BaseViewModel

class WalletViewModel(private val repository: MainRepository) : BaseViewModel(repository) {

    private val _wallet = MutableLiveData<WalletResponse>()
    val wallet: LiveData<WalletResponse>
        get() = _wallet

    fun fetchWallet(dateBegin: String = "", dateEnd: String = "") = launch {
        _wallet.value = repository.fetchWallet(dateBegin, dateEnd)
    }

}
