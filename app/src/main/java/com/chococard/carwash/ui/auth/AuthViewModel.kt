package com.chococard.carwash.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chococard.carwash.data.networks.response.SignInResponse
import com.chococard.carwash.data.networks.response.SignUpResponse
import com.chococard.carwash.data.repositories.AuthRepository
import com.chococard.carwash.util.Coroutines
import kotlinx.coroutines.Job

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private lateinit var job: Job

    private val _signUpResponse = MutableLiveData<SignUpResponse>()
    val signUpResponse: LiveData<SignUpResponse>
        get() = _signUpResponse

    private val _signInResponse = MutableLiveData<SignInResponse>()
    val signInResponse: LiveData<SignInResponse>
        get() = _signInResponse

    fun signUp(
        name: String,
        username: String,
        password: String,
        identityCard: String,
        phone: String
    ) {
        job = Coroutines.main {
            val response = repository.signUp(name, username, password, identityCard, phone)
            _signUpResponse.value = response
        }
    }

    fun signIn(username: String, password: String){
        job = Coroutines.main {
            val response = repository.signIn(username, password)
            _signInResponse.value = response
        }
    }

    fun isIdentityCard(identityCard: String): Boolean {
        val ic = identityCard.substring(0, 12)
        var sumIdentityCard = 0
        ic.forEachIndexed { index, c ->
            sumIdentityCard += (13 - index) * c.toString().toInt()
        }
        val digit = (11 - (sumIdentityCard % 11)) % 10
        val realIdentityCard = ic + digit
        return realIdentityCard != identityCard
    }

    fun isTelephoneNumber(phone: String): Boolean {
        val tel = listOf("06", "08", "09")
        val p = phone.substring(0, 2)
        var isPhone = true
        tel.forEach {
            if (it == p) {
                isPhone = false
                return@forEach
            }
        }
        return isPhone
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

}