package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.db.entities.UserInfo
import com.chococard.carwash.data.networks.request.ChangePhoneRequest
import com.chococard.carwash.data.networks.request.ValidatePhoneRequest
import com.chococard.carwash.data.networks.response.BaseResponse
import com.chococard.carwash.repositories.HeaderRepository
import com.chococard.carwash.util.extension.isVerifyPhone
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

data class ChangeProfileViewState(
    val loading: Boolean = false
)

class ChangeProfileViewModel(
    private val repository: HeaderRepository
) : BaseViewModel<ChangeProfileViewState>(ChangeProfileViewState()) {

    val getDbUser = repository.getDbUserInfoLiveData()

    private var user: UserInfo? = null

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

    fun callChangeImageProfile(file: MultipartBody.Part) {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callChangeImageProfile(file)
                changeImageProfileResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

    fun callChangePhone(changePhone: ChangePhoneRequest) {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callChangePhone(changePhone)
                changePhoneResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

    fun callLogout() {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callLogout()
                logoutResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

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
                launch {
                    try {
                        setState { copy(loading = true) }
                        val request = ValidatePhoneRequest(phoneNumber)
                        val response = repository.callValidatePhone(request)
                        validatePhoneResponse.value = response
                        setState { copy(loading = false) }
                    } catch (e: Throwable) {
                        setState { copy(loading = false) }
                        setError(e)
                    }
                }
            }
        }
    }

    fun setValueUser(user: UserInfo) {
        this.user = user
    }

    fun setValueValidatePhone(phoneNumber: String) {
        when {
            user == null -> _validatePhone.value = false
            phoneNumber.isEmpty() -> _validatePhone.value = false
            user?.phone == phoneNumber -> _validatePhone.value = false
            phoneNumber.length != 10 -> _validatePhone.value = false
            phoneNumber.isVerifyPhone() -> _validatePhone.value = false
            else -> _validatePhone.value = true
        }
    }

}
