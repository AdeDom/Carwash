package com.chococard.carwash.viewmodel

import com.chococard.carwash.data.networks.request.SwitchSystemRequest
import com.chococard.carwash.data.networks.response.HomeScoreResponse
import com.chococard.carwash.data.sharedpreference.SharedPreference
import com.chococard.carwash.repositories.HeaderRepository
import com.chococard.carwash.util.FlagConstant
import kotlinx.coroutines.launch

data class HomeViewState(
    val homeScore: HomeScoreResponse = HomeScoreResponse(),
    val switchSystem: Int = 0,
    val loading: Boolean = false
)

class HomeViewModel(
    private val repository: HeaderRepository,
    private val sharedPreference: SharedPreference
) : BaseViewModel<HomeViewState>(HomeViewState()) {

    val getDbUserLiveData = repository.getDbUserLiveData()

    fun initialize() {
        setState { copy(switchSystem = sharedPreference.switchFlag) }
    }

    fun callSwitchSystem() {
        val flag = if (sharedPreference.switchFlag == FlagConstant.SWITCH_OFF)
            FlagConstant.SWITCH_ON
        else
            FlagConstant.SWITCH_OFF
        sharedPreference.switchFlag = flag
        setState { copy(switchSystem = flag) }
        launch {
            try {
                setState { copy(loading = true) }
                repository.callSwitchSystem(SwitchSystemRequest(flag))
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

    fun callHomeScore() {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callHomeScore()
                sharedPreference.switchFlag = response.switchFlag ?: 0
                setState {
                    copy(
                        loading = false,
                        homeScore = response,
                        switchSystem = sharedPreference.switchFlag
                    )
                }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

}
