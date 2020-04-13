package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.models.History

data class HistoryResponse(
    val success: Boolean = false,
    val message: String? = null,
    val history: List<History>? = null
)
