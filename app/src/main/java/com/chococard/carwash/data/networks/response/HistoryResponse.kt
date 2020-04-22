package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.models.History
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @SerializedName(ApiConstant.SUCCESS) val success: Boolean = false,
    @SerializedName(ApiConstant.MESSAGE) val message: String? = null,
    @SerializedName(ApiConstant.HISTORY) val history: List<History>? = null
)
