package com.chococard.carwash.data.models

import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class ServiceImage(
    @SerializedName(ApiConstant.IMAGE_FRONT) val imageFront: String? = null,
    @SerializedName(ApiConstant.IMAGE_BACK) val imageBack: String? = null,
    @SerializedName(ApiConstant.IMAGE_LEFT) val imageLeft: String? = null,
    @SerializedName(ApiConstant.IMAGE_RIGHT) val imageRight: String? = null,
    @SerializedName(ApiConstant.OTHER_IMAGES) val otherImages: List<OtherImage>? = null
)
