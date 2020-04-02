package com.chococard.carwash.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chococard.carwash.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        init()
    }

    private fun init() {
        //event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        tv_sign_up.setOnClickListener {
            Intent(baseContext, SignUpActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }
    }
}
