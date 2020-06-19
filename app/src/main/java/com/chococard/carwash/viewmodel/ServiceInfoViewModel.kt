package com.chococard.carwash.viewmodel

import com.chococard.carwash.repositories.HeaderRepository

class ServiceInfoViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    val getDbJob = repository.getJob()

}
