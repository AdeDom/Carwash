package com.chococard.carwash.ui.requestotp

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.chococard.carwash.R
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
        root_layout.setOnClickListener {
            hideSoftKeyboard()
        }

        et_phone.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) callRequestOtp()
            false
        }

        bt_request_otp.setOnClickListener {
            callRequestOtp()
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

        viewModel.errorMessage.observe(this, Observer {
            progress_bar.hide()
            root_layout.snackbar(it)
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            dialogError(it)
        })
    }

    private fun callRequestOtp() {
        progress_bar.show()
        val phoneNumber = et_phone.getContents()
        viewModel.callValidatePhone(phoneNumber)
    }

    private fun dialogValidatePhone(message: String) = AlertDialog.Builder(this).apply {
        setTitle(R.string.validate_phone)
        setMessage(message)
        setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }
        setCancelable(false)
        show()
    }

}
