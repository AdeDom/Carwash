package com.chococard.carwash.data.models

import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName(ApiConstant.ACCESS_TOKEN) val accessToken: String,
    @SerializedName(ApiConstant.REFRESH_TOKEN) val refreshToken: String
)
