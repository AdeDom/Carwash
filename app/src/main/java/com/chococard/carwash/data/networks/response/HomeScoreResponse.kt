package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.models.Home
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class HomeScoreResponse(
    @SerializedName(ApiConstant.SUCCESS) val success: Boolean = false,
    @SerializedName(ApiConstant.MESSAGE) val message: String? = null,
    @SerializedName(ApiConstant.HOME_SCORE) val homeScore: Home? = null,
    @SerializedName(ApiConstant.SWITCH_FLAG) val switchFlag: Int? = null
)
