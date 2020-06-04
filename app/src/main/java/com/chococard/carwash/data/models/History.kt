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
    @SerializedName(ApiConstant.IMAGE_FRONT) val imageFront: String? = null,
    @SerializedName(ApiConstant.IMAGE_BACK) val imageBack: String? = null,
    @SerializedName(ApiConstant.IMAGE_LEFT) val imageLeft: String? = null,
    @SerializedName(ApiConstant.IMAGE_RIGHT) val imageRight: String? = null,
    @SerializedName(ApiConstant.OTHER_IMAGE) val otherImages: List<OtherImage>? = null,
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        arrayListOf<OtherImage>().apply {
            parcel.readList(this as List<OtherImage>, OtherImage::class.java.classLoader)
        },
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
        parcel.writeString(imageFront)
        parcel.writeString(imageBack)
        parcel.writeString(imageLeft)
        parcel.writeString(imageRight)
        parcel.writeList(otherImages)
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
