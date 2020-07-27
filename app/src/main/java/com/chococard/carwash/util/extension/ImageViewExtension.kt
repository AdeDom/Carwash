package com.chococard.carwash.util.extension

import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chococard.carwash.R
import com.chococard.carwash.util.FlagConstant

fun ImageView.setImageCircle(url: String?, @DrawableRes placeholder: Int = R.drawable.ic_user) {
    Glide.with(this)
        .load(url)
        .apply(RequestOptions.placeholderOf(placeholder))
        .circleCrop()
        .into(this)
}

fun ImageView.setImageFromInternet(
    url: String?,
    @DrawableRes placeholder: Int = R.drawable.img_logo_blue
) {
    Glide.with(this)
        .load(url)
        .apply(RequestOptions.placeholderOf(placeholder))
        .into(this)
}

// TODO: 09/07/2563 password length
infix fun ImageView.setTogglePassword(editText: EditText) {
    if (this.tag == FlagConstant.TOGGLE_PASSWORD_OFF) {
        this.tag = FlagConstant.TOGGLE_PASSWORD_ON
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        editText.setSelection(editText.length())
    } else {
        this.tag = FlagConstant.TOGGLE_PASSWORD_OFF
        editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        editText.setSelection(editText.length())
    }
}
