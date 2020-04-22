package com.chococard.carwash.util.extension

import android.widget.EditText

fun EditText.getContents() = this.text.toString().trim()

fun EditText.isEmpty(error: String = ""): Boolean {
    if (this.getContents().isEmpty()) {
        this.requestFocus()
        this.error = error
        return true
    }
    return false
}

fun EditText.isMinLength(length: Int, error: String = ""): Boolean {
    if (this.getContents().length < length) {
        this.requestFocus()
        this.error = error
        return true
    }
    return false
}

fun EditText.isEqualLength(length: Int, error: String = ""): Boolean {
    if (this.getContents().length != length) {
        this.requestFocus()
        this.error = error
        return true
    }
    return false
}

fun EditText.isMatching(editText: EditText, error: String = ""): Boolean {
    if (this.getContents() != editText.getContents()) {
        editText.requestFocus()
        editText.error = error
        return true
    }
    return false
}

fun EditText.failed(message: String = "") {
    this.apply {
        requestFocus()
        error = message
    }
}
