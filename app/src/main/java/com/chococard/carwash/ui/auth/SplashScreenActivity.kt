package com.chococard.carwash.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chococard.carwash.R
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.extension.readPref
import kotlinx.coroutines.delay

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

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
