package com.chococard.carwash.ui.signin

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.ui.requestotp.RequestOtpActivity
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.SignInViewModel
import com.chococard.carwash.viewmodel.ValidateSignIn
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInActivity : BaseActivity() {

    val viewModel by viewModel<SignInViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        init()
    }

    private fun init() {
        //observe
        viewModel.state.observe { state ->
            if (state.loading) progress_bar.show() else progress_bar.hide()
            if (state.isValidUsername && state.isValidPassword) bt_sign_in.ready() else bt_sign_in.unready()
        }

        viewModel.getSignIn.observe { response ->
            val (success, message, _, _) = response
            if (success) {
                startActivity<MainActivity> {
                    finishAffinity()
                }
            } else {
                root_layout.snackbar(message)
            }
        }

        viewModel.onSignIn.observe {
            when (it) {
                ValidateSignIn.USERNAME_EMPTY ->
                    root_layout.snackbar(getString(R.string.error_empty_username))
                ValidateSignIn.USERNAME_INCORRECT ->
                    root_layout.snackbar(getString(R.string.error_least_length, 4))
                ValidateSignIn.PASSWORD_EMPTY ->
                    root_layout.snackbar(getString(R.string.error_empty_password))
                ValidateSignIn.PASSWORD_INCORRECT ->
                    root_layout.snackbar(getString(R.string.error_least_length, 8))
                else -> viewModel.callSignIn()
            }
        }

        viewModel.error.observeError()

        et_username.addTextChangedListener { viewModel.setUsername(it.toString()) }

        et_password.addTextChangedListener {
            viewModel.setPassword(it.toString())
            et_password setTogglePassword iv_toggle_password
        }

        // toggle password
        iv_toggle_password.setOnClickListener {
            iv_toggle_password setTogglePassword et_password
        }

        //event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        bt_sign_in.setOnClickListener { viewModel.onSignIn() }

        tv_sign_up.setOnClickListener {
            startActivity<RequestOtpActivity> {
                finish()
            }
        }

        et_password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) viewModel.onSignIn()
            false
        }

        root_layout.setOnClickListener { hideSoftKeyboard() }
    }

}
