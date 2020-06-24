package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.DeleteImageServiceRequest
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

    private val deleteServiceImageResponse = MutableLiveData<ServiceImageResponse>()
    val getDeleteServiceImage: LiveData<ServiceImageResponse>
        get() = deleteServiceImageResponse

    private val deleteServiceOtherImageResponse = MutableLiveData<ServiceImageResponse>()
    val getDeleteServiceOtherImage: LiveData<ServiceImageResponse>
        get() = deleteServiceOtherImageResponse

    private val validateMaximumOtherImage = MutableLiveData<Int>()
    val getValidateMaximumOtherImage: LiveData<Int>
        get() = validateMaximumOtherImage

    fun callUploadImageService(
        file: MultipartBody.Part,
        statusService: RequestBody?
    ) = launchCallApi(
        request = { repository.callUploadImageService(file, statusService) },
        response = { uploadImageServiceResponse.value = it }
    )

    fun callFetchImageService() = launchCallApi(
        request = { repository.callFetchImageService() },
        response = {
            setValueValidateMaximumOtherImage(it?.serviceImage?.otherImageService?.size ?: 0)
            imageServiceResponse.value = it
        }
    )

    fun callDeleteServiceImage(deleteImageService: DeleteImageServiceRequest) = launchCallApi(
        request = { repository.callDeleteServiceImage(deleteImageService) },
        response = { deleteServiceImageResponse.value = it }
    )

    fun callDeleteServiceOtherImage(deleteImageService: DeleteImageServiceRequest) = launchCallApi(
        request = { repository.callDeleteServiceOtherImage(deleteImageService) },
        response = { deleteServiceOtherImageResponse.value = it }
    )

    fun setValueValidateMaximumOtherImage(count: Int) {
        validateMaximumOtherImage.value = count
    }

    fun getValueValidateMaximumOtherImage() = validateMaximumOtherImage.value ?: 0

}
