package com.chococard.carwash.viewmodel

import com.chococard.carwash.repositories.BaseRepository

class AddWalletViewModel(private val repository: BaseRepository) : BaseViewModel() {

    val getDbUser = repository.getUser()

}
