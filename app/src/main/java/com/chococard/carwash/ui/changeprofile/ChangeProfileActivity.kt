package com.chococard.carwash.ui.changeprofile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.request.ChangePhoneRequest
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.changepassword.ChangePasswordActivity
import com.chococard.carwash.ui.splashscreen.SplashScreenActivity
import com.chococard.carwash.ui.verifyotp.OtpChangeProfileActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ChangeProfileViewModel
import kotlinx.android.synthetic.main.activity_change_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeProfileActivity : BaseActivity() {

    val viewModel by viewModel<ChangeProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        init()
    }

    private fun init() {
        setToolbar(toolbar)

        //set widgets
        viewModel.getDbUser.observe { user ->
            val (_, _, _, phone, _, image) = user
            et_phone.setText(phone)
            et_phone.setSelection(et_phone.length())
            iv_photo.setImageCircle(image)

            viewModel.setValueUser(user)
        }

        //set event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        iv_photo.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE) }

        iv_camera.setOnClickListener { selectImage(CommonsConstant.REQUEST_CODE_IMAGE) }

        card_remove_profile.setOnClickListener {
            iv_photo.setImageResource(R.drawable.ic_user)
            card_remove_profile.hide()
        }

        root_layout.setOnClickListener { hideSoftKeyboard() }

        et_phone.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) callValidatePhone()
            false
        }

        bt_cancel.setOnClickListener { finish() }

        bt_confirm.setOnClickListener { callValidatePhone() }

        et_phone.addTextChangedListener { viewModel.setValueValidatePhone(it.toString()) }

        //observe
        viewModel.state.observe { state ->
            if (state.loading) progress_bar.show() else progress_bar.hide()
        }

        viewModel.getChangeImageProfile.observe { response ->
            val (success, message) = response
            if (!success) root_layout.snackbar(message)
        }

        viewModel.getChangePhone.observe { response ->
            val (_, message) = response
            root_layout.snackbar(message)
        }

        viewModel.getLogout.observe { response ->
            val (success, message) = response
            if (success) {
                startActivity<SplashScreenActivity> {
                    finishAffinity()
                }
            } else {
                root_layout.snackbar(message)
            }
        }

        viewModel.getValidatePhone.observe { response ->
            val (success, message) = response
            if (success) changeProfile() else root_layout.snackbar(message)
        }

        viewModel.validatePhone.observe {
            if (it) bt_confirm.ready() else bt_confirm.unready()
        }

        viewModel.errorMessage.observe {
            root_layout.snackbar(it)
        }

        viewModel.error.observeError()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonsConstant.REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val fileUri = data.data!!
            card_remove_profile.show()
            iv_photo.setImageCircle(fileUri.toString())
            val multipartBody = convertToMultipartBody(fileUri)
            viewModel.callChangeImageProfile(multipartBody)
        } else if (requestCode == CommonsConstant.REQUEST_CODE_VERIFY_PHONE && resultCode == Activity.RESULT_OK) {
            val phoneNumber = et_phone.getContents()
            val changePhone = ChangePhoneRequest(phoneNumber)
            viewModel.callChangePhone(changePhone)
        }
    }

    private fun callValidatePhone() {
        val phoneNumber = et_phone.getContents()
        viewModel.callValidatePhone(phoneNumber)
    }

    private fun changeProfile() {
        val phoneNumber = et_phone.getContents()
        Intent(baseContext, OtpChangeProfileActivity::class.java).apply {
            putExtra(CommonsConstant.PHONE, phoneNumber)
            startActivityForResult(this, CommonsConstant.REQUEST_CODE_VERIFY_PHONE)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_change_password -> startActivity<ChangePasswordActivity> { finish() }
            R.id.option_contact_admin -> dialogContactAdmin { startActivityActionDial() }
            R.id.option_logout -> dialogLogout { viewModel.callLogout() }
        }
        return super.onOptionsItemSelected(item)
    }

}
