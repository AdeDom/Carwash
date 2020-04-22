package com.chococard.carwash.data.models

import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class History(
    @SerializedName(ApiConstant.USER_ID) val userId: String? = null,
    @SerializedName(ApiConstant.FULL_NAME) val fullName: String? = null,
    @SerializedName(ApiConstant.IMAGE) val image: String? = null,
    @SerializedName(ApiConstant.JOB_ID) val jobId: String? = null,
    @SerializedName(ApiConstant.PACKAGE_ID) val packageId: String? = null,
    @SerializedName(ApiConstant.SERVICE) val service: String? = null,
    @SerializedName(ApiConstant.PRICE) val price: String? = null,
    @SerializedName(ApiConstant.LATITUDE) val latitude: Double? = null,
    @SerializedName(ApiConstant.LONGITUDE) val longitude: Double? = null,
    @SerializedName(ApiConstant.CAR_ID) val carId: String? = null,
    @SerializedName(ApiConstant.VEHICLE_REGISTRATION) val vehicleRegistration: String? = null,
    @SerializedName(ApiConstant.DATE_TIME) val dateTime: String? = null
)
