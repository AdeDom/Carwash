package com.chococard.carwash.data.models

import android.os.Parcel
import android.os.Parcelable

data class DateRangePicker(
    val dateBegin: Long? = null,
    val dateEnd: Long? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readValue(Long::class.java.classLoader) as? Long
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(dateBegin)
        parcel.writeValue(dateEnd)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DateRangePicker> {
        override fun createFromParcel(parcel: Parcel): DateRangePicker {
            return DateRangePicker(parcel)
        }

        override fun newArray(size: Int): Array<DateRangePicker?> {
            return arrayOfNulls(size)
        }
    }
}
