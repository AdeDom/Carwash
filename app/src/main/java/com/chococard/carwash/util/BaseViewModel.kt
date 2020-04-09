package com.chococard.carwash.util

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {

    lateinit var job: Job

    var exception: ((String) -> Unit)? = null

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

}
