package com.chococard.carwash.ui.signin

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.ui.signup.SignUpActivity
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.SignInViewModel
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
        // set widgets
        validateSignIn()

        //event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        bt_sign_in.setOnClickListener { callSignIn() }

        tv_sign_up.setOnClickListener {
            startActivity<SignUpActivity> {
                finish()
            }
        }

        et_password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) callSignIn()
            false
        }

        root_layout.setOnClickListener {
            hideSoftKeyboard()
        }

        // toggle password
        iv_toggle_password.setOnClickListener {
            iv_toggle_password setTogglePassword et_password
        }

        et_password.addTextChangedListener {
            validateSignIn()
            et_password setTogglePassword iv_toggle_password
        }

        et_username.addTextChangedListener {
            validateSignIn()
        }

        //observe
        viewModel.getSignIn.observe { response ->
            val (success, message, _, _) = response
            progress_bar.hide()
            if (success) {
                startActivity<MainActivity> {
                    finishAffinity()
                }
            } else {
                root_layout.snackbar(message)
            }
        }

        viewModel.errorMessage.observe {
            progress_bar.hide()
            root_layout.snackbar(it)
        }

        viewModel.validateSignIn.observe {
            if (it) bt_sign_in.ready() else bt_sign_in.unready()
        }

        viewModel.state.observe { state ->
            if (state.loading) progress_bar.show() else progress_bar.hide()
        }

        viewModel.error.observeError()
    }

    private fun validateSignIn() {
        val username = et_username.getContents()
        val password = et_password.getContents()
        viewModel.validateSignIn(username, password)
    }

    private fun callSignIn() {
        progress_bar.show()
        val username = et_username.getContents()
        val password = et_password.getContents()
        viewModel.callSignIn(username, password)
    }

}
