package com.chococard.carwash.util.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chococard.carwash.data.repositories.BaseRepository
import com.chococard.carwash.util.ApiException
import com.chococard.carwash.util.NoInternetException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

abstract class BaseViewModel(private val repository: BaseRepository) : ViewModel() {

    lateinit var job: Job

    private val _exception = MutableLiveData<String>()
    val exception: LiveData<String>
        get() = _exception

    private val _upload = MutableLiveData<ResponseBody>()
    val upload: LiveData<ResponseBody>
        get() = _upload

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

    fun launch(work: suspend (() -> Unit)) {
        job = CoroutineScope(Dispatchers.Main).launch {
            try {
                work.invoke()
            } catch (e: ApiException) {
                _exception.value = e.message
            } catch (e: NoInternetException) {
                _exception.value = e.message
            }
        }
    }

    fun uploadImageFile(file: MultipartBody.Part, description: RequestBody) = launch {
        _upload.value = repository.uploadImageFile(file, description)
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

}
