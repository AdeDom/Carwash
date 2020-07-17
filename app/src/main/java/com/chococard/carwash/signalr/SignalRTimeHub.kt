package com.chococard.carwash.signalr

import android.util.Log
import com.chococard.carwash.data.networks.BASE_URL
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.TimeoutCancellationException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

class SignalRTimeHub(listener: SignalRListener) {

    private val hubConnection: HubConnection = HubConnectionBuilder
        .create("${BASE_URL}carwash/signalr/timehub")
        .build()

    init {
        hubConnection.on("ReceiveTime", {
            try {
                listener.onReceive(it)
            } catch (e: TimeoutException) {
                Log.d(TAG, "SignalRTimeHub : TimeoutException")
                startSignalR()
            } catch (e: SocketTimeoutException) {
                Log.d(TAG, "SignalRTimeHub : SocketTimeoutException")
                startSignalR()
            } catch (e: TimeoutCancellationException) {
                Log.d(TAG, "SignalRTimeHub : TimeoutCancellationException")
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
