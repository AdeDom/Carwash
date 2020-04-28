package com.chococard.carwash.data.networks

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.chococard.carwash.util.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkHeaderInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable())
            throw NoInternetException("Make sure you have an active data connection")

//        val token = context.readPref(CommonsConstant.TOKEN)
//
//        val request = chain.request()
//            .newBuilder()
//            .addHeader("access_key", token)
//            .build()
//
//        return chain.proceed(request)

        return chain.proceed(chain.request())
    }

    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
        }
        return result
    }

}
