package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.SwitchSystemRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.data.networks.response.HomeScoreResponse
import com.chococard.carwash.data.sharedpreference.SharedPreference
import com.chococard.carwash.repositories.HeaderRepositoryV2
import com.chococard.carwash.util.FlagConstant
import kotlinx.coroutines.launch

data class HomeViewState(
    val loading: Boolean = false
)

class HomeViewModel(
    private val repository: HeaderRepositoryV2,
    private val sharedPreference: SharedPreference
) : BaseViewModelV2<HomeViewState>(HomeViewState()) {

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
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callSwitchSystem(SwitchSystemRequest(flag))
                switchSystemResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

    fun initializeSwitchButton() {
        _switchFlag.value = sharedPreference.switchFlag
    }

    fun callHomeScore() {
        launch {
            try {
                setState { copy(loading = true) }
                homeScoreResponse.value = repository.callHomeScore()
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

}
