package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.models.EmployeeLocation
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName(ApiConstant.SUCCESS) val success: Boolean = false,
    @SerializedName(ApiConstant.MESSAGE) val message: String? = null,
    @SerializedName(ApiConstant.EMPLOYEE_LOCATION) val employeeLocation: List<EmployeeLocation>? = null
)
