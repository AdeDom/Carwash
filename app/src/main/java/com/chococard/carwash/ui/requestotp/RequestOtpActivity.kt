package com.chococard.carwash.ui.requestotp

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.request.ValidatePhone
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.verifyphone.VPSignUpActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.RequestOtpViewModel
import kotlinx.android.synthetic.main.activity_request_otp.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequestOtpActivity : BaseActivity() {

    val viewModel: RequestOtpViewModel by viewModel()

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

        viewModel.getValidatePhone.observe(this, Observer { response ->
            val (success, message) = response
            progress_bar.hide()
            if (success) {
                val phoneNumber = et_phone.getContents()
                startActivity<VPSignUpActivity> { intent ->
                    intent.putExtra(CommonsConstant.PHONE, phoneNumber)
                    finish()
                }
            } else {
                message?.let { dialogValidatePhone(it) }
            }
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            dialogError(it)
        })
    }

    private fun requestOtp() {
        when {
            et_phone.isEmpty(getString(R.string.error_empty_phone)) -> return
            et_phone.isEqualLength(10, getString(R.string.error_equal_length, 10)) -> return
            et_phone.isVerifyPhone(getString(R.string.error_phone)) -> return
        }

        progress_bar.show()
        val phoneNumber = et_phone.getContents()
        val validatePhone = ValidatePhone(phoneNumber)
        viewModel.callValidatePhone(validatePhone)
    }

    private fun dialogValidatePhone(message: String) = AlertDialog.Builder(this).apply {
        setTitle(R.string.validate_phone)
        setMessage(message)
        setPositiveButton(android.R.string.ok) { dialog, which ->
            dialog.dismiss()
        }
        setCancelable(false)
        show()
    }

}
