package com.chococard.carwash.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.UserResponse
import com.chococard.carwash.data.repositories.UserRepository
import com.chococard.carwash.util.ApiException
import com.chococard.carwash.util.BaseViewModel
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.NoInternetException

class MainViewModel(private val repository: UserRepository) : BaseViewModel() {

    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse>
        get() = _user

    fun fetchUser() {
        job = Coroutines.main {
            try {
                _user.value = repository.fetchUser()
            } catch (e: ApiException) {
                exception?.invoke(e.message!!)
            } catch (e: NoInternetException) {
                exception?.invoke(e.message!!)
            }
        }
    }

}
