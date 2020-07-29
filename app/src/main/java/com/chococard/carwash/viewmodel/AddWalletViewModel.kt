package com.chococard.carwash.viewmodel

import com.chococard.carwash.repositories.HeaderRepository

data class AddWalletViewState(
    val loading: Boolean = false
)

class AddWalletViewModel(
    private val repository: HeaderRepository
) : BaseViewModel<AddWalletViewState>(AddWalletViewState()) {

    val getDbUserLiveData = repository.getDbUserLiveData()

}
