package com.chococard.carwash.data.networks

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.chococard.carwash.data.sharedpreference.SharedPreference
import com.chococard.carwash.ui.splashscreen.SplashScreenActivity
import com.chococard.carwash.util.NoInternetException
import com.chococard.carwash.util.extension.startActivity
import okhttp3.Interceptor
import okhttp3.Response

class NetworkHeaderInterceptor(
    private val context: Context,
    private val sharedPreference: SharedPreference
) : Interceptor {

    // TODO: 21/07/2563 re-fresh token
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable())
            throw NoInternetException("Make sure you have an active data connection")

        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", sharedPreference.accessToken)
            .build()

        val response = chain.proceed(request)

        if (response.code() == 401 || response.code() == 403) {
            context.startActivity<SplashScreenActivity>()
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
