package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.repositories.ConnectionRepository
import kotlinx.coroutines.launch

object SplashScreenViewState

enum class RootNavigation {
    AUTHENTICATION, MAIN, HAS_JOB
}

class SplashScreenViewModel(
    private val repository: ConnectionRepository
) : BaseViewModel<SplashScreenViewState>(SplashScreenViewState) {

    private val _rootNavigation = MutableLiveData<RootNavigation>()
    val rootNavigation: LiveData<RootNavigation>
        get() = _rootNavigation

    fun initialize() {
        launch {
            _rootNavigation.value = when {
                repository.getDbJob() != null -> RootNavigation.HAS_JOB
                repository.getDbUser() != null -> RootNavigation.MAIN
                else -> RootNavigation.AUTHENTICATION
            }
        }
    }

}
