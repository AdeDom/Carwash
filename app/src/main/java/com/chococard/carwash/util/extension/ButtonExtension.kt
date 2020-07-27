package com.chococard.carwash.util.extension

import android.widget.Button
import androidx.annotation.DrawableRes
import com.chococard.carwash.R

fun Button.ready(@DrawableRes res: Int = R.drawable.shape_bt_blue) {
    this.isClickable = true
    this.setBackgroundResource(res)
}

fun Button.unready(@DrawableRes res: Int = R.drawable.shape_bt_gray) {
    this.isClickable = false
    this.setBackgroundResource(res)
}
