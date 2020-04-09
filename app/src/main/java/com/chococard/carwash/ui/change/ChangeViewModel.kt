package com.chococard.carwash.ui.change

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.repositories.ChangeRepository
import com.chococard.carwash.util.ApiException
import com.chococard.carwash.util.BaseViewModel
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.NoInternetException
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

class ChangeViewModel(private val repository: ChangeRepository): BaseViewModel() {

    private val _upload = MutableLiveData<ResponseBody>()
    val upload: LiveData<ResponseBody>
        get() = _upload

    fun uploadImageFile(file: MultipartBody.Part, description: RequestBody) {
        job = Coroutines.main {
            try {
                val response = repository.uploadImageFile(file, description)
                _upload.value = response
            } catch (e: ApiException) {
                exception?.invoke(e.message!!)
            } catch (e: NoInternetException) {
                exception?.invoke(e.message!!)
            }
        }
    }

}
