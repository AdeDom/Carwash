package com.chococard.carwash.util.extension

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chococard.carwash.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

inline fun <reified T : Activity> Context.startActivity(noinline intent: ((Intent) -> Unit)? = null) {
    Intent(this, T::class.java).apply {
        intent?.invoke(this)
        startActivity(this)
    }
}

fun Context.startActivityActionDial(phone: String? = getString(R.string.contact_admin_tel)) {
    Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phone")
        startActivity(this)
    }
}

fun Context.startActivityGoogleMapNavigation(
    @StringRes googleMapsNavigation: Int = R.string.google_maps_navigation,
    beginLatitude: Double?,
    beginLongitude: Double?,
    endLatitude: Double?,
    endLongitude: Double?
) = Intent(Intent.ACTION_VIEW).apply {
    val url = getString(
        googleMapsNavigation,
        beginLatitude,
        beginLongitude,
        endLatitude,
        endLongitude
    )
    data = Uri.parse(url)
    startActivity(this)
}

fun Context.convertToMultipartBody(fileUri: Uri): MultipartBody.Part {
    val parcelFileDescriptor = contentResolver.openFileDescriptor(fileUri, "r", null)

    val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
    val file = File(cacheDir, contentResolver.getFileName(fileUri))
    val outputStream = FileOutputStream(file)
    inputStream.copyTo(outputStream)

    val requestFile = RequestBody
        .create(MediaType.parse(contentResolver.getType(fileUri)), file)

    return MultipartBody.Part.createFormData("file", file.name, requestFile)
}

fun Context.setImageCircle(
    url: String?,
    @DrawableRes placeholder: Int = R.drawable.ic_user,
    onResourceReady: (Bitmap) -> Unit
) {
    Glide.with(this)
        .asBitmap()
        .load(url)
        .apply(RequestOptions.placeholderOf(placeholder))
        .circleCrop()
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                onResourceReady.invoke(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })
}

fun Context?.setImageMarkerCircle(
    image: Bitmap,
    @LayoutRes layout: Int = R.layout.layout_my_location
): Bitmap {
    val inflater = this?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view: View = inflater.inflate(layout, null)
    view.findViewById<ImageView>(R.id.iv_photo).setImageBitmap(image)
    val displayMetrics = DisplayMetrics()
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

fun Context.dialogNegative(@StringRes title: Int, @StringRes message: Int, negative: () -> Unit) =
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(android.R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        setNegativeButton(android.R.string.yes) { _, _ ->
            negative.invoke()
        }
        setCancelable(false)
        show()
    }

fun Context.dialogPositive(
    @StringRes title: Int,
    message: String?,
    positive: (DialogInterface) -> Unit
) = AlertDialog.Builder(this).apply {
    setTitle(title)
    setMessage(message)
    setPositiveButton(android.R.string.ok) { dialog, _ ->
        positive.invoke(dialog)
    }
    setCancelable(false)
    show()
}

fun Context.startAnimationFabOpen(view: View, @AnimRes anim: Int = R.anim.fab_open) {
    view.startAnimation(AnimationUtils.loadAnimation(this, anim))
}

fun Context.startAnimationFabClose(view: View, @AnimRes anim: Int = R.anim.fab_close) {
    view.startAnimation(AnimationUtils.loadAnimation(this, anim))
}

fun Context.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}
