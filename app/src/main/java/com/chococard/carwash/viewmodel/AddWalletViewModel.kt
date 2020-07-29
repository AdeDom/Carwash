package com.chococard.carwash.viewmodel

import com.chococard.carwash.repositories.HeaderRepositoryV2

data class AddWalletViewState(
    val loading: Boolean = false
)

class AddWalletViewModel(
    private val repository: HeaderRepositoryV2
) : BaseViewModelV2<AddWalletViewState>(AddWalletViewState()) {

    val getDbUserLiveData = repository.getDbUserLiveData()

}
