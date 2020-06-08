package com.chococard.carwash.util.extension

import android.app.Activity
import android.content.Intent

fun Activity.selectImage(requestCode: Int) = Intent(Intent.ACTION_PICK).apply {
    type = "image/*"
    val mimeTypes = arrayOf("image/jpeg", "image/png")
    putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    startActivityForResult(this, requestCode)
}
