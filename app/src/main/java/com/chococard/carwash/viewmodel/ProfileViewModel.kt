package com.chococard.carwash.viewmodel

import com.chococard.carwash.repositories.HeaderRepository

class ProfileViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    val getDbUser = repository.getUser()

}
