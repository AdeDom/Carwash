package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.models.EmployeeLocation

data class LocationResponse(
    val success: Boolean = false,
    val message: String? = null,
    val location: List<EmployeeLocation>? = null
)
