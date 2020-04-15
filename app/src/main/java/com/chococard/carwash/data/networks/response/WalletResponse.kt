package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.models.WalletLog
import com.google.gson.annotations.SerializedName

data class WalletResponse(
    val success: Boolean = false,
    val message: String? = null,
    val wallet: String? = null,
    @SerializedName("wallet_logs") val walletLogs: List<WalletLog>? = null
)
