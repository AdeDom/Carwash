package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.models.History

data class HistoryResponse(
    val history: List<History>? = null,
    val message: String? = null,
    val success: Boolean = false
)
