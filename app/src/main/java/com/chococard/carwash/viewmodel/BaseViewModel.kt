package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chococard.carwash.util.ApiException
import com.chococard.carwash.util.NoInternetException
import kotlinx.coroutines.*
import java.io.EOFException
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, err ->
        error.value = err.message
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main + exceptionHandler

    private val error = MutableLiveData<String>()
    val getError: LiveData<String>
        get() = error

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancel()
    }

    protected fun <T : Any> launchCallApi(request: suspend () -> T?,response: suspend (T?) -> Unit) {
        launch {
            try {
                val data = CoroutineScope(Dispatchers.IO).async {
                    request.invoke()
                }.await()
                response(data)
            } catch (e: ApiException) {
                error.value = e.message
            } catch (e: NoInternetException) {
                error.value = e.message
            } catch (e: EOFException) {
                error.value = "Server has a problem"
            } catch (e: SocketTimeoutException) {
                error.value = e.message
            } catch (e:Throwable) {
                error.value = e.message
            }
        }
    }

}
