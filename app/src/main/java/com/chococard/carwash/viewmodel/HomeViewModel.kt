package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.SwitchSystemRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.HomeScoreResponse
import com.chococard.carwash.repositories.HeaderRepository

class HomeViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    val getDbUser = repository.getUser()

    private val switch = MutableLiveData<BaseResponse>()
    val callSwitchSystem: LiveData<BaseResponse>
        get() = switch

    private val homeScore = MutableLiveData<HomeScoreResponse>()
    val getHomeScore: LiveData<HomeScoreResponse>
        get() = homeScore

    fun callSwitchSystem(switchSystem: SwitchSystemRequest) = launchCallApi(
        request = { repository.callSwitchSystem(switchSystem) },
        response = { switch.value = it }
    )

    fun callHomeScore() = launchCallApi(
        request = { repository.callHomeScore() },
        response = { homeScore.value = it }
    )

}
