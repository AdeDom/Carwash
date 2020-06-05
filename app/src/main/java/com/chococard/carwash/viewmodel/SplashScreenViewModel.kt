package com.chococard.carwash.viewmodel

import com.chococard.carwash.repositories.ConnectionRepository

class SplashScreenViewModel(private val repository: ConnectionRepository) : BaseViewModel() {

    val getJob = repository.getJob()

}
