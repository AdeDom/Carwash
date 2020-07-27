package com.chococard.carwash.ui.requestotp

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.verifyotp.OtpSignUpActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.RequestOtpViewModel
import kotlinx.android.synthetic.main.activity_request_otp.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequestOtpActivity : BaseActivity() {

    val viewModel by viewModel<RequestOtpViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_otp)

        init()
    }

    private fun init() {
        // set widget
        viewModel.validatePhone(et_phone.getContents())

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

        et_phone.onTextChanged { viewModel.validatePhone(et_phone.getContents()) }

        // observe
        viewModel.getValidatePhone.observe { response ->
            val (success, message) = response
            progress_bar.hide()
            if (success) {
                val phoneNumber = et_phone.getContents()
                startActivity<OtpSignUpActivity> { intent ->
                    intent.putExtra(CommonsConstant.PHONE, phoneNumber)
                    finish()
                }
            } else {
                message?.let { dialogValidatePhone(it) }
            }
        }

        viewModel.errorMessage.observe {
            progress_bar.hide()
            root_layout.snackbar(it)
        }

        viewModel.validatePhone.observe {
            if (it) bt_request_otp.ready() else bt_request_otp.unready()
        }

        viewModel.getError.observe {
            progress_bar.hide()
            dialogError(it)
        }
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
