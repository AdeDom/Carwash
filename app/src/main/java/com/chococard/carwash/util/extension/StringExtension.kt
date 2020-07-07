package com.chococard.carwash.util.extension

import android.text.SpannableString
import android.text.style.UnderlineSpan
import okhttp3.MultipartBody
import okhttp3.RequestBody

fun String.isVerifyIdentityCard(): Boolean {
    val identityCard = this.substring(0, 12)
    var sumIdentityCard = 0
    identityCard.forEachIndexed { index, c ->
        sumIdentityCard += (13 - index) * c.toString().toInt()
    }
    val digit = (11 - (sumIdentityCard % 11)) % 10
    val realIdentityCard = identityCard + digit
    return realIdentityCard != this
}

fun String.isVerifyPhone(): Boolean {
    val tel = listOf("06", "08", "09")
    val p = this.substring(0, 2)
    var isPhone = true
    tel.forEach {
        if (it == p) {
            isPhone = false
            return@forEach
        }
    }
    return isPhone
}

fun String?.toRequestBody(): RequestBody = RequestBody.create(MultipartBody.FORM, this.toString())

fun String?.toUnderline(): SpannableString {
    val spannableString = SpannableString(this)
    spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)
    return spannableString
}
