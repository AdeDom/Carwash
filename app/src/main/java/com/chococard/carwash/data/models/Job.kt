package com.chococard.carwash.data.models

import android.os.Parcel
import android.os.Parcelable
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class Job(
    @SerializedName(ApiConstant.USER_ID) val userId: String? = null,
    @SerializedName(ApiConstant.FULL_NAME) val fullName: String? = null,
    @SerializedName(ApiConstant.IMAGE) val image: String? = null,
    @SerializedName(ApiConstant.JOB_ID) val jobId: String? = null,
    @SerializedName(ApiConstant.PACKAGE_ID) val packageId: String? = null,
    @SerializedName(ApiConstant.SERVICE) val service: String? = null,
    @SerializedName(ApiConstant.PRICE) val price: String? = null,
    @SerializedName(ApiConstant.BEGIN_LATITUDE) val beginLatitude: Double? = null,
    @SerializedName(ApiConstant.BEGIN_LONGITUDE) val beginLongitude: Double? = null,
    @SerializedName(ApiConstant.END_LATITUDE) val endLatitude: Double? = null,
    @SerializedName(ApiConstant.END_LONGITUDE) val endLongitude: Double? = null,
    @SerializedName(ApiConstant.CAR_ID) val carId: String? = null,
    @SerializedName(ApiConstant.VEHICLE_REGISTRATION) val vehicleRegistration: String? = null,
    @SerializedName(ApiConstant.DATE_TIME) val dateTime: String? = null
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
        parcel.writeValue(beginLatitude)
        parcel.writeValue(beginLongitude)
        parcel.writeValue(endLatitude)
        parcel.writeValue(endLongitude)
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
