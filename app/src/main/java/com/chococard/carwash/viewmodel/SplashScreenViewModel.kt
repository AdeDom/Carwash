package com.chococard.carwash.viewmodel

import androidx.lifecycle.ViewModel
import com.chococard.carwash.data.sharedpreference.SharedPreference
import com.chococard.carwash.repositories.ConnectionRepository

class SplashScreenViewModel(
    private val repository: ConnectionRepository,
    private val sharedPreference: SharedPreference
) : ViewModel() {

    suspend fun getDbJob() = repository.getDbJob()

    fun getSharedPreference() = sharedPreference.accessToken

}
