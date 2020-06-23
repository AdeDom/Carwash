package com.chococard.carwash.ui.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.signin.SignInActivity
import com.chococard.carwash.ui.splashscreen.SplashScreenActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.FlagConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.SignUpViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : BaseActivity() {

    val viewModel: SignUpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        init()
    }

    private fun init() {
        // set widgets
        val phoneNumber = intent.getStringExtra(CommonsConstant.PHONE)
        if (phoneNumber == null) finish() else et_phone.setText(phoneNumber)

        //event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        iv_photo.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE) }

        iv_camera.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE) }

        card_remove_profile.setOnClickListener {
            viewModel.setValueFileUri(null)
        }

        et_password.onTextChanged {
            et_password setTogglePassword iv_toggle_password
        }

        iv_toggle_password.setOnClickListener {
            iv_toggle_password setTogglePassword et_password
        }

        et_re_password.onTextChanged {
            et_re_password setTogglePassword iv_toggle_re_password
        }

        iv_toggle_re_password.setOnClickListener {
            iv_toggle_re_password setTogglePassword et_re_password
        }

        root_layout.setOnClickListener {
            hideSoftKeyboard()
        }

        et_identity_card.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) signUp()
            false
        }

        bt_sign_up.setOnClickListener { signUp() }

        tv_sign_in.setOnClickListener {
            startActivity<SignInActivity> {
                finish()
            }
        }

        //observe
        viewModel.getFileUri.observe(this, Observer { uri ->
            if (uri == null) {
                card_remove_profile.hide()
                iv_photo.setImageResource(R.drawable.ic_user)
            } else {
                card_remove_profile.show()
                iv_photo.setImageCircle(uri.toString())
            }
        })

        viewModel.getSignUp.observe(this, Observer { response ->
            val (success, message) = response
            progress_bar.hide()
            if (success) {
                dialogContactAdmin()
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            dialogError(it)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonsConstant.REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            viewModel.setValueFileUri(data.data)
        }
    }

    private fun signUp() {
        when {
            et_username.isEmpty(getString(R.string.error_empty_username)) -> return
            et_username.isMinLength(4, getString(R.string.error_least_length, 4)) -> return
            et_password.isEmpty(getString(R.string.error_empty_password)) -> return
            et_password.isMinLength(8, getString(R.string.error_least_length, 8)) -> return
            et_re_password.isEmpty(getString(R.string.error_empty_re_password)) -> return
            et_re_password.isMinLength(8, getString(R.string.error_least_length, 8)) -> return
            et_password.isMatched(et_re_password, getString(R.string.error_matched)) -> return
            et_full_name.isEmpty(getString(R.string.error_empty_name)) -> return
            et_identity_card.isEmpty(getString(R.string.error_empty_identity_card)) -> return
            et_identity_card.isEqualLength(13, getString(R.string.error_equal_length, 13)) -> return
            et_identity_card.isVerifyIdentityCard(getString(R.string.error_identity_card)) -> return
            et_phone.isEmpty(getString(R.string.error_empty_phone)) -> return
            et_phone.isEqualLength(10, getString(R.string.error_equal_length, 10)) -> return
            et_phone.isVerifyPhone(getString(R.string.error_phone)) -> return
            viewModel.isValueFileUri() -> {
                toast(getString(R.string.please_select_profile_picture), Toast.LENGTH_LONG)
                return
            }
        }

        progress_bar.show()
        val username = et_username.getContents().toRequestBody()
        val password = et_password.getContents().toRequestBody()
        val fullName = et_full_name.getContents().toRequestBody()
        val identityCard = et_identity_card.getContents().toRequestBody()
        val phone = et_phone.getContents().toRequestBody()
        val role = FlagConstant.EMPLOYEE.toRequestBody()
        val multipartBody = convertToMultipartBody(viewModel.getValueFileUri()!!)
        viewModel.callSignUp(username, password, fullName, identityCard, phone, role, multipartBody)

    }

    private fun dialogContactAdmin() = AlertDialog.Builder(this).apply {
        setTitle(R.string.contact_admin)
        setMessage(R.string.please_contact_car_wash)
        setPositiveButton(android.R.string.ok) { dialog, which ->
            dialog.dismiss()
            startActivity<SplashScreenActivity> {
                finishAffinity()
            }
        }
        setCancelable(false)
        show()
    }

}
