package com.chococard.carwash.ui.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.AuthApi
import com.chococard.carwash.data.repositories.AuthRepository
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.util.Commons
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.base.BaseActivity
import com.chococard.carwash.util.extension.readPref
import kotlinx.coroutines.delay

class SplashScreenActivity : BaseActivity<AuthViewModel, AuthFactory>() {

    private val GRANTED = PackageManager.PERMISSION_GRANTED
    private val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val REQUEST_CODE_PERMISSION: Int = 1

    override fun viewModel() = AuthViewModel::class.java

    override fun factory() = AuthFactory(AuthRepository(AuthApi.invoke(interceptor)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        init()
    }

    private fun init() {
        // check permission location
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (
                ContextCompat.checkSelfPermission(baseContext, ACCESS_FINE_LOCATION) != GRANTED ||
                ContextCompat.checkSelfPermission(baseContext, ACCESS_COARSE_LOCATION) != GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION
                    ), REQUEST_CODE_PERMISSION
                )
            } else {
                authByCheckToken()
            }
        } else {
            authByCheckToken()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (
                grantResults[0] != GRANTED ||
                grantResults[1] != GRANTED
            ) {
                finish()
            } else {
                authByCheckToken()
            }
        }
    }

    private fun authByCheckToken() {
        Coroutines.main {
            delay(2000)

            val token = readPref(Commons.TOKEN)
            if (token.isEmpty()) {
                Intent(baseContext, AuthActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            } else {
                Intent(baseContext, MainActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
        }
    }

}
