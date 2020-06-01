package com.chococard.carwash.ui.signup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : BaseActivity() {

    val viewModel: SignUpViewModel by viewModel()
    private var mPhoneNumber: String? = null
    private var mFileUri: Uri? = null

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
            if (success) {
                dialogContactAdmin()
            } else {
                message?.let { toast(it) }
            }
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
            mFileUri = data.data!!
            iv_photo.setImageCircle(mFileUri.toString())
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
            mFileUri == null -> {
                toast(getString(R.string.please_select_profile_picture), Toast.LENGTH_LONG)
                return
            }
        }

        progress_bar.show()
        val username = RequestBody.create(MultipartBody.FORM, et_username.getContents())
        val password = RequestBody.create(MultipartBody.FORM, et_password.getContents())
        val fullName = RequestBody.create(MultipartBody.FORM, et_full_name.getContents())
        val identityCard = RequestBody.create(MultipartBody.FORM, et_identity_card.getContents())
        val phone = RequestBody.create(MultipartBody.FORM, et_phone.getContents())
        val role = RequestBody.create(MultipartBody.FORM, FlagConstant.EMPLOYEE.toString())
        val multipartBody = convertToMultipartBody(mFileUri!!)
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
