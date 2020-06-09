package com.chococard.carwash.data.models

import android.os.Parcel
import android.os.Parcelable
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class OtherImage(
    @SerializedName(ApiConstant.IMAGE) val image: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OtherImage> {
        override fun createFromParcel(parcel: Parcel): OtherImage {
            return OtherImage(parcel)
        }

        override fun newArray(size: Int): Array<OtherImage?> {
            return arrayOfNulls(size)
        }
    }
}
