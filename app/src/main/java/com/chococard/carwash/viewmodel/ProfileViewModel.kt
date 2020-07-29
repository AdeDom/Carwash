package com.chococard.carwash.viewmodel

import com.chococard.carwash.repositories.HeaderRepositoryV2

data class ProfileViewState(
    val loading: Boolean = false
)

class ProfileViewModel(
    private val repository: HeaderRepositoryV2
) : BaseViewModelV2<ProfileViewState>(ProfileViewState()) {

    val getDbUserLiveData = repository.getDbUserLiveData()

}
