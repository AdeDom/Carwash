package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.SwitchSystemRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.HomeScoreResponse
import com.chococard.carwash.data.sharedpreference.SharedPreference
import com.chococard.carwash.repositories.HeaderRepository
import com.chococard.carwash.util.FlagConstant

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

    private val _switchFlag = MutableLiveData<Int>()
    val switchFlag: LiveData<Int>
        get() = _switchFlag

    fun callSwitchSystem() {
        val flag = if (sharedPreference.switchFlag == FlagConstant.SWITCH_OFF)
            FlagConstant.SWITCH_ON
        else
            FlagConstant.SWITCH_OFF
        _switchFlag.value = flag
        sharedPreference.switchFlag = flag
        launchCallApi(
            request = { repository.callSwitchSystem(SwitchSystemRequest(flag)) },
            response = { switchSystemResponse.value = it }
        )
    }

    fun initializeSwitchButton() {
        _switchFlag.value = sharedPreference.switchFlag
    }

    fun callHomeScore() = launchCallApi(
        request = { repository.callHomeScore() },
        response = { homeScoreResponse.value = it }
    )

}
