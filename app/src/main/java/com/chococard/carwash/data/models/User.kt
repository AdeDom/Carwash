package com.chococard.carwash.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("full_name") val fullName: String? = null,
    @SerializedName("id_card") val idCard: String? = null,
    val phone: String? = null,
    val code: String? = null,
    val image: String? = null
)
