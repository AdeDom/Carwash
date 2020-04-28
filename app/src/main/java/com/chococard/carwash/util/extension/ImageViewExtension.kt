package com.chococard.carwash.util.extension

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chococard.carwash.R

fun ImageView.setImageCircle(url: String) {
    Glide.with(this)
        .load(url)
        .apply(RequestOptions.placeholderOf(R.drawable.ic_user))
        .circleCrop()
        .into(this)
}

fun ImageView.setImageCircle(bitmap: Bitmap?) {
    Glide.with(this)
        .load(bitmap)
        .apply(RequestOptions.placeholderOf(R.drawable.ic_user))
        .circleCrop()
        .into(this)
}
