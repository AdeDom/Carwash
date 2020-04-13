package com.chococard.carwash.ui.main.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.HistoryResponse
import com.chococard.carwash.data.repositories.MainRepository
import com.chococard.carwash.util.base.BaseViewModel

class HistoryViewModel(private val repository: MainRepository) : BaseViewModel(repository) {

    private val _history = MutableLiveData<HistoryResponse>()
    val history: LiveData<HistoryResponse>
        get() = _history

    fun fetchHistory(dateBegin: String = "", dateEnd: String = "") = launch {
        _history.value = repository.fetchHistory(dateBegin, dateEnd)
    }

}
