package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.models.Navigation
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class NavigationResponse(
    @SerializedName(ApiConstant.SUCCESS) val success: Boolean = false,
    @SerializedName(ApiConstant.MESSAGE) val message: String? = null,
    @SerializedName(ApiConstant.NAVIGATION) val navigation: Navigation? = null
)
