package com.chococard.carwash.ui.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.signin.SignInActivity
import com.chococard.carwash.ui.splashscreen.SplashScreenActivity
import com.chococard.carwash.util.CommonsConstant
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
        // get value intent
        val phoneNumber = intent.getStringExtra(CommonsConstant.PHONE)
        if (phoneNumber == null) finish() else et_phone.setText(phoneNumber)

        // set widgets
        validateSignUp()

        //event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        iv_photo.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE) }

        iv_camera.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE) }

        card_remove_profile.setOnClickListener {
            viewModel.setValueFileUri(null)
        }

        // text changed
        et_username.onTextChanged { validateSignUp() }

        et_full_name.onTextChanged { validateSignUp() }

        et_identity_card.onTextChanged { validateSignUp() }

        et_phone.onTextChanged { validateSignUp() }

        et_password.onTextChanged {
            validateSignUp()
            et_password setTogglePassword iv_toggle_password
        }

        iv_toggle_password.setOnClickListener {
            iv_toggle_password setTogglePassword et_password
        }

        et_re_password.onTextChanged {
            validateSignUp()
            et_re_password setTogglePassword iv_toggle_re_password
        }

        iv_toggle_re_password.setOnClickListener {
            iv_toggle_re_password setTogglePassword et_re_password
        }

        root_layout.setOnClickListener {
            hideSoftKeyboard()
        }

        et_identity_card.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) callSignUp()
            false
        }

        bt_sign_up.setOnClickListener { callSignUp() }

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
            if (success) dialogContactAdmin() else root_layout.snackbar(message)
        })

        viewModel.errorMessage.observe(this, Observer {
            progress_bar.hide()
            root_layout.snackbar(it)
        })

        viewModel.validateSignUp.observe(this, Observer {
            if (it) bt_sign_up.ready() else bt_sign_up.unready()
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            dialogError(it)
        })
    }

    private fun validateSignUp() {
        val username = et_username.getContents()
        val password = et_password.getContents()
        val rePassword = et_re_password.getContents()
        val fullName = et_full_name.getContents()
        val identityCard = et_identity_card.getContents()
        val phone = et_phone.getContents()
        viewModel.validateSignUp(username, password, rePassword, fullName, identityCard, phone)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonsConstant.REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null)
            viewModel.setValueFileUri(data.data)
    }

    private fun callSignUp() {
        progress_bar.show()
        val username = et_username.getContents()
        val password = et_password.getContents()
        val rePassword = et_re_password.getContents()
        val fullName = et_full_name.getContents()
        val identityCard = et_identity_card.getContents()
        val phone = et_phone.getContents()
        viewModel.callSignUp(username, password, rePassword, fullName, identityCard, phone)
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
