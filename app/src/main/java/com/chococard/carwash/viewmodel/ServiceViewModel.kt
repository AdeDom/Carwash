package com.chococard.carwash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chococard.carwash.data.networks.request.DeleteImageServiceRequest
import com.chococard.carwash.data.networks.response.ServiceImageResponse
import com.chococard.carwash.repositories.HeaderRepositoryV2
import com.chococard.carwash.util.FlagConstant
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

data class ServiceViewState(
    val loading: Boolean = false,
    val loadingFrontBefore: Boolean = false,
    val loadingBackBefore: Boolean = false,
    val loadingLeftBefore: Boolean = false,
    val loadingRightBefore: Boolean = false,
    val loadingFrontAfter: Boolean = false,
    val loadingBackAfter: Boolean = false,
    val loadingLeftAfter: Boolean = false,
    val loadingRightAfter: Boolean = false,
    val loadingOtherImage: Boolean = false
)

class ServiceViewModel(
    private val repository: HeaderRepositoryV2
) : BaseViewModelV2<ServiceViewState>(ServiceViewState()) {

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
        statusService: Int
    ) {
        launch {
            try {
                showProgressBar(statusService)
                val response = repository.callUploadImageService(file, statusService)
                uploadImageServiceResponse.value = response
                hideProgressBar()
            } catch (e: Throwable) {
                hideProgressBar()
                setError(e)
            }
        }
    }

    private fun showProgressBar(statusService: Int) {
        when (statusService) {
            FlagConstant.STATUS_SERVICE_FRONT_BEFORE -> setState { copy(loadingFrontBefore = true) }
            FlagConstant.STATUS_SERVICE_BACK_BEFORE -> setState { copy(loadingBackBefore = true) }
            FlagConstant.STATUS_SERVICE_LEFT_BEFORE -> setState { copy(loadingLeftBefore = true) }
            FlagConstant.STATUS_SERVICE_RIGHT_BEFORE -> setState { copy(loadingRightBefore = true) }
            FlagConstant.STATUS_SERVICE_FRONT_AFTER -> setState { copy(loadingFrontAfter = true) }
            FlagConstant.STATUS_SERVICE_BACK_AFTER -> setState { copy(loadingBackAfter = true) }
            FlagConstant.STATUS_SERVICE_LEFT_AFTER -> setState { copy(loadingLeftAfter = true) }
            FlagConstant.STATUS_SERVICE_RIGHT_AFTER -> setState { copy(loadingRightAfter = true) }
            FlagConstant.STATUS_SERVICE_OTHER_IMAGE -> setState { copy(loadingOtherImage = true) }
        }
    }

    private fun hideProgressBar() {
        setState {
            copy(
                loading = false,
                loadingFrontBefore = false,
                loadingBackBefore = false,
                loadingLeftBefore = false,
                loadingRightBefore = false,
                loadingFrontAfter = false,
                loadingBackAfter = false,
                loadingLeftAfter = false,
                loadingRightAfter = false,
                loadingOtherImage = false
            )
        }
    }

    fun callFetchImageService() {
        launch {
            try {
                setState { copy(loading = true) }
                val response = repository.callFetchImageService()
                setValueValidateMaximumOtherImage(
                    response.serviceImage?.otherImageService?.size ?: 0
                )
                imageServiceResponse.value = response
                setState { copy(loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
                setError(e)
            }
        }
    }

    fun callDeleteServiceImage(statusService: Int) {
        launch {
            try {
                showProgressBar(statusService)
                val request = DeleteImageServiceRequest(statusService)
                val response = repository.callDeleteServiceImage(request)
                deleteServiceImageResponse.value = response
                hideProgressBar()
            } catch (e: Throwable) {
                hideProgressBar()
                setError(e)
            }
        }
    }

    fun callDeleteServiceOtherImage(deleteImageService: DeleteImageServiceRequest) {
        setValueValidateMaximumOtherImage(getValueValidateMaximumOtherImage().minus(1))
        launch {
            try {
                setState { copy(loadingOtherImage = true) }
                val response = repository.callDeleteServiceOtherImage(deleteImageService)
                deleteServiceOtherImageResponse.value = response
                setState { copy(loadingOtherImage = false) }
            } catch (e: Throwable) {
                setState { copy(loadingOtherImage = false) }
                setError(e)
            }
        }
    }

    fun setValueValidateMaximumOtherImage(count: Int) {
        validateMaximumOtherImage.value = count
    }

    fun getValueValidateMaximumOtherImage() = validateMaximumOtherImage.value ?: 0

}
