package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chococard.carwash.data.networks.response.TimerResponse
import com.chococard.carwash.signalr.SignalRTimeHub

class TimerJobViewModel : ViewModel(), SignalRTimeHub.SignalRListener {

    private var signalRTimeHub = SignalRTimeHub(this)

    private val timerJobQuestion = MutableLiveData<TimerResponse>()
    val getTimerJobQuestion: LiveData<TimerResponse>
        get() = timerJobQuestion

    override fun onReceive(timer: TimerResponse) = timerJobQuestion.postValue(timer)

    fun startSignalRTimeHub() = signalRTimeHub.startSignalR()

    fun stopSignalRTimeHub() = signalRTimeHub.stopSignalR()

}
