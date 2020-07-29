package com.chococard.carwash.viewmodel

import com.chococard.carwash.data.models.History
import com.chococard.carwash.repositories.HeaderRepository
import kotlinx.coroutines.launch

data class HistoryViewState(
    val histories: List<History>? = emptyList(),
    val loading: Boolean = false
)

class HistoryViewModel(
    private val repository: HeaderRepository
) : BaseViewModel<HistoryViewState>(HistoryViewState()) {

    fun callFetchHistory(dateBegin: Long = 0, dateEnd: Long = 0) {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callFetchHistory(dateBegin, dateEnd)
                setState { copy(loading = false, histories = response.histories) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

}
