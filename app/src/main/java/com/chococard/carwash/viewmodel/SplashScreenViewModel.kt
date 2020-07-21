package com.chococard.carwash.viewmodel

import com.chococard.carwash.data.sharedpreference.SharedPreference
import com.chococard.carwash.repositories.ConnectionRepository

class SplashScreenViewModel(
    private val repository: ConnectionRepository,
    private val sharedPreference: SharedPreference
) : BaseViewModel() {

    val getDbJob = repository.getJob()

    fun getSharedPreference() = sharedPreference.accessToken

}
