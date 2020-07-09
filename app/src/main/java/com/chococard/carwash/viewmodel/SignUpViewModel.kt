package com.chococard.carwash.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.ConnectionRepository
import com.chococard.carwash.util.extension.isVerifyIdentityCard
import com.chococard.carwash.util.extension.isVerifyPhone

class SignUpViewModel(private val repository: ConnectionRepository) : BaseViewModel() {

    private val signUpResponse = MutableLiveData<BaseResponse>()
    val getSignUp: LiveData<BaseResponse>
        get() = signUpResponse

    private val fileUri = MutableLiveData<Uri>()
    val getFileUri: LiveData<Uri>
        get() = fileUri

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun callSignUp(
        username: String,
        password: String,
        rePassword: String,
        fullName: String,
        identityCard: String,
        phone: String
    ) {
        when {
            username.isEmpty() ->
                _errorMessage.value = "Please enter username"
            username.length < 4 ->
                _errorMessage.value = "Please enter an username of at least 4 characters"
            password.isEmpty() ->
                _errorMessage.value = "Please enter password"
            password.length < 8 ->
                _errorMessage.value = "Please enter an password of at least 8 characters"
            rePassword.isEmpty() ->
                _errorMessage.value = "Please enter re-password"
            rePassword.length < 8 ->
                _errorMessage.value = "Please enter an re-password of at least 8 characters"
            password != rePassword ->
                _errorMessage.value = "Please enter the password to match"
            fullName.isEmpty() ->
                _errorMessage.value = "Please enter full name"
            identityCard.isEmpty() ->
                _errorMessage.value = "Please enter identity card"
            identityCard.length != 13 ->
                _errorMessage.value = "Please enter a total of 13 characters"
            identityCard.isVerifyIdentityCard() ->
                _errorMessage.value = "Please enter the correct Identity card"
            phone.isEmpty() ->
                _errorMessage.value = "Please enter phone"
            phone.length != 10 ->
                _errorMessage.value = "Please enter a total of 10 characters"
            phone.isVerifyPhone() ->
                _errorMessage.value = "Please enter the correct phone number"
            fileUri.value == null ->
                _errorMessage.value = "Please select a profile picture"
            else -> {
                launchCallApi(
                    request = {
                        repository.callSignUp(
                            username,
                            password,
                            fullName,
                            identityCard,
                            phone,
                            fileUri.value!!
                        )
                    },
                    response = { signUpResponse.value = it }
                )
            }
        }
    }

    fun setValueFileUri(file: Uri?) {
        fileUri.value = file
    }

}
