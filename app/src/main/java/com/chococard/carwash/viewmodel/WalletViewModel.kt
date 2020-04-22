package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.WalletResponse
import com.chococard.carwash.repositories.BaseRepository

class WalletViewModel(private val repository: BaseRepository) : BaseViewModel() {

    private val wallet = MutableLiveData<WalletResponse>()
    val getWallet: LiveData<WalletResponse>
        get() = wallet

    fun callFetchWallet(dateBegin: String = "", dateEnd: String = "") = launch {
        wallet.value = repository.callFetchWallet(dateBegin, dateEnd)
    }

}
