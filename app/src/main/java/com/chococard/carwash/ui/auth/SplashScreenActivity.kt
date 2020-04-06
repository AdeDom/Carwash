package com.chococard.carwash.ui.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.chococard.carwash.R
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.extension.readPref
import kotlinx.coroutines.delay

class SplashScreenActivity : AppCompatActivity() {

    private val GRANTED = PackageManager.PERMISSION_GRANTED
    private val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    private val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val REQUEST_CODE_PERMISSION: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (
            ContextCompat.checkSelfPermission(baseContext, ACCESS_FINE_LOCATION) != GRANTED ||
            ContextCompat.checkSelfPermission(baseContext, ACCESS_COARSE_LOCATION) != GRANTED ||
            ContextCompat.checkSelfPermission(baseContext, READ_EXTERNAL_STORAGE) != GRANTED ||
            ContextCompat.checkSelfPermission(baseContext, WRITE_EXTERNAL_STORAGE) != GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION,
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
                ), REQUEST_CODE_PERMISSION
            )
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
                grantResults[1] != GRANTED ||
                grantResults[2] != GRANTED ||
                grantResults[3] != GRANTED
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

            val token = readPref(R.string.token)
            if (token.isEmpty()) {
                Intent(baseContext, AuthActivity::class.java).also {
                    startActivity(it)
                }
            } else {
                Intent(baseContext, MainActivity::class.java).also {
                    startActivity(it)
                }
            }

            finish()
        }
    }

}
