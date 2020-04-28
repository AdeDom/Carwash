package com.chococard.carwash.util.extension

import android.widget.EditText

//TODO create edit text extension isIdCard, Phone, Password
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

fun EditText.isMatched(editText: EditText, error: String = ""): Boolean {
    if (this.getContents() != editText.getContents()) {
        editText.requestFocus()
        editText.error = error
        return true
    }
    return false
}

fun EditText.setWarning(message: String = "") {
    this.apply {
        requestFocus()
        error = message
    }
}
