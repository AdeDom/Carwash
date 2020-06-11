package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.response.ServiceImageResponse
import com.chococard.carwash.repositories.HeaderRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ServiceViewModel(private val repository: HeaderRepository) : BaseViewModel() {

    private val uploadImageServiceResponse = MutableLiveData<ServiceImageResponse>()
    val getUploadImageService: LiveData<ServiceImageResponse>
        get() = uploadImageServiceResponse

    private val imageServiceResponse = MutableLiveData<ServiceImageResponse>()
    val getImageService: LiveData<ServiceImageResponse>
        get() = imageServiceResponse

    fun callUploadImageService(
        file: MultipartBody.Part,
        statusService: RequestBody
    ) = launchCallApi(
        request = { repository.callUploadImageService(file, statusService) },
        response = { uploadImageServiceResponse.value = it }
    )

    fun callFetchImageService() = launchCallApi(
        request = { repository.callFetchImageService() },
        response = { imageServiceResponse.value = it }
    )

}
