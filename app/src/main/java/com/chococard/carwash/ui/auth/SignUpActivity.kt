package com.chococard.carwash.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.AuthApi
import com.chococard.carwash.data.repositories.AuthRepository
import com.chococard.carwash.util.Coroutines
import com.chococard.carwash.util.extension.toast
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        init()
    }

    private fun init() {
        //event
        iv_arrow_back.setOnClickListener {
            onBackPressed()
        }

        tv_sign_in.setOnClickListener {
            Intent(baseContext, SignInActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }

        bt_sign_up.setOnClickListener {
            Coroutines.main {
                val repo = AuthRepository(AuthApi.invoke())
                val response = repo.signUp("", "", "", "", "", "")
                toast("${response.success}, ${response.message}")
            }
        }
    }
}
