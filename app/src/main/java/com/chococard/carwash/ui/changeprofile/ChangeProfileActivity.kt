package com.chococard.carwash.ui.changeprofile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.request.ChangePhoneRequest
import com.chococard.carwash.data.networks.request.ValidatePhoneRequest
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.changepassword.ChangePasswordActivity
import com.chococard.carwash.ui.splashscreen.SplashScreenActivity
import com.chococard.carwash.ui.verifyphone.VPChangeProfileActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ChangeProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeProfileActivity : BaseActivity() {

    val viewModel: ChangeProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        init()
    }

    private fun init() {
        setToolbar(toolbar)

        //set widgets
        viewModel.getDbUser.observe(this, Observer { user ->
            if (user == null) return@Observer
            val (_, _, _, phone, _, image) = user
            et_phone.setText(phone)
            et_phone.setSelection(et_phone.length())
            iv_photo.setImageCircle(image)
        })

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
            if (actionId == EditorInfo.IME_ACTION_DONE) validatePhone()
            false
        }

        bt_cancel.setOnClickListener { finish() }

        bt_confirm.setOnClickListener { validatePhone() }

        //observe
        viewModel.getChangeImageProfile.observe(this, Observer { response ->
            val (success, message) = response
            progress_bar.hide()
            if (!success) toast(message, Toast.LENGTH_LONG)
        })

        viewModel.getChangePhone.observe(this, Observer { response ->
            progress_bar.hide()
            val (_, message) = response
            toast(message)
        })

        viewModel.getLogout.observe(this, Observer { response ->
            val (success, message) = response
            if (success) {
                writePref(CommonsConstant.ACCESS_TOKEN, "")
                writePref(CommonsConstant.REFRESH_TOKEN, "")
                startActivity<SplashScreenActivity> {
                    finishAffinity()
                }
            } else {
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getValidatePhone.observe(this, Observer { response ->
            progress_bar.hide()
            val (success, message) = response
            if (success) {
                changeProfile()
            } else {
                toast(message)
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
            val fileUri = data.data!!
            card_remove_profile.show()
            iv_photo.setImageCircle(fileUri.toString())
            progress_bar.show()
            val multipartBody = convertToMultipartBody(fileUri)
            viewModel.callChangeImageProfile(multipartBody)
        } else if (requestCode == CommonsConstant.REQUEST_CODE_VERIFY_PHONE && resultCode == Activity.RESULT_OK) {
            progress_bar.show()
            val phoneNumber = et_phone.getContents()
            val changePhone = ChangePhoneRequest(phoneNumber)
            viewModel.callChangePhone(changePhone)
        }
    }

    private fun validatePhone() {
        when {
            et_phone.isEmpty(getString(R.string.error_empty_phone)) -> return
            et_phone.isEqualLength(10, getString(R.string.error_equal_length, 10)) -> return
            et_phone.isVerifyPhone(getString(R.string.error_phone)) -> return
        }

        progress_bar.show()
        val phoneNumber = et_phone.getContents()
        val validatePhone = ValidatePhoneRequest(phoneNumber)
        viewModel.callValidatePhone(validatePhone)
    }

    private fun changeProfile() {
        val phoneNumber = et_phone.getContents()
        Intent(baseContext, VPChangeProfileActivity::class.java).apply {
            putExtra(CommonsConstant.PHONE, phoneNumber)
            startActivityForResult(this, CommonsConstant.REQUEST_CODE_VERIFY_PHONE)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_change_password -> {
                startActivity<ChangePasswordActivity> {
                    finish()
                }
            }
            R.id.option_contact_admin -> dialogContactAdmin { startActivityActionDial() }
            R.id.option_logout -> dialogLogout {
                FirebaseAuth.getInstance().signOut()
                viewModel.callLogout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
