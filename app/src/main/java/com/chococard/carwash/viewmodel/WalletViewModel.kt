package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.WalletResponse
import com.chococard.carwash.repositories.HeaderRepository

class WalletViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val wallet = MutableLiveData<WalletResponse>()
    val getWallet: LiveData<WalletResponse>
        get() = wallet

    fun callFetchWallet(dateBegin: String = "", dateEnd: String = "") = launchCallApi(
        request = { repository.callFetchWallet(dateBegin, dateEnd) },
        response = { wallet.value = it }
    )

}
