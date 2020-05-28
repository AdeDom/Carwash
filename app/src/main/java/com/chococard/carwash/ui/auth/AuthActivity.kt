package com.chococard.carwash.ui.auth

import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.requestotp.RequestOtpActivity
import com.chococard.carwash.ui.signin.SignInActivity
import com.chococard.carwash.util.extension.startActivity
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        bt_sign_up.setOnClickListener {
            startActivity<RequestOtpActivity>()
        }

        bt_sign_in.setOnClickListener {
            startActivity<SignInActivity>()
        }
    }

}
