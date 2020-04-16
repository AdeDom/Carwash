package com.chococard.carwash.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Job(
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(fullName)
        parcel.writeString(image)
        parcel.writeString(jobId)
        parcel.writeString(packageId)
        parcel.writeString(service)
        parcel.writeString(price)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeString(carId)
        parcel.writeString(vehicleRegistration)
        parcel.writeString(dateTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Job> {
        override fun createFromParcel(parcel: Parcel): Job {
            return Job(parcel)
        }

        override fun newArray(size: Int): Array<Job?> {
            return arrayOfNulls(size)
        }
    }
}
