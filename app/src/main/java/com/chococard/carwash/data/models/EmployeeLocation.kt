package com.chococard.carwash.data.models

import com.google.gson.annotations.SerializedName

data class EmployeeLocation(
    @SerializedName("full_name") val fullName: String? = null,
    val image: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerializedName("user_id") val userId: String? = null
)
