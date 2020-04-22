package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.HistoryResponse
import com.chococard.carwash.repositories.BaseRepository

class HistoryViewModel(private val repository: BaseRepository) : BaseViewModel() {

    private val history = MutableLiveData<HistoryResponse>()
    val getHistory: LiveData<HistoryResponse>
        get() = history

    fun callFetchHistory(dateBegin: String = "", dateEnd: String = "") = launch {
        history.value = repository.callFetchHistory(dateBegin, dateEnd)
    }

}
