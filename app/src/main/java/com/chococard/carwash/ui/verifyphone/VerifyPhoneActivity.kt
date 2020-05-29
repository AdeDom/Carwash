package com.chococard.carwash.ui.verifyphone

import android.os.Bundle
import android.widget.Toast
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.signup.SignUpActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.startActivity
import com.chococard.carwash.util.extension.toast
import kotlinx.android.synthetic.main.activity_verify_phone.*

class VerifyPhoneActivity : BaseActivity() {

    private var mPhoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone)

        init()
    }

    private fun init() {
        //get intent phone
        mPhoneNumber = intent.getStringExtra(CommonsConstant.PHONE)
        if (mPhoneNumber != null)
            toast(mPhoneNumber!!, Toast.LENGTH_LONG)

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
