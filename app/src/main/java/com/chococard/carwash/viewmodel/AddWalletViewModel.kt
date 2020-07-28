package com.chococard.carwash.viewmodel

import com.chococard.carwash.data.db.entities.User
import com.chococard.carwash.repositories.HeaderRepositoryV2
import kotlinx.coroutines.launch

data class AddWalletViewState(
    val user: User? = User()
)

class AddWalletViewModel(
    private val repository: HeaderRepositoryV2
) : BaseViewModelV2<AddWalletViewState>(AddWalletViewState()) {

    init {
        launch {
            val user = repository.getDbUser()
            setState { copy(user = user) }
        }
    }

}
