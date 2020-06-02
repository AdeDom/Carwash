package com.chococard.carwash.data.models

import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class History(
    @SerializedName(ApiConstant.JOB_ID) val jobId: Int? = null,
    @SerializedName(ApiConstant.FULL_NAME) val fullName: String? = null,
    @SerializedName(ApiConstant.IMAGE_PROFILE) val imageProfile: String? = null,
    @SerializedName(ApiConstant.PACKAGE_NAME) val packageName: String? = null,
    @SerializedName(ApiConstant.LATITUDE) val latitude: Double? = null,
    @SerializedName(ApiConstant.LONGITUDE) val longitude: Double? = null,
    @SerializedName(ApiConstant.VEHICLE_REGISTRATION) val vehicleRegistration: String? = null,
    @SerializedName(ApiConstant.PRICE) val price: String? = null,
    @SerializedName(ApiConstant.JOB_DATE_TIME) val jobDateTime: String? = null,
    @SerializedName(ApiConstant.IMAGE_FRONT) val imageFront: String? = null,
    @SerializedName(ApiConstant.IMAGE_BACK) val imageBack: String? = null,
    @SerializedName(ApiConstant.IMAGE_LEFT) val imageLeft: String? = null,
    @SerializedName(ApiConstant.IMAGE_RIGHT) val imageRight: String? = null,
    @SerializedName(ApiConstant.OTHER_IMAGE) val otherImage: Any? = null,
    @SerializedName(ApiConstant.COMMENT) val comment: String? = null
)
