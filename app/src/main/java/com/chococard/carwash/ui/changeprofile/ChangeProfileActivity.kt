package com.chococard.carwash.ui.changeprofile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.models.User
import com.chococard.carwash.data.networks.AppService
import com.chococard.carwash.factory.ChangeProfileFactory
import com.chococard.carwash.repositories.BaseRepository
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.changepassword.ChangePasswordActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ChangeProfileViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_change_profile.*

class ChangeProfileActivity : BaseActivity<ChangeProfileViewModel, ChangeProfileFactory>() {

    override fun viewModel() = ChangeProfileViewModel::class.java

    override fun factory() = ChangeProfileFactory(BaseRepository(AppService.invoke(baseContext)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        init()
    }

    private fun init() {
        setToolbar(toolbar)

        //set widgets
        val user = Gson().fromJson(readPref(CommonsConstant.USER), User::class.java)
        val (_, fullName, idCard, phone, _, image) = user
        et_full_name.setText(fullName)
        et_identity_card.setText(idCard)
        et_phone.setText(phone)
        image?.let { iv_photo.loadCircle(it) }

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
            val (success, message, userInfo) = response
            progress_bar.hide()
            if (success) {
                writePref(CommonsConstant.USER, Gson().toJson(userInfo))
                finish()
            } else {
                message?.let { toast(it) }
            }
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            toast(it, Toast.LENGTH_LONG)
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
            iv_photo.loadCircle(fileUri.toString())
            uploadFile(fileUri) { body, description ->
                progress_bar.show()
                viewModel.callUploadImageFile(body, description)
            }
        }
    }

    private fun changeProfile() {
        when {
            et_full_name.isEmpty(getString(R.string.error_empty_name)) -> return
            et_identity_card.isEmpty(getString(R.string.error_empty_identity_card)) -> return
            et_identity_card.isEqualLength(13, getString(R.string.error_equal_length, 13)) -> return
            et_identity_card.getContents().isVerifyIdentityCard() -> {
                et_identity_card.failed(getString(R.string.error_identity_card))
                return
            }
            et_phone.isEmpty(getString(R.string.error_empty_phone)) -> return
            et_phone.isEqualLength(10, getString(R.string.error_equal_length, 10)) -> return
            et_phone.getContents().isVerifyPhone() -> {
                et_phone.failed(getString(R.string.error_phone))
                return
            }
        }

        progress_bar.show()
        viewModel.callChangeProfile(
            et_full_name.getContents(),
            et_identity_card.getContents(),
            et_phone.getContents()
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_change_password -> {
                Intent(baseContext, ChangePasswordActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
            R.id.option_contact_admin -> contactAdmin()
            R.id.option_logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

}
