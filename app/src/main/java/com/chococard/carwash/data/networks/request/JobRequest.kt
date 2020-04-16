package com.chococard.carwash.data.networks.request

import com.chococard.carwash.data.models.Job

data class JobRequest(
    val success: Boolean = false,
    val message: String? = null,
    val job: Job? = null
)
