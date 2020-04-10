package com.chococard.carwash.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.UserResponse
import com.chococard.carwash.data.repositories.UserRepository
import com.chococard.carwash.util.BaseViewModel

class MainViewModel(private val repository: UserRepository) : BaseViewModel(repository) {

    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse>
        get() = _user

    fun fetchUser() = launch {
        _user.value = repository.fetchUser()
    }

}
