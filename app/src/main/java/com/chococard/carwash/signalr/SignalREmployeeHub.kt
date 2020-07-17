package com.chococard.carwash.signalr

import android.util.Log
import com.chococard.carwash.data.networks.BASE_URL
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.TimeoutCancellationException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

class SignalREmployeeHub(listener: SignalRListener) {

    private val hubConnection: HubConnection = HubConnectionBuilder
        .create("${BASE_URL}carwash/signalr/employeehub")
        .build()

    init {
        hubConnection.on("ReceiveEmployee", {
            try {
                listener.onReceive(it)
            } catch (e: TimeoutException) {
                Log.d(TAG, "SignalREmployeeHub : TimeoutException")
                startSignalR()
            } catch (e: SocketTimeoutException) {
                Log.d(TAG, "SignalREmployeeHub : SocketTimeoutException")
                startSignalR()
            } catch (e: TimeoutCancellationException) {
                Log.d(TAG, "SignalREmployeeHub : TimeoutCancellationException")
                startSignalR()
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

    companion object {
        private const val TAG = "SignalR"
    }

}
