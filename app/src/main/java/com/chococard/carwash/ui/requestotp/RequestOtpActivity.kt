package com.chococard.carwash.ui.requestotp

import android.os.Bundle
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.verifyphone.VPSignUpActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
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
        when {
            et_phone.isEmpty(getString(R.string.error_empty_phone)) -> return
            et_phone.isEqualLength(10, getString(R.string.error_equal_length, 10)) -> return
            et_phone.isVerifyPhone(getString(R.string.error_phone)) -> return
        }

        val phoneNumber = et_phone.getContents()
        startActivity<VPSignUpActivity> { intent ->
            intent.putExtra(CommonsConstant.PHONE, phoneNumber)
            finish()
        }
    }

}
