package com.chococard.carwash.data.networks

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    const val BASE_URL = "https://sncarwash.azurewebsites.net/"

    fun instant(interceptor: Interceptor): Retrofit {
        val okHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
            connectTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
        }.build()

        return Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }

}
