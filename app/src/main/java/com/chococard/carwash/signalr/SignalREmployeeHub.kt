package com.chococard.carwash.signalr

import com.chococard.carwash.data.networks.BASE_URL
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import java.net.SocketTimeoutException

class SignalREmployeeHub(listener: SignalRListener) {

    private val hubConnection: HubConnection = HubConnectionBuilder
        .create("${BASE_URL}carwash/signalr/employeehub")
        .build()

    init {
        hubConnection.on("ReceiveEmployee", {
            try {
                listener.onReceive(it)
            } catch (e: SocketTimeoutException) {
            }
        }, String::class.java)
    }

    fun startSignalR() {
        if (hubConnection.connectionState == HubConnectionState.DISCONNECTED)
            hubConnection.start()
    }

    fun stopSignalR() {
        if (hubConnection.connectionState == HubConnectionState.CONNECTED)
            hubConnection.stop()
    }

    interface SignalRListener {
        fun onReceive(data: String)
    }

}
