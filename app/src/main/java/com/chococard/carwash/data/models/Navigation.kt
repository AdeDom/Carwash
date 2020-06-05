package com.chococard.carwash.data.models

import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class Navigation(
    @SerializedName(ApiConstant.CUSTOMER_LATITUDE) val customerLatitude: Double? = null,
    @SerializedName(ApiConstant.CUSTOMER_LONGITUDE) val customerLongitude: Double? = null
)
