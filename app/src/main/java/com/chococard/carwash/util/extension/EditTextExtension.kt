package com.chococard.carwash.util.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import com.chococard.carwash.R
import com.chococard.carwash.util.FlagConstant

fun EditText.getContents() = this.text.toString().trim()

// todo delete edit text set error
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

fun EditText.isVerifyIdentityCard(error: String = ""): Boolean {
    if (this.getContents().isVerifyIdentityCard()) {
        this.requestFocus()
        this.error = error
        return true
    }
    return false
}

fun EditText.isVerifyPhone(error: String = ""): Boolean {
    if (this.getContents().isVerifyPhone()) {
        this.requestFocus()
        this.error = error
        return true
    }
    return false
}

fun EditText.onTextChanged(text: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            text.invoke(s.toString())
        }
    })
}

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
