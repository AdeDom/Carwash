package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.db.entities.User
import com.chococard.carwash.data.networks.request.ChangePhoneRequest
import com.chococard.carwash.data.networks.request.ValidatePhoneRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository
import com.chococard.carwash.util.extension.isVerifyPhone
import okhttp3.MultipartBody

class ChangeProfileViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    val getDbUser = repository.getUser()

    private var user: User? = null

    private val changeImageProfileResponse = MutableLiveData<BaseResponse>()
    val getChangeImageProfile: LiveData<BaseResponse>
        get() = changeImageProfileResponse

    private val changePhoneResponse = MutableLiveData<BaseResponse>()
    val getChangePhone: LiveData<BaseResponse>
        get() = changePhoneResponse

    private val logoutResponse = MutableLiveData<BaseResponse>()
    val getLogout: LiveData<BaseResponse>
        get() = logoutResponse

    private val validatePhoneResponse = MutableLiveData<BaseResponse>()
    val getValidatePhone: LiveData<BaseResponse>
        get() = validatePhoneResponse

    private val _validatePhone = MutableLiveData<Boolean>()
    val validatePhone: LiveData<Boolean>
        get() = _validatePhone

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun callChangeImageProfile(file: MultipartBody.Part) = launchCallApi(
        request = { repository.callChangeImageProfile(file) },
        response = { changeImageProfileResponse.value = it }
    )

    fun callChangePhone(changePhone: ChangePhoneRequest) = launchCallApi(
        request = { repository.callChangePhone(changePhone) },
        response = { changePhoneResponse.value = it }
    )

    fun callLogout() = launchCallApi(
        request = { repository.callLogout() },
        response = { logoutResponse.value = it }
    )

    fun callValidatePhone(phoneNumber: String) {
        when {
            user == null ->
                _errorMessage.value = "User is null"
            phoneNumber.isEmpty() ->
                _errorMessage.value = "Please enter phone"
            user?.phone == phoneNumber ->
                _errorMessage.value = "Please enter a new phone number"
            phoneNumber.length != 10 ->
                _errorMessage.value = "Please enter a total of 10 characters"
            phoneNumber.isVerifyPhone() ->
                _errorMessage.value = "Please enter the correct phone number"
            else -> {
                val validatePhone = ValidatePhoneRequest(phoneNumber)
                launchCallApi(
                    request = { repository.callValidatePhone(validatePhone) },
                    response = { validatePhoneResponse.value = it }
                )
            }
        }
    }

    fun setValueUser(user: User) {
        this.user = user
    }

    fun setValueValidatePhone(phoneNumber: String) {
        when {
            user == null -> _validatePhone.value = false
            user?.phone == phoneNumber -> _validatePhone.value = false
            phoneNumber.length != 10 -> _validatePhone.value = false
            phoneNumber.isVerifyPhone() -> _validatePhone.value = false
            else -> _validatePhone.value = true
        }
    }

}
