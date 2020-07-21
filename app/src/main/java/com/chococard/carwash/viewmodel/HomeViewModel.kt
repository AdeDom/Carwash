package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.SwitchSystemRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.HomeScoreResponse
import com.chococard.carwash.data.sharedpreference.SharedPreference
import com.chococard.carwash.repositories.HeaderRepository

class HomeViewModel(
    private val repository: HeaderRepository,
    private val sharedPreference: SharedPreference
) : BaseViewModel() {

    val getDbUser = repository.getUser()

    private val switchSystemResponse = MutableLiveData<BaseResponse>()
    val callSwitchSystem: LiveData<BaseResponse>
        get() = switchSystemResponse

    private val homeScoreResponse = MutableLiveData<HomeScoreResponse>()
    val getHomeScore: LiveData<HomeScoreResponse>
        get() = homeScoreResponse

    fun callSwitchSystem(switchSystem: SwitchSystemRequest) {
        sharedPreference.switchFlag = switchSystem.state ?: 0
        launchCallApi(
            request = { repository.callSwitchSystem(switchSystem) },
            response = { switchSystemResponse.value = it }
        )
    }

    fun getSharedPreference() = sharedPreference.switchFlag

    fun callHomeScore() = launchCallApi(
        request = { repository.callHomeScore() },
        response = { homeScoreResponse.value = it }
    )

}
