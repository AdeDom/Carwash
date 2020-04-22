package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.models.WalletLog
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class WalletResponse(
    @SerializedName(ApiConstant.SUCCESS) val success: Boolean = false,
    @SerializedName(ApiConstant.MESSAGE) val message: String? = null,
    @SerializedName(ApiConstant.WALLET) val wallet: String? = null,
    @SerializedName(ApiConstant.WALLET_LOGS) val walletLogs: List<WalletLog>? = null
)
