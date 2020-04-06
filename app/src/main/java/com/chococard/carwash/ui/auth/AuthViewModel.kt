package com.chococard.carwash.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chococard.carwash.data.networks.response.SignInResponse
import com.chococard.carwash.data.networks.response.SignUpResponse
import com.chococard.carwash.data.repositories.AuthRepository
import com.chococard.carwash.util.ApiException
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.NoInternetException
import kotlinx.coroutines.Job
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private lateinit var job: Job

    var exception: ((String) -> Unit)? = null

    private val _signUpResponse = MutableLiveData<SignUpResponse>()
    val signUpResponse: LiveData<SignUpResponse>
        get() = _signUpResponse

    private val _signInResponse = MutableLiveData<SignInResponse>()
    val signInResponse: LiveData<SignInResponse>
        get() = _signInResponse

    private val _uploadResponse = MutableLiveData<ResponseBody>()
    val uploadResponse: LiveData<ResponseBody>
        get() = _uploadResponse

    fun signUp(
        name: String,
        username: String,
        password: String,
        identityCard: String,
        phone: String
    ) {
        job = Coroutines.main {
            try {
                val response = repository.signUp(name, username, password, identityCard, phone)
                _signUpResponse.value = response
            } catch (e: ApiException) {
                exception?.invoke(e.message!!)
            } catch (e: NoInternetException) {
                exception?.invoke(e.message!!)
            }
        }
    }

    fun signIn(username: String, password: String) {
        job = Coroutines.main {
            try {
                val response = repository.signIn(username, password)
                _signInResponse.value = response
            } catch (e: ApiException) {
                exception?.invoke(e.message!!)
            } catch (e: NoInternetException) {
                exception?.invoke(e.message!!)
            }
        }
    }

    fun uploadImageFile(file: MultipartBody.Part, description: RequestBody) {
        job = Coroutines.main {
            try {
                val response = repository.uploadImageFile(file, description)
                _uploadResponse.value = response
            } catch (e: ApiException) {
                exception?.invoke(e.message!!)
            } catch (e: NoInternetException) {
                exception?.invoke(e.message!!)
            }
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