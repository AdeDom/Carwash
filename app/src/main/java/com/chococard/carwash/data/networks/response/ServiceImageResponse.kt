package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.models.ServiceImage
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class ServiceImageResponse(
    @SerializedName(ApiConstant.SUCCESS) val success: Boolean = false,
    @SerializedName(ApiConstant.MESSAGE) val message: String? = null,
    @SerializedName(ApiConstant.SERVICE_IMAGE) val serviceImage: ServiceImage? = null
)
