package com.chococard.carwash.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("full_name") val fullName: String? = null,
    @SerializedName("id_card") val idCard: String? = null,
    val phone: String? = null,
    val code: String? = null,
    val image: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(fullName)
        parcel.writeString(idCard)
        parcel.writeString(phone)
        parcel.writeString(code)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
