package com.chococard.carwash.data.networks.response

import com.google.gson.annotations.SerializedName

data class JobResponse(
    val success: Boolean = false,
    val message: String? = null,
    @SerializedName("job_flag") val jobFlag: Boolean = false
)
