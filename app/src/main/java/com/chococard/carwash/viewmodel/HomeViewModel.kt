package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.SwitchSystemRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository

class HomeViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val switch = MutableLiveData<BaseResponse>()
    val callSwitchSystem: LiveData<BaseResponse>
        get() = switch

    val getDbUser = repository.getUser()

    fun callSwitchSystem(switchSystem: SwitchSystemRequest) = launchCallApi(
        request = { repository.callSwitchSystem(switchSystem) },
        response = { switch.value = it }
    )

}
