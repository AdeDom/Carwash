package com.chococard.carwash.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chococard.carwash.data.db.DatabaseConstant
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

@Entity(tableName = DatabaseConstant.USER_INFO)
data class UserInfo(
    @PrimaryKey(autoGenerate = false)
    @SerializedName(ApiConstant.USER_ID) @ColumnInfo(name = DatabaseConstant.USER_ID) val userId: Int = 0,
    @SerializedName(ApiConstant.FULL_NAME) @ColumnInfo(name = DatabaseConstant.FULL_NAME) val fullName: String? = null,
    @SerializedName(ApiConstant.ID_CARD_NUMBER) @ColumnInfo(name = DatabaseConstant.ID_CARD_NUMBER) val idCardNumber: String? = null,
    @SerializedName(ApiConstant.PHONE) @ColumnInfo(name = DatabaseConstant.PHONE) val phone: String? = null,
    @SerializedName(ApiConstant.CODE) @ColumnInfo(name = DatabaseConstant.CODE) val code: String? = null,
    @SerializedName(ApiConstant.IMAGE) @ColumnInfo(name = DatabaseConstant.IMAGE) val image: String? = null
)
