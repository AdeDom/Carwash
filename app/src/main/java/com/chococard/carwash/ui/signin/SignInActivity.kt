package com.chococard.carwash.ui.signin

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.request.SignInRequest
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.ui.signup.SignUpActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.SignInViewModel
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInActivity : BaseActivity() {

    val viewModel: SignInViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        init()
    }

    private fun init() {
        //event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        bt_sign_in.setOnClickListener { signIn() }

        tv_sign_up.setOnClickListener {
            startActivity<SignUpActivity> {
                finish()
            }
        }

        et_password.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) signIn()
            false
        }

        root_layout.setOnClickListener {
            hideSoftKeyboard()
        }

        iv_toggle_password.tag = FlagConstant.TOGGLE_PASSWORD_ON
        iv_toggle_password.setOnClickListener {
            iv_toggle_password setTogglePassword et_password
        }

        et_password.onTextChanged {
            et_password setTogglePassword iv_toggle_password
        }

        //observe
        viewModel.getSignIn.observe(this, Observer { response ->
            val (success, message, token, refreshToken) = response
            progress_bar.hide()
            if (success) {
                token?.let { writePref(CommonsConstant.TOKEN, it) }
                refreshToken?.let { writePref(CommonsConstant.REFRESH_TOKEN, it) }
                startActivity<MainActivity> {
                    finishAffinity()
                }
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            dialogError(it)
        })
    }

    private fun signIn() {
        when {
            et_username.isEmpty(getString(R.string.error_empty_username)) -> return
            et_username.isMinLength(4, getString(R.string.error_least_length, 4)) -> return
            et_password.isEmpty(getString(R.string.error_empty_password)) -> return
            et_password.isMinLength(8, getString(R.string.error_least_length, 8)) -> return
        }

        progress_bar.show()
        val signIn = SignInRequest(et_username.getContents(), et_password.getContents())
        viewModel.callSignIn(signIn)
    }

}
