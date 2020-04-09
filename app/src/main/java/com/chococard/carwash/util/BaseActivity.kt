package com.chococard.carwash.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chococard.carwash.data.networks.NetworkConnectionInterceptor

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    lateinit var viewModel: VM
    lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkConnectionInterceptor = NetworkConnectionInterceptor(baseContext)
    }

}
