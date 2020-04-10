package com.chococard.carwash.util.extension

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.chococard.carwash.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).show()

fun Context.writePref(key: Int, values: String) =
    getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE).edit()
        .putString(getString(key), values)
        .apply()

fun Context.readPref(key: Int) =
    getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE)
        .getString(getString(key), "") ?: ""

fun Context.uploadFile(fileUri: Uri, upload: (MultipartBody.Part, RequestBody) -> Unit) {
    val parcelFileDescriptor = contentResolver.openFileDescriptor(fileUri, "r", null) ?: return

    val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
    val file = File(cacheDir, contentResolver.getFileName(fileUri))
    val outputStream = FileOutputStream(file)
    inputStream.copyTo(outputStream)

    val requestFile = RequestBody
        .create(MediaType.parse(contentResolver.getType(fileUri)), file)

    val body = MultipartBody.Part.createFormData("uploaded_file", file?.name, requestFile)

    val descriptionString = "hello, this is description speaking"
    val description = RequestBody.create(MultipartBody.FORM, descriptionString)

    upload.invoke(body, description)
}
