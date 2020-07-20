package com.chococard.carwash.signalr

import android.util.Log
import com.chococard.carwash.data.networks.BASE_URL
import com.chococard.carwash.data.networks.response.TimerResponse
import com.google.gson.Gson
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import com.microsoft.signalr.TransportEnum
import kotlinx.coroutines.TimeoutCancellationException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

class SignalRTimeHub(listener: SignalRListener) {

    private val hubConnection: HubConnection = HubConnectionBuilder
        .create("${BASE_URL}carwash/signalr/timehub")
        .withTransport(TransportEnum.LONG_POLLING)
        .build()

    init {
        hubConnection.on("ReceiveTime", {
            try {
                val fromJson = Gson().fromJson(it, TimerResponse::class.java)
                listener.onReceive(fromJson)
            } catch (e: TimeoutException) {
                Log.d(TAG, "SignalRTimeHub : TimeoutException")
                startSignalR()
            } catch (e: SocketTimeoutException) {
                Log.d(TAG, "SignalRTimeHub : SocketTimeoutException")
                startSignalR()
            } catch (e: TimeoutCancellationException) {
                Log.d(TAG, "SignalRTimeHub : TimeoutCancellationException")
                startSignalR()
            } catch (e: NullPointerException) {
                Log.d(TAG, "SignalRTimeHub : NullPointerException")
                startSignalR()
            } catch (e: Exception) {
                Log.d(TAG, "SignalRTimeHub : Exception")
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
        fun onReceive(timer: TimerResponse)
    }

    companion object {
        private const val TAG = "SignalR"
    }

}
