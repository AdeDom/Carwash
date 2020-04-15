package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.models.EmployeeLocation
import com.google.gson.annotations.SerializedName

data class LocationResponse(
    val success: Boolean = false,
    val message: String? = null,
    @SerializedName("employee_location") val employeeLocation: List<EmployeeLocation>? = null
)
