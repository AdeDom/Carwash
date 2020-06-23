package com.chococard.carwash.data.models

import android.os.Parcel
import android.os.Parcelable
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class History(
    @SerializedName(ApiConstant.JOB_ID) val jobId: Int? = null,
    @SerializedName(ApiConstant.FULL_NAME) val fullName: String? = null,
    @SerializedName(ApiConstant.IMAGE_PROFILE) val imageProfile: String? = null,
    @SerializedName(ApiConstant.PACKAGE_NAME) val packageName: String? = null,
    @SerializedName(ApiConstant.LATITUDE) val latitude: Double? = null,
    @SerializedName(ApiConstant.LONGITUDE) val longitude: Double? = null,
    @SerializedName(ApiConstant.VEHICLE_REGISTRATION) val vehicleRegistration: String? = null,
    @SerializedName(ApiConstant.PRICE) val price: String? = null,
    @SerializedName(ApiConstant.JOB_DATE_TIME) val jobDateTime: String? = null,
    @SerializedName(ApiConstant.IMAGE_BEFORE_SERVICE) val imageBeforeService: List<ImageService>? = null,
    @SerializedName(ApiConstant.IMAGE_AFTER_SERVICE) val imagesAfterService: List<ImageService>? = null,
    @SerializedName(ApiConstant.OTHER_IMAGE_SERVICE) val otherImageService: List<ImageService>? = null,
    @SerializedName(ApiConstant.COMMENT) val comment: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(ImageService),
        parcel.createTypedArrayList(ImageService),
        parcel.createTypedArrayList(ImageService),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(jobId)
        parcel.writeString(fullName)
        parcel.writeString(imageProfile)
        parcel.writeString(packageName)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeString(vehicleRegistration)
        parcel.writeString(price)
        parcel.writeString(jobDateTime)
        parcel.writeTypedList(imageBeforeService)
        parcel.writeTypedList(imagesAfterService)
        parcel.writeTypedList(otherImageService)
        parcel.writeString(comment)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<History> {
        override fun createFromParcel(parcel: Parcel): History {
            return History(parcel)
        }

        override fun newArray(size: Int): Array<History?> {
            return arrayOfNulls(size)
        }
    }
}
