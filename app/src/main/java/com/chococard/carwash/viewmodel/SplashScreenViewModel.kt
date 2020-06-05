package com.chococard.carwash.viewmodel

import com.chococard.carwash.repositories.HeaderRepository

class SplashScreenViewModel(private val repository: HeaderRepository) : BaseViewModel(){

    val getJob = repository.getJob()

}
