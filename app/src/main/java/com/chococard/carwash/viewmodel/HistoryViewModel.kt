package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.HistoryResponse
import com.chococard.carwash.repositories.HeaderRepository

class HistoryViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val history = MutableLiveData<HistoryResponse>()
    val getHistory: LiveData<HistoryResponse>
        get() = history

    fun callFetchHistory(dateBegin: String = "", dateEnd: String = "") = launchCallApi(
        request = { repository.callFetchHistory(dateBegin, dateEnd) },
        response = { history.value = it }
    )

}
