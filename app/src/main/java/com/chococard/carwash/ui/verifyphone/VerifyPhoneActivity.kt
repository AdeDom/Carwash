package com.chococard.carwash.ui.verifyphone

import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.signup.SignUpActivity
import com.chococard.carwash.util.extension.startActivity
import kotlinx.android.synthetic.main.activity_verify_phone.*

class VerifyPhoneActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone)

        init()
    }

    private fun init() {
        //set event
        bt_verify_phone.setOnClickListener {
            verifyOtp()
        }
    }

    private fun verifyOtp() {
        startActivity<SignUpActivity> {
            finish()
        }
    }

}
