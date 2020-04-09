package com.chococard.carwash.util

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.NetworkConnectionInterceptor

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    lateinit var viewModel: VM
    lateinit var networkConnectionInterceptor: NetworkConnectionInterceptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkConnectionInterceptor = NetworkConnectionInterceptor(baseContext)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return true
    }

}
