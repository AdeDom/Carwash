package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository

class HistoryDetailViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val logoutResponse = MutableLiveData<BaseResponse>()
    val getLogout: LiveData<BaseResponse>
        get() = logoutResponse

    fun callLogout() = launchCallApi(
        request = { repository.callLogout() },
        response = { logoutResponse.value = it }
    )

}
