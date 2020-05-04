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
    @SerializedName(ApiConstant.USER_ID) @ColumnInfo(name = DatabaseConstant.USER_ID) val userId: String? = null,
    @SerializedName(ApiConstant.FULL_NAME) @ColumnInfo(name = DatabaseConstant.FULL_NAME) val fullName: String? = null,
    @SerializedName(ApiConstant.IMAGE) @ColumnInfo(name = DatabaseConstant.IMAGE) val image: String? = null,
    @PrimaryKey(autoGenerate = false)
    @SerializedName(ApiConstant.JOB_ID) @ColumnInfo(name = DatabaseConstant.JOB_ID) val jobId: String = "",
    @SerializedName(ApiConstant.PACKAGE_ID) @ColumnInfo(name = DatabaseConstant.PACKAGE_ID) val packageId: String? = null,
    @SerializedName(ApiConstant.SERVICE) @ColumnInfo(name = DatabaseConstant.SERVICE) val service: String? = null,
    @SerializedName(ApiConstant.PRICE) @ColumnInfo(name = DatabaseConstant.PRICE) val price: String? = null,
    @SerializedName(ApiConstant.BEGIN_LATITUDE) @ColumnInfo(name = DatabaseConstant.BEGIN_LATITUDE) val beginLatitude: Double? = null,
    @SerializedName(ApiConstant.BEGIN_LONGITUDE) @ColumnInfo(name = DatabaseConstant.BEGIN_LONGITUDE) val beginLongitude: Double? = null,
    @SerializedName(ApiConstant.END_LATITUDE) @ColumnInfo(name = DatabaseConstant.END_LATITUDE) val endLatitude: Double? = null,
    @SerializedName(ApiConstant.END_LONGITUDE) @ColumnInfo(name = DatabaseConstant.END_LONGITUDE) val endLongitude: Double? = null,
    @SerializedName(ApiConstant.CAR_ID) @ColumnInfo(name = DatabaseConstant.CAR_ID) val carId: String? = null,
    @SerializedName(ApiConstant.VEHICLE_REGISTRATION) @ColumnInfo(name = DatabaseConstant.VEHICLE_REGISTRATION) val vehicleRegistration: String? = null,
    @SerializedName(ApiConstant.DATE_TIME) @ColumnInfo(name = DatabaseConstant.DATE_TIME) val dateTime: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString() as String,
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
