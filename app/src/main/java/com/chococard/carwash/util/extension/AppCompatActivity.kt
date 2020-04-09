package com.chococard.carwash.util.extension

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

fun AppCompatActivity.setToolbar(toolbar: Toolbar) {
    toolbar.title = ""
    setSupportActionBar(toolbar)
    toolbar.overflowIcon?.setColorFilter(
        resources.getColor(android.R.color.white),
        PorterDuff.Mode.SRC_ATOP
    )
}
