package com.chococard.carwash.ui.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.signin.SignInActivity
import com.chococard.carwash.ui.splashscreen.SplashScreenActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.SignUpViewModel
import com.chococard.carwash.viewmodel.ValidateSignUp
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : BaseActivity() {

    val viewModel by viewModel<SignUpViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        init()
    }

    private fun init() {
        // get value intent
        val phoneNumber = intent.getStringExtra(CommonsConstant.PHONE)
        if (phoneNumber == null) finish() else et_phone.setText(phoneNumber)

        viewModel.setPhoneNumber(phoneNumber!!)

        //event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        iv_photo.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE) }

        iv_camera.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE) }

        card_remove_profile.setOnClickListener { viewModel.setImage("") }

        iv_toggle_password.setOnClickListener {
            iv_toggle_password setTogglePassword et_password
        }

        iv_toggle_re_password.setOnClickListener {
            iv_toggle_re_password setTogglePassword et_re_password
        }

        root_layout.setOnClickListener {
            hideSoftKeyboard()
        }

        et_identity_card.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) viewModel.onSignUp()
            false
        }

        bt_sign_up.setOnClickListener { viewModel.onSignUp() }

        tv_sign_in.setOnClickListener {
            startActivity<SignInActivity> {
                finish()
            }
        }

        //observe
        viewModel.getSignUp.observe { response ->
            val (success, message) = response
            progress_bar.hide()
            if (success) dialogContactAdmin() else root_layout.snackbar(message)
        }

        viewModel.onSignUp.observe {
            when (it) {
                ValidateSignUp.USERNAME_EMPTY ->
                    root_layout.snackbar(getString(R.string.error_empty_username))
                ValidateSignUp.USERNAME_INCORRECT ->
                    root_layout.snackbar(getString(R.string.error_least_length, 4))
                ValidateSignUp.PASSWORD_EMPTY ->
                    root_layout.snackbar(getString(R.string.error_empty_password))
                ValidateSignUp.PASSWORD_INCORRECT ->
                    root_layout.snackbar(getString(R.string.error_least_length, 8))
                ValidateSignUp.RE_PASSWORD_EMPTY ->
                    root_layout.snackbar(getString(R.string.error_empty_re_password))
                ValidateSignUp.RE_PASSWORD_INCORRECT ->
                    root_layout.snackbar(getString(R.string.error_least_length, 8))
                ValidateSignUp.PASSWORD_NOT_MATCHED ->
                    root_layout.snackbar(getString(R.string.error_matched))
                ValidateSignUp.FULL_NAME_EMPTY ->
                    root_layout.snackbar(getString(R.string.error_empty_name))
                ValidateSignUp.IDENTITY_CARD_EMPTY ->
                    root_layout.snackbar(getString(R.string.error_empty_identity_card))
                ValidateSignUp.IDENTITY_CARD_TOTAL_13 ->
                    root_layout.snackbar(getString(R.string.error_equal_length, 13))
                ValidateSignUp.IDENTITY_CARD_INCORRECT ->
                    root_layout.snackbar(getString(R.string.error_identity_card))
                ValidateSignUp.PHONE_EMPTY ->
                    root_layout.snackbar(getString(R.string.error_empty_phone))
                ValidateSignUp.PHONE_TOTAL_10 ->
                    root_layout.snackbar(getString(R.string.error_equal_length, 10))
                ValidateSignUp.PHONE_INCORRECT ->
                    root_layout.snackbar(getString(R.string.error_phone))
                ValidateSignUp.IMAGE_EMPTY ->
                    root_layout.snackbar(getString(R.string.please_select_profile_picture))
                else -> viewModel.callSignUp()
            }
        }

        viewModel.state.observe { state ->
            // progress bar
            if (state.loading) progress_bar.show() else progress_bar.hide()

            // button
            if (state.isValidUsername && state.isValidPassword && state.isValidRePassword &&
                state.isValidFullName && state.isValidIdentityCard && state.isValidPhoneNumber &&
                state.isValidImage
            ) {
                bt_sign_up.ready()
            } else {
                bt_sign_up.unready()
            }

            // image
            if (state.isValidImage) {
                card_remove_profile.show()
                iv_photo.setImageCircle(state.image)
            } else {
                card_remove_profile.hide()
                iv_photo.setImageResource(R.drawable.ic_user)
            }
        }

        viewModel.error.observeError()

        et_username.addTextChangedListener { viewModel.setUsername(it.toString()) }

        et_password.addTextChangedListener {
            viewModel.setPassword(it.toString())
            et_password setTogglePassword iv_toggle_password
        }

        et_re_password.addTextChangedListener {
            viewModel.setRePassword(it.toString())
            et_re_password setTogglePassword iv_toggle_re_password
        }

        et_full_name.addTextChangedListener { viewModel.setFullName(it.toString()) }

        et_identity_card.addTextChangedListener { viewModel.setIdentityCard(it.toString()) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonsConstant.REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null)
            viewModel.setImage(data.data.toString())
    }

    private fun dialogContactAdmin() = AlertDialog.Builder(this).apply {
        setTitle(R.string.contact_admin)
        setMessage(R.string.please_contact_car_wash)
        setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
            startActivity<SplashScreenActivity> {
                finishAffinity()
            }
        }
        setCancelable(false)
        show()
    }

}
