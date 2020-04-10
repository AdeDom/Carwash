package com.chococard.carwash.data.networks

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val BASE_URL = "http://www.mocky.io/v2/"
//    const val BASE_URL = "http://192.168.43.22/upload/"

    fun instance(networkConnectionInterceptor: NetworkConnectionInterceptor): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}
