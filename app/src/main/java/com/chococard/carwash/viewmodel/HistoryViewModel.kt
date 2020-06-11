package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.HistoryResponse
import com.chococard.carwash.repositories.HeaderRepository

class HistoryViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val historyResponse = MutableLiveData<HistoryResponse>()
    val getHistory: LiveData<HistoryResponse>
        get() = historyResponse

    fun callFetchHistory(dateBegin: Long = 0, dateEnd: Long = 0) = launchCallApi(
        request = { repository.callFetchHistory(dateBegin, dateEnd) },
        response = { historyResponse.value = it }
    )

}
