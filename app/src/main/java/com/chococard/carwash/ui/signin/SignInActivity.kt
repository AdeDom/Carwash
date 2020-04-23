package com.chococard.carwash.ui.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.AppService
import com.chococard.carwash.factory.SignInFactory
import com.chococard.carwash.repositories.BaseRepository
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.main.MainActivity
import com.chococard.carwash.ui.signup.SignUpActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.SignInViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity<SignInViewModel, SignInFactory>() {

    override fun viewModel() = SignInViewModel::class.java

    override fun factory() = SignInFactory(BaseRepository(AppService.invoke(connectionInterceptor)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        init()
    }

    private fun init() {
        //set widget
        val username = readPref(CommonsConstant.USERNAME)
        et_username.setText(username)

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
        viewModel.getSignIn.observe(this, Observer { response ->
            val (success, message, token) = response
            progress_bar.hide()
            if (success) {
                token?.let { writePref(CommonsConstant.TOKEN, it) }
                progress_bar.show()
                viewModel.callFetchUser()
            } else {
                message?.let { toast(it, Toast.LENGTH_LONG) }
            }
        })

        viewModel.getUser.observe(this, Observer { response ->
            val (success, message, user) = response
            progress_bar.hide()
            if (success) {
                writePref(CommonsConstant.USERNAME, et_username.getContents())
                writePref(CommonsConstant.USER, Gson().toJson(user))
                Intent(baseContext, MainActivity::class.java).apply {
                    startActivity(this)
                    finishAffinity()
                }
            } else {
                message?.let { toast(it) }
            }
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            toast(it, Toast.LENGTH_LONG)
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
        viewModel.callSignIn(et_username.getContents(), et_password.getContents())
    }

}
