package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModelV2<S : Any>(private val initialState: S) : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, err ->
        setError(err)
    }
    private val _state = MutableLiveData<S>().apply { value = initialState }
    private val _error = MutableLiveData<Throwable>()
    private val _attachFirstTime = MutableLiveData<Unit>().apply { value = Unit }

    val state: LiveData<S> = _state
    val error: LiveData<Throwable> = _error
    val attachFirstTime: LiveData<Unit> = _attachFirstTime

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main + exceptionHandler

    override fun onCleared() {
        coroutineContext.cancel()
        super.onCleared()
    }

    protected fun setState(reducer: S.() -> S) {
        _state.value = (_state.value ?: initialState).reducer()
    }

    protected fun setError(throwable: Throwable) {
        _error.value = throwable
    }

}
