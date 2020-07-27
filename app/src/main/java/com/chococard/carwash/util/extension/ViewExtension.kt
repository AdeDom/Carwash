package com.chococard.carwash.util.extension

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.snackbar(
    errorMessage: String?,
    duration: Int = Snackbar.LENGTH_INDEFINITE,
    @StringRes text: Int = android.R.string.ok,
    action: (() -> Unit)? = null
) = Snackbar.make(this, errorMessage.toString(), duration)
    .setAction(text) {
        action?.invoke()
    }.show()
