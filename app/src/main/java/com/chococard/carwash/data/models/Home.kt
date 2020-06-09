package com.chococard.carwash.data.models

import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class Home(
    @SerializedName(ApiConstant.RATINGS) val ratings: String? = null,
    @SerializedName(ApiConstant.ACCEPTANCE) val acceptance: String? = null,
    @SerializedName(ApiConstant.CANCELLATION) val cancellation: String? = null
)
