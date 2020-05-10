package com.chococard.carwash.ui.auth

import android.content.Intent
import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseNormalActivity
import com.chococard.carwash.ui.signin.SignInActivity
import com.chococard.carwash.ui.signup.SignUpActivity
import com.chococard.carwash.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : BaseNormalActivity<AuthViewModel>() {

    override fun viewModel() = AuthViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        bt_sign_up.setOnClickListener {
            Intent(baseContext, SignUpActivity::class.java).also {
                startActivity(it)
            }
        }

        bt_sign_in.setOnClickListener {
            Intent(baseContext, SignInActivity::class.java).also {
                startActivity(it)
            }
        }

    }
}
