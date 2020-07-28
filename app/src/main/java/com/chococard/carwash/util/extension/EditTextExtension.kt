package com.chococard.carwash.util.extension

import android.widget.EditText
import android.widget.ImageView
import com.chococard.carwash.R
import com.chococard.carwash.util.FlagConstant

fun EditText.getContents() = this.text.toString().trim()

infix fun EditText.setTogglePassword(imageView: ImageView) {
    if (this.text.isNotEmpty()) {
        imageView.show()
        if (imageView.tag == FlagConstant.TOGGLE_PASSWORD_OFF) {
            imageView.setImageResource(R.drawable.ic_visibility_off)
        } else {
            imageView.setImageResource(R.drawable.ic_visibility)
        }
    } else {
        imageView.hide()
        imageView.setImageResource(0)
    }
}
