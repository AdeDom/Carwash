package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.SetNavigationRequest
import com.chococard.carwash.data.networks.response.NavigationResponse
import com.chococard.carwash.repositories.HeaderRepository

class NavigationViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val navigationResponse = MutableLiveData<NavigationResponse>()
    val getNavigationResponse: LiveData<NavigationResponse>
        get() = navigationResponse

    val getDbUser = repository.getUser()

    val getDbJob = repository.getJob()

    fun callSetNavigation(setNavigation: SetNavigationRequest) = launchCallApi(
        request = { repository.callSetNavigation(setNavigation) },
        response = { navigationResponse.value = it }
    )

}
