package com.chococard.carwash.viewmodel

import com.chococard.carwash.data.db.entities.User
import com.chococard.carwash.repositories.HeaderRepositoryV2
import kotlinx.coroutines.launch

data class ProfileViewState(
    val user: User? = User()
)

class ProfileViewModel(
    private val repository: HeaderRepositoryV2
) : BaseViewModelV2<ProfileViewState>(ProfileViewState()) {

    init {
        launch {
            val user = repository.getDbUser()
            setState { copy(user = user) }
        }
    }

}
