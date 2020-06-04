package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.db.entities.Job
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class JobResponse(
    @SerializedName(ApiConstant.SUCCESS) val success: Boolean = false,
    @SerializedName(ApiConstant.MESSAGE) val message: String? = null,
    @SerializedName(ApiConstant.JOB) val job: Job? = null
)
