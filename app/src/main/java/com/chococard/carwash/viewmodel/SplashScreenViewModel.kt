package com.chococard.carwash.viewmodel

import androidx.lifecycle.ViewModel
import com.chococard.carwash.data.sharedpreference.SharedPreference
import com.chococard.carwash.repositories.ConnectionRepository

class SplashScreenViewModel(
    private val repository: ConnectionRepository,
    private val sharedPreference: SharedPreference
) : ViewModel() {

    val getDbJob = repository.getJob()

    fun getSharedPreference() = sharedPreference.accessToken

}
