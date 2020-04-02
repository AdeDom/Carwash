package com.chococard.carwash.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chococard.carwash.data.networks.response.SignUpResponse
import com.chococard.carwash.data.repositories.AuthRepository
import com.chococard.carwash.util.Coroutines
import kotlinx.coroutines.Job

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private lateinit var job: Job

    private val _signUpResponse = MutableLiveData<SignUpResponse>()
    val signUpResponse: LiveData<SignUpResponse>
        get() = _signUpResponse

    fun signUp(
        name: String,
        username: String,
        password: String,
        identityCard: String,
        phone: String,
        image: String
    ) {
        job = Coroutines.main {
            val response = repository.signUp(name, username, password, identityCard, phone, image)
            _signUpResponse.value = response
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

}