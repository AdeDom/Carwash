package com.chococard.carwash.ui.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.signin.SignInActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.SignUpViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : BaseActivity() {

    val viewModel: SignUpViewModel by viewModel()
    private var mPhoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        init()
    }

    private fun init() {
        // set widgets
        mPhoneNumber = intent.getStringExtra(CommonsConstant.PHONE)
        if (mPhoneNumber == null) finish() else et_phone.setText(mPhoneNumber)

        //event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        iv_photo.setOnClickListener { selectImage() }

        iv_camera.setOnClickListener { selectImage() }

        bt_sign_up.setOnClickListener { signUp() }

        tv_sign_in.setOnClickListener {
            startActivity<SignInActivity> {
                finish()
            }
        }

        //observe
        viewModel.getSignUp.observe(this, Observer { response ->
            val (success, message) = response
            progress_bar.hide()
            message?.let { toast(it) }
            if (success) {
                startActivity<SignInActivity> {
                    finish()
                }
            }
        })

        viewModel.getUpload.observe(this, Observer {
            progress_bar.hide()
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            dialogError(it)
        })
    }

    private fun selectImage() = Intent(Intent.ACTION_PICK).apply {
        type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(this, CommonsConstant.REQUEST_CODE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonsConstant.REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val fileUri = data.data!!
            iv_photo.setImageCircle(fileUri.toString())
            uploadFile(fileUri) { body, description ->
                progress_bar.show()
                viewModel.callUploadImageFile(body, description)
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
            et_password.isMatched(et_re_password, getString(R.string.error_matched)) -> return
            et_identity_card.isEmpty(getString(R.string.error_empty_identity_card)) -> return
            et_identity_card.isEqualLength(13, getString(R.string.error_equal_length, 13)) -> return
            et_identity_card.isVerifyIdentityCard(getString(R.string.error_identity_card)) -> return
            et_phone.isEmpty(getString(R.string.error_empty_phone)) -> return
            et_phone.isEqualLength(10, getString(R.string.error_equal_length, 10)) -> return
            et_phone.isVerifyPhone(getString(R.string.error_phone)) -> return
        }

        progress_bar.show()
        viewModel.callSignUp(
            et_full_name.getContents(),
            et_username.getContents(),
            et_password.getContents(),
            et_identity_card.getContents(),
            et_phone.getContents()
        )
    }

}
