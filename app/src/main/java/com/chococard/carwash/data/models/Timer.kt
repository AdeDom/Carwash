package com.chococard.carwash.data.models

import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class Timer(
    @SerializedName(ApiConstant.EMPLOYEE_ID) val employeeId: Int? = null,
    @SerializedName(ApiConstant.TIMER) val timer: Int? = null
)
