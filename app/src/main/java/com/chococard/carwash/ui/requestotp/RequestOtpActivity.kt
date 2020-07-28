package com.chococard.carwash.ui.requestotp

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.verifyotp.OtpSignUpActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.RequestOtpViewModel
import com.chococard.carwash.viewmodel.ValidatePhoneNumber
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
        // observe
        viewModel.state.observe { state ->
            if (state.loading) progress_bar.show() else progress_bar.hide()
            if (state.isValidPhoneNumber) bt_request_otp.ready() else bt_request_otp.unready()
        }

        viewModel.getValidatePhone.observe { response ->
            val (success, message) = response
            if (success) {
                val phoneNumber = et_phone.getContents()
                startActivity<OtpSignUpActivity> { intent ->
                    intent.putExtra(CommonsConstant.PHONE, phoneNumber)
                    finish()
                }
            } else {
                dialogPositive(R.string.validate_phone, message) { it.dismiss() }
            }
        }

        viewModel.onValidatePhone.observe {
            when (it) {
                ValidatePhoneNumber.PHONE_EMPTY ->
                    root_layout.snackbar(getString(R.string.error_empty_phone))
                ValidatePhoneNumber.PHONE_TOTAL_10 ->
                    root_layout.snackbar(getString(R.string.error_equal_length, 10))
                ValidatePhoneNumber.PHONE_INCORRECT ->
                    root_layout.snackbar(getString(R.string.error_phone))
                else -> viewModel.callValidatePhone()
            }
        }

        viewModel.error.observeError()

        et_phone.addTextChangedListener { viewModel.setPhoneNumber(it.toString()) }

        //set event
        root_layout.setOnClickListener { hideSoftKeyboard() }

        et_phone.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) viewModel.onValidatePhone()
            false
        }

        bt_request_otp.setOnClickListener { viewModel.onValidatePhone() }
    }

}
