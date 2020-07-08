package com.chococard.carwash.util.extension

import android.widget.Button
import com.chococard.carwash.R

fun Button.ready() {
    this.isClickable = true
    this.setBackgroundResource(R.drawable.shape_bt_blue)
}

fun Button.unready() {
    this.isClickable = false
    this.setBackgroundResource(R.drawable.shape_bt_gray)
}
