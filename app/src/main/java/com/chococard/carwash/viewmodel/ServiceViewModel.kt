package com.chococard.carwash.viewmodel

import com.chococard.carwash.data.models.ServiceImage
import com.chococard.carwash.data.networks.request.DeleteImageServiceRequest
import com.chococard.carwash.repositories.HeaderRepository
import com.chococard.carwash.util.FlagConstant
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

data class ServiceViewState(
    val serviceImage: ServiceImage? = ServiceImage(),
    val imageId: Int = 0,
    val countOtherImage: Int = 0,
    val isValidMaximumOtherImage: Boolean = false,
    val isConfirmService: Boolean = false,
    val isImageFrontBefore: Boolean = false,
    val isImageBackBefore: Boolean = false,
    val isImageLeftBefore: Boolean = false,
    val isImageRightBefore: Boolean = false,
    val isImageFrontAfter: Boolean = false,
    val isImageBackAfter: Boolean = false,
    val isImageLeftAfter: Boolean = false,
    val isImageRightAfter: Boolean = false,
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
    private val repository: HeaderRepository
) : BaseViewModel<ServiceViewState>(ServiceViewState()) {

    fun callUploadImageService(
        file: MultipartBody.Part,
        statusService: Int
    ) {
        launch {
            try {
                showProgressBar(statusService)
                setState {
                    copy(
                        countOtherImage = countOtherImage.plus(1),
                        isValidMaximumOtherImage = countOtherImage < 5
                    )
                }
                val response = repository.callUploadImageService(
                    file,
                    statusService,
                    state.value?.imageId ?: 0
                )
                setServiceImageOnResponse(response.serviceImage)
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
                setState {
                    copy(
                        loading = false,
                        imageId = response.imageId ?: 0
                    )
                }
                setServiceImageOnResponse(response.serviceImage)
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
                setServiceImageOnResponse(response.serviceImage)
                hideProgressBar()
            } catch (e: Throwable) {
                hideProgressBar()
                setError(e)
            }
        }
    }

    fun callDeleteServiceOtherImage(deleteImageService: DeleteImageServiceRequest) {
        launch {
            try {
                setState {
                    copy(
                        loadingOtherImage = true,
                        countOtherImage = countOtherImage.minus(1),
                        isValidMaximumOtherImage = countOtherImage < 5
                    )
                }
                val response = repository.callDeleteServiceOtherImage(deleteImageService)
                setServiceImageOnResponse(response.serviceImage)
                setState { copy(loadingOtherImage = false) }
            } catch (e: Throwable) {
                setState { copy(loadingOtherImage = false) }
                setError(e)
            }
        }
    }

    private fun setServiceImageOnResponse(serviceImage: ServiceImage?) {
        setState {
            copy(
                serviceImage = serviceImage,
                countOtherImage = serviceImage?.otherImageService?.size ?: 0,
                isValidMaximumOtherImage = (serviceImage?.otherImageService?.size ?: 0) < 5,
                isConfirmService = serviceImage?.frontBefore.orEmpty().isNotBlank() &&
                        serviceImage?.backBefore.orEmpty().isNotBlank() &&
                        serviceImage?.leftBefore.orEmpty().isNotBlank() &&
                        serviceImage?.rightBefore.orEmpty().isNotBlank() &&
                        serviceImage?.frontAfter.orEmpty().isNotBlank() &&
                        serviceImage?.backAfter.orEmpty().isNotBlank() &&
                        serviceImage?.leftAfter.orEmpty().isNotBlank() &&
                        serviceImage?.rightAfter.orEmpty().isNotBlank(),
                isImageFrontBefore = serviceImage?.frontBefore.orEmpty().isNotBlank(),
                isImageBackBefore = serviceImage?.backBefore.orEmpty().isNotBlank(),
                isImageLeftBefore = serviceImage?.leftBefore.orEmpty().isNotBlank(),
                isImageRightBefore = serviceImage?.rightBefore.orEmpty().isNotBlank(),
                isImageFrontAfter = serviceImage?.frontAfter.orEmpty().isNotBlank(),
                isImageBackAfter = serviceImage?.backAfter.orEmpty().isNotBlank(),
                isImageLeftAfter = serviceImage?.leftAfter.orEmpty().isNotBlank(),
                isImageRightAfter = serviceImage?.rightAfter.orEmpty().isNotBlank()
            )
        }
    }

}
