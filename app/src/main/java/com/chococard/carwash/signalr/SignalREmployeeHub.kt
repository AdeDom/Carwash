package com.chococard.carwash.signalr

import android.util.Log
import com.chococard.carwash.data.networks.BASE_URL
import com.chococard.carwash.data.networks.response.JobResponse
import com.google.gson.Gson
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import com.microsoft.signalr.TransportEnum
import kotlinx.coroutines.TimeoutCancellationException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

class SignalREmployeeHub(employerId: Int, listener: SignalRListener) {

    private val hubConnection: HubConnection = HubConnectionBuilder
        .create("${BASE_URL}carwash/signalr/employeehub")
        .withTransport(TransportEnum.LONG_POLLING)
        .build()

    init {
        hubConnection.on("ReceiveEmployee$employerId", {
            try {
                val fromJson = Gson().fromJson(it, JobResponse::class.java)
                listener.onReceive(fromJson)
            } catch (e: TimeoutException) {
                Log.d(TAG, "SignalREmployeeHub : TimeoutException")
                startSignalR()
            } catch (e: SocketTimeoutException) {
                Log.d(TAG, "SignalREmployeeHub : SocketTimeoutException")
                startSignalR()
            } catch (e: TimeoutCancellationException) {
                Log.d(TAG, "SignalREmployeeHub : TimeoutCancellationException")
                startSignalR()
            } catch (e: NullPointerException) {
                Log.d(TAG, "SignalREmployeeHub : NullPointerException")
                startSignalR()
            } catch (e: Exception) {
                Log.d(TAG, "SignalREmployeeHub : Exception")
                startSignalR()
            }
        }, String::class.java)

        startSignalR()
    }

    private fun startSignalR() {
        if (hubConnection.connectionState == HubConnectionState.DISCONNECTED)
            hubConnection.start()
    }

    interface SignalRListener {
        fun onReceive(job: JobResponse)
    }

    companion object {
        private const val TAG = "SignalR"
    }

}
