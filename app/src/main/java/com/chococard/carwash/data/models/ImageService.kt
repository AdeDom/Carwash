package com.chococard.carwash.data.models

import android.os.Parcel
import android.os.Parcelable
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class ImageService(
    @SerializedName(ApiConstant.IMAGE_ID) val imageId: Int? = null,
    @SerializedName(ApiConstant.IMAGE) val image: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(imageId)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageService> {
        override fun createFromParcel(parcel: Parcel): ImageService {
            return ImageService(parcel)
        }

        override fun newArray(size: Int): Array<ImageService?> {
            return arrayOfNulls(size)
        }
    }
}
