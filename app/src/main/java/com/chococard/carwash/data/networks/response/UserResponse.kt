package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.models.User
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName(ApiConstant.SUCCESS) val success: Boolean = false,
    @SerializedName(ApiConstant.MESSAGE) val message: String? = null,
    @SerializedName(ApiConstant.USER) val user: User? = null
)
