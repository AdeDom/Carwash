package com.chococard.carwash.util.extension

import android.app.Activity
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

fun Activity.selectImage(requestCode: Int) = Intent(Intent.ACTION_PICK).apply {
    type = "image/*"
    val mimeTypes = arrayOf("image/jpeg", "image/png")
    putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    startActivityForResult(this, requestCode)
}

fun Activity.hideSoftKeyboard() {
    currentFocus?.let {
        val imm = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}
