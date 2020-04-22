package com.chococard.carwash.data.models

import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class EmployeeLocation(
    @SerializedName(ApiConstant.USER_ID) val userId: String? = null,
    @SerializedName(ApiConstant.FULL_NAME) val fullName: String? = null,
    @SerializedName(ApiConstant.IMAGE) val image: String? = null,
    @SerializedName(ApiConstant.LATITUDE) val latitude: Double? = null,
    @SerializedName(ApiConstant.LONGITUDE) val longitude: Double? = null
)
