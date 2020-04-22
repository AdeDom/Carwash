package com.chococard.carwash.data.models

import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class WalletLog(
    @SerializedName(ApiConstant.DATE) val date: String? = null,
    @SerializedName(ApiConstant.TIME) val time: String? = null,
    @SerializedName(ApiConstant.TYPE) val type: String? = null,
    @SerializedName(ApiConstant.AMOUNT) val amount: String? = null,
    @SerializedName(ApiConstant.CONFIRM) val confirm: Boolean = false
)
