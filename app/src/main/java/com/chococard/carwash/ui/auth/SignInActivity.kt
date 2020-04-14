package com.chococard.carwash.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.AuthApi
import com.chococard.carwash.data.repositories.AuthRepository
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.util.base.BaseActivity
import com.chococard.carwash.util.extension.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity<AuthViewModel, AuthFactory>() {

    override fun viewModel() = AuthViewModel::class.java

    override fun factory() = AuthFactory(AuthRepository(AuthApi.invoke(interceptor)))

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
            Intent(baseContext, SignUpActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        //observe
        viewModel.signIn.observe(this, Observer { response ->
            progress_bar.hide()
            if (response.success) {
                response.token?.let { writePref(R.string.token, it) }
                Intent(baseContext, MainActivity::class.java).also {
                    startActivity(it)
                    finishAffinity()
                }
            } else {
                response.message?.let { toast(it) }
            }
        })

        viewModel.exception.observe(this, Observer {
            progress_bar.hide()
            toast(it)
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
        viewModel.signIn(et_username.getContents(), et_password.getContents())
    }

}
