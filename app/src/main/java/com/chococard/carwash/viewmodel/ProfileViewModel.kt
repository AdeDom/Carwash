package com.chococard.carwash.viewmodel

import com.chococard.carwash.repositories.HeaderRepository

data class ProfileViewState(
    val loading: Boolean = false
)

class ProfileViewModel(
    private val repository: HeaderRepository
) : BaseViewModel<ProfileViewState>(ProfileViewState()) {

    val getDbUserLiveData = repository.getDbUserLiveData()

}
