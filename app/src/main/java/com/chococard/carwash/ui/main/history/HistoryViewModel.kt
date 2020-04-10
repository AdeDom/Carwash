package com.chococard.carwash.ui.main.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.HistoryResponse
import com.chococard.carwash.data.repositories.HistoryRepository
import com.chococard.carwash.util.BaseViewModel

class HistoryViewModel(private val repository: HistoryRepository) : BaseViewModel(repository) {

    private val _history = MutableLiveData<HistoryResponse>()
    val history: LiveData<HistoryResponse>
        get() = _history

    fun fetchHistory(dateBegin: String, dateEnd: String) = launch {
        _history.value = repository.fetchHistory(dateBegin, dateEnd)
    }

}
