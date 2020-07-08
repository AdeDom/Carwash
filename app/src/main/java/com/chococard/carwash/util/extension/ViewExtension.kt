package com.chococard.carwash.util.extension

import android.view.View
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
    text: Int = android.R.string.ok,
    action: (() -> Unit)? = null
) = Snackbar.make(this, errorMessage.toString(), Snackbar.LENGTH_LONG)
    .setAction(text) {
        action?.invoke()
    }.show()
