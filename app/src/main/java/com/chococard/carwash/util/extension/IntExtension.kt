package com.chococard.carwash.util.extension

import okhttp3.MultipartBody
import okhttp3.RequestBody

fun Int?.toRequestBody(): RequestBody = RequestBody.create(MultipartBody.FORM, this.toString())
