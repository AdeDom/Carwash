package com.chococard.carwash.data.models

import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName(ApiConstant.USER_ID) val userId: String? = null,
    @SerializedName(ApiConstant.FULL_NAME) val fullName: String? = null,
    @SerializedName(ApiConstant.ID_CARD_NUMBER) val idCardNumber: String? = null,
    @SerializedName(ApiConstant.PHONE) val phone: String? = null,
    @SerializedName(ApiConstant.CODE) val code: String? = null,
    @SerializedName(ApiConstant.IMAGE) val image: String? = null
)
