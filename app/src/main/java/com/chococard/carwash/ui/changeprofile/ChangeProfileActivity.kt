package com.chococard.carwash.ui.changeprofile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.request.ChangePhoneRequest
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

// TODO: 29/05/2563 re-check get db user info from room database
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
            iv_photo.setImageCircle(image)
        })

        //set event
        iv_arrow_back.setOnClickListener { onBackPressed() }
        iv_photo.setOnClickListener { selectImage() }
        iv_camera.setOnClickListener { selectImage() }
        bt_cancel.setOnClickListener { finish() }
        bt_confirm.setOnClickListener { changeProfile() }

        //observe
        viewModel.getUpload.observe(this, Observer {
            progress_bar.hide()
        })

        viewModel.getChangeProfile.observe(this, Observer { response ->
            val (success, message) = response
            progress_bar.hide()
            message?.let { toast(it) }
            if (success) {
                progress_bar.show()
                viewModel.callFetchUser()
            }
        })

        viewModel.getUser.observe(this, Observer { response ->
            val (success, message, _) = response
            progress_bar.hide()
            if (success) {
                finish()
            } else {
                finishAffinity()
                message?.let { toast(it, Toast.LENGTH_LONG) }
            }
        })

        viewModel.getLogout.observe(this, Observer { response ->
            val (success, message) = response
            if (success) {
                writePref(CommonsConstant.TOKEN, "")
                writePref(CommonsConstant.REFRESH_TOKEN, "")
                startActivity<SplashScreenActivity> {
                    finishAffinity()
                }
            } else {
                message?.let { toast(it, Toast.LENGTH_LONG) }
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
            val fileUri = data.data!!
            iv_photo.setImageCircle(fileUri.toString())
            progress_bar.show()
            val multipartBody = convertToMultipartBody(fileUri)
            viewModel.callUploadImageFile(multipartBody)
        } else if (requestCode == CommonsConstant.REQUEST_CODE_VERIFY_PHONE && resultCode == Activity.RESULT_OK) {
            progress_bar.show()
            val phoneNumber = et_phone.getContents()
            val changePhone = ChangePhoneRequest(phoneNumber)
            viewModel.callChangeProfile(changePhone)
        }
    }

    private fun changeProfile() {
        when {
            et_phone.isEmpty(getString(R.string.error_empty_phone)) -> return
            et_phone.isEqualLength(10, getString(R.string.error_equal_length, 10)) -> return
            et_phone.isVerifyPhone(getString(R.string.error_phone)) -> return
        }

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
            R.id.option_contact_admin -> {
                startActivity(Intent.ACTION_DIAL, getString(R.string.contact_admin_tel))
            }
            R.id.option_logout -> dialogLogout {
                FirebaseAuth.getInstance().signOut()
                viewModel.deleteUser()
                viewModel.callLogout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
