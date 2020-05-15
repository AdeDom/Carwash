package com.chococard.carwash.viewmodel

import com.chococard.carwash.repositories.HeaderRepository

class AddWalletViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    val getDbUser = repository.getUser()

}
