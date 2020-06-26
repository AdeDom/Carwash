package com.chococard.carwash.ui.splashscreen

import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.auth.AuthActivity
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.ui.navigation.NavigationActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.CommonsConstant.ACCESS_COARSE_LOCATION
import com.chococard.carwash.util.CommonsConstant.ACCESS_FINE_LOCATION
import com.chococard.carwash.util.CommonsConstant.GRANTED
import com.chococard.carwash.util.extension.readPref
import com.chococard.carwash.util.extension.startActivity
import com.chococard.carwash.viewmodel.SplashScreenViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenActivity : BaseActivity() {

    val viewModel: SplashScreenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        init()
    }

    private fun init() {
        // check permission location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (
                ContextCompat.checkSelfPermission(baseContext, ACCESS_FINE_LOCATION) != GRANTED ||
                ContextCompat.checkSelfPermission(baseContext, ACCESS_COARSE_LOCATION) != GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION
                    ), CommonsConstant.REQUEST_CODE_PERMISSION
                )
            } else {
                onReadyCarWash()
            }
        } else {
            onReadyCarWash()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CommonsConstant.REQUEST_CODE_PERMISSION) {
            if (
                grantResults[0] != GRANTED ||
                grantResults[1] != GRANTED
            ) {
                finish()
            } else {
                onReadyCarWash()
            }
        }
    }

    private fun onReadyCarWash() {
        val token = readPref(CommonsConstant.TOKEN)
        if (token.isEmpty()) {
            startActivity<AuthActivity> {
                finish()
            }
        } else {
            viewModel.getDbJob.observe(this, Observer { job ->
                if (job == null) {
                    startActivity<MainActivity> {
                        finish()
                    }
                } else {
                    startActivity<NavigationActivity> {
                        finish()
                    }
                }
            })
        }
    }

}
