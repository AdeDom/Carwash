package com.chococard.carwash.data.networks.request

import com.chococard.carwash.data.models.Job
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class JobRequest(
    @SerializedName(ApiConstant.SUCCESS) val success: Boolean = false,
    @SerializedName(ApiConstant.MESSAGE) val message: String? = null,
    @SerializedName(ApiConstant.JOB) val job: Job? = null
)
