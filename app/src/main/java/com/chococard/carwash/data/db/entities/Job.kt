package com.chococard.carwash.data.db.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chococard.carwash.data.db.DatabaseConstant
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

@Entity(tableName = DatabaseConstant.JOB)
data class Job(
    @PrimaryKey(autoGenerate = false)
    @SerializedName(ApiConstant.JOB_ID) @ColumnInfo(name = DatabaseConstant.JOB_ID) val jobId: Int = 0,
    @SerializedName(ApiConstant.FULL_NAME) @ColumnInfo(name = DatabaseConstant.FULL_NAME) val fullName: String? = null,
    @SerializedName(ApiConstant.IMAGE_PROFILE) @ColumnInfo(name = DatabaseConstant.IMAGE_PROFILE) val imageProfile: String? = null,
    @SerializedName(ApiConstant.PACKAGE_NAME) @ColumnInfo(name = DatabaseConstant.PACKAGE_NAME) val packageName: String? = null,
    @SerializedName(ApiConstant.PRICE) @ColumnInfo(name = DatabaseConstant.PRICE) val price: String? = null,
    @SerializedName(ApiConstant.VEHICLE_REGISTRATION) @ColumnInfo(name = DatabaseConstant.VEHICLE_REGISTRATION) val vehicleRegistration: String? = null,
    @SerializedName(ApiConstant.LATITUDE) @ColumnInfo(name = DatabaseConstant.LATITUDE) val latitude: Double? = null,
    @SerializedName(ApiConstant.LONGITUDE) @ColumnInfo(name = DatabaseConstant.LONGITUDE) val longitude: Double? = null,
    @SerializedName(ApiConstant.DATE_TIME) @ColumnInfo(name = DatabaseConstant.DATE_TIME) val dateTime: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(jobId)
        parcel.writeString(fullName)
        parcel.writeString(imageProfile)
        parcel.writeString(packageName)
        parcel.writeString(price)
        parcel.writeString(vehicleRegistration)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
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
