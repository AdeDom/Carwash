package com.chococard.carwash.util.extension

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

fun ImageView.setImageFromInternet(url: String?) {
    Glide.with(this)
        .load(url)
        .apply(RequestOptions.placeholderOf(R.drawable.img_logo_blue))
        .into(this)
}
