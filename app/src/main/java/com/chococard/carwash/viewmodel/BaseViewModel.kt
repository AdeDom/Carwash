package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chococard.carwash.util.ApiException
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.NoInternetException
import kotlinx.coroutines.Job

//TODO implement CoroutineScope
abstract class BaseViewModel : ViewModel() {

    private lateinit var job: Job

    private val error = MutableLiveData<String>()
    val getError: LiveData<String>
        get() = error

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

    fun launch(work: suspend (() -> Unit)) {
        job = Coroutines.main {
            try {
                work.invoke()
            } catch (e: ApiException) {
                error.value = e.message
            } catch (e: NoInternetException) {
                error.value = e.message
            }
        }
    }

}
