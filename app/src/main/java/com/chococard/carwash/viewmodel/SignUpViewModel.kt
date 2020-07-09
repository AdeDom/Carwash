package com.chococard.carwash.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.ConnectionRepository

class SignUpViewModel(private val repository: ConnectionRepository) : BaseViewModel() {

    private val signUpResponse = MutableLiveData<BaseResponse>()
    val getSignUp: LiveData<BaseResponse>
        get() = signUpResponse

    private val fileUri = MutableLiveData<Uri>()
    val getFileUri: LiveData<Uri>
        get() = fileUri

    fun callSignUp(
        username: String,
        password: String,
        fullName: String,
        identityCard: String,
        phone: String
    ) {
        if (fileUri.value != null) {
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

    fun isValueFileUri() = fileUri.value == null

    fun setValueFileUri(file: Uri?) {
        fileUri.value = file
    }

}
