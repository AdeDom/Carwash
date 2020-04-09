package com.chococard.carwash.data.networks

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun retrofitClient(networkConnectionInterceptor: NetworkConnectionInterceptor): Retrofit {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(networkConnectionInterceptor)
        .build()

    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
