package com.chococard.carwash.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.AuthApi
import com.chococard.carwash.data.repositories.AuthRepository
import com.chococard.carwash.util.base.BaseActivity
import com.chococard.carwash.util.extension.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity<AuthViewModel, AuthFactory>() {

    override fun viewModel() = AuthViewModel::class.java

    override fun factory() = AuthFactory(AuthRepository(AuthApi.invoke(interceptor)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        init()
    }

    private fun init() {
        //event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        iv_photo.setOnClickListener { selectImage() }

        iv_camera.setOnClickListener { selectImage() }

        bt_sign_up.setOnClickListener { signUp() }

        tv_sign_in.setOnClickListener {
            Intent(baseContext, SignInActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        //observe
        viewModel.signUp.observe(this, Observer { response ->
            val (success, message) = response
            progress_bar.hide()
            message?.let { toast(it) }
            if (success) {
                Intent(baseContext, SignInActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        })

        viewModel.upload.observe(this, Observer {
            progress_bar.hide()
        })

        viewModel.exception.observe(this, Observer {
            progress_bar.hide()
            toast(it, Toast.LENGTH_LONG)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val fileUri = data.data!!
            iv_photo.loadCircle(fileUri.toString())
            uploadFile(fileUri) { body, description ->
                progress_bar.show()
                viewModel.uploadImageFile(body, description)
            }
        }
    }

    private fun signUp() {
        when {
            et_full_name.isEmpty(getString(R.string.error_empty_name)) -> return
            et_username.isEmpty(getString(R.string.error_empty_username)) -> return
            et_username.isMinLength(4, getString(R.string.error_least_length, 4)) -> return
            et_password.isEmpty(getString(R.string.error_empty_password)) -> return
            et_password.isMinLength(8, getString(R.string.error_least_length, 8)) -> return
            et_re_password.isEmpty(getString(R.string.error_empty_re_password)) -> return
            et_re_password.isMinLength(8, getString(R.string.error_least_length, 8)) -> return
            et_password.isMatching(et_re_password, getString(R.string.error_matching)) -> return
            et_identity_card.isEmpty(getString(R.string.error_empty_identity_card)) -> return
            et_identity_card.isEqualLength(13, getString(R.string.error_equal_length, 13)) -> return
            viewModel.isIdentityCard(et_identity_card.getContents()) -> {
                et_identity_card.failed(getString(R.string.error_identity_card))
                return
            }
            et_phone.isEmpty(getString(R.string.error_empty_phone)) -> return
            et_phone.isEqualLength(10, getString(R.string.error_equal_length, 10)) -> return
            viewModel.isTelephoneNumber(et_phone.getContents()) -> {
                et_phone.failed(getString(R.string.error_phone))
                return
            }
        }

        progress_bar.show()
        viewModel.signUp(
            et_full_name.getContents(),
            et_username.getContents(),
            et_password.getContents(),
            et_identity_card.getContents(),
            et_phone.getContents()
        )
    }

}
