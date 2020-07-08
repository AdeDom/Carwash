package com.chococard.carwash.ui.signin

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.ui.signup.SignUpActivity
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

        iv_toggle_password.setOnClickListener {
            iv_toggle_password setTogglePassword et_password
        }

        et_password.onTextChanged {
            et_password setTogglePassword iv_toggle_password
        }

        //observe
        viewModel.getSignIn.observe(this, Observer { response ->
            val (success, message, _, _) = response
            progress_bar.hide()
            if (success) {
                startActivity<MainActivity> {
                    finishAffinity()
                }
            } else {
                root_layout.snackbar(message)
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

    private fun callSignIn() {
        progress_bar.show()
        val username = et_username.getContents()
        val password = et_password.getContents()
        viewModel.callSignIn(username, password)
    }

}
