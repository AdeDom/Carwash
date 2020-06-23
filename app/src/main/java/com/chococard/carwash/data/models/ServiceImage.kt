package com.chococard.carwash.data.models

import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class ServiceImage(
    @SerializedName(ApiConstant.FRONT_BEFORE) val frontBefore: String? = null,
    @SerializedName(ApiConstant.BACK_BEFORE) val backBefore: String? = null,
    @SerializedName(ApiConstant.LEFT_BEFORE) val leftBefore: String? = null,
    @SerializedName(ApiConstant.RIGHT_BEFORE) val rightBefore: String? = null,
    @SerializedName(ApiConstant.FRONT_AFTER) val frontAfter: String? = null,
    @SerializedName(ApiConstant.BACK_AFTER) val backAfter: String? = null,
    @SerializedName(ApiConstant.LEFT_AFTER) val leftAfter: String? = null,
    @SerializedName(ApiConstant.RIGHT_AFTER) val rightAfter: String? = null,
    @SerializedName(ApiConstant.OTHER_IMAGES_SERVICE) val otherImageService: List<ImageService>? = null
)
