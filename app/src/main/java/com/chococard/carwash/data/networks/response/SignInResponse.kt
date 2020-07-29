package com.chococard.carwash.data.networks.response

import com.chococard.carwash.data.db.entities.UserInfo
import com.chococard.carwash.data.models.Token
import com.chococard.carwash.data.networks.ApiConstant
import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName(ApiConstant.SUCCESS) val success: Boolean = false,
    @SerializedName(ApiConstant.MESSAGE) val message: String? = null,
    @SerializedName(ApiConstant.TOKEN) val token: Token? = null,
    @SerializedName(ApiConstant.USER_INFO) val userInfo: UserInfo? = null
)
