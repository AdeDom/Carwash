package com.chococard.carwash.util.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.net.Uri
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chococard.carwash.R
import com.chococard.carwash.util.CommonsConstant
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

inline fun <reified T : Activity> Context.startActivity(noinline intent: ((Intent) -> Unit)? = null) {
    Intent(this, T::class.java).apply {
        intent?.invoke(this)
        startActivity(this)
    }
}

fun Context.startActivity(action: String, url: String) {
    Intent(action).apply {
        data = Uri.parse(url)
        startActivity(this)
    }
}

fun Context?.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).show()

fun Context.getLocality(latitude: Double, longitude: Double): String {
    return try {
        val list = Geocoder(this).getFromLocation(latitude, longitude, 1)
        if (list[0].locality != null) list[0].locality else getString(R.string.unknown)
    } catch (e: IOException) {
        getString(R.string.unknown)
    }
}

fun Context.writePref(key: String, values: String) =
    getSharedPreferences(CommonsConstant.PREF_FILE, Context.MODE_PRIVATE).edit().apply {
        putString(key, values)
        apply()
    }

fun Context.readPref(key: String) =
    getSharedPreferences(CommonsConstant.PREF_FILE, Context.MODE_PRIVATE)
        .getString(key, "") ?: ""

fun Context.convertToMultipartBody(fileUri: Uri): MultipartBody.Part {
    val parcelFileDescriptor = contentResolver.openFileDescriptor(fileUri, "r", null)

    val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
    val file = File(cacheDir, contentResolver.getFileName(fileUri))
    val outputStream = FileOutputStream(file)
    inputStream.copyTo(outputStream)

    val requestFile = RequestBody
        .create(MediaType.parse(contentResolver.getType(fileUri)), file)

    return MultipartBody.Part.createFormData("file", file?.name, requestFile)
}

fun Context.setImageCircle(
    url: String?,
    onResourceReady: (Bitmap) -> Unit
) {
    Glide.with(this)
        .asBitmap()
        .load(url)
        .apply(RequestOptions.placeholderOf(R.drawable.ic_user))
        .circleCrop()
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                onResourceReady.invoke(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })
}

fun Context?.setImageMarkerCircle(image: Bitmap): Bitmap {
    val inflater = this?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view: View = inflater.inflate(R.layout.layout_my_location, null)
    view.findViewById<ImageView>(R.id.iv_photo).setImageBitmap(image)
    val displayMetrics = DisplayMetrics()
//    (this as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
    view.layoutParams = ViewGroup.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT)
    view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
    view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
    view.buildDrawingCache()
    val bitmap = Bitmap.createBitmap(
        view.measuredWidth,
        view.measuredHeight,
        Bitmap.Config.ARGB_8888
    )
    view.draw(Canvas(bitmap))
    return bitmap
}
