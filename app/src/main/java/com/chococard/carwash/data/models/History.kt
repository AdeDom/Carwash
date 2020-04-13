package com.chococard.carwash.data.models

import com.google.gson.annotations.SerializedName

data class History(
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("full_name") val fullName: String? = null,
    val image: String? = null,
    @SerializedName("job_id") val jobId: String? = null,
    @SerializedName("package_id") val packageId: String? = null,
    val service: String? = null,
    val price: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerializedName("car_id") val carId: String? = null,
    @SerializedName("vehicle_registration") val vehicleRegistration: String? = null,
    @SerializedName("date_time") val dateTime: String? = null
)
