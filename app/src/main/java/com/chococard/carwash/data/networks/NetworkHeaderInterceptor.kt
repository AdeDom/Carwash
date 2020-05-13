package com.chococard.carwash.data.networks

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.chococard.carwash.ui.splashscreen.SplashScreenActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.NoInternetException
import com.chococard.carwash.util.extension.readPref
import okhttp3.Interceptor
import okhttp3.Response

class NetworkHeaderInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable())
            throw NoInternetException("Make sure you have an active data connection")

        val token = context.readPref(CommonsConstant.TOKEN)

        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", token)
            .build()

        val response = chain.proceed(request)

        if (response.code() == 401 || response.code() == 403) {
            Intent(context, SplashScreenActivity::class.java).also {
                context.startActivity(it)
            }
        }

        return response
    }

    @Suppress("DEPRECATION")
    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let { cm ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                    }
                }
            } else {
                connectivityManager.activeNetworkInfo.also {
                    result = it != null && it.isConnected
                }
            }
        }
        return result
    }

}
