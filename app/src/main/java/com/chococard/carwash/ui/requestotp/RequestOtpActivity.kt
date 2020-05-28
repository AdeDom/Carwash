package com.chococard.carwash.ui.requestotp

import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.verifyphone.VerifyPhoneActivity
import com.chococard.carwash.util.extension.startActivity
import kotlinx.android.synthetic.main.activity_request_otp.*

class RequestOtpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_otp)

        init()
    }

    private fun init() {
        //set event
        bt_request_otp.setOnClickListener {
            requestOtp()
        }
    }

    private fun requestOtp() {
        startActivity<VerifyPhoneActivity> {
            finish()
        }
    }

}
