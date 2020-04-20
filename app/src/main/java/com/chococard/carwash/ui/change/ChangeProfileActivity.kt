package com.chococard.carwash.ui.change

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.models.User
import com.chococard.carwash.data.networks.ChangeApi
import com.chococard.carwash.data.repositories.ChangeRepository
import com.chococard.carwash.util.Commons
import com.chococard.carwash.util.base.BaseActivity
import com.chococard.carwash.util.extension.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_change_profile.*

class ChangeProfileActivity : BaseActivity<ChangeViewModel, ChangeFactory>() {

    override fun viewModel() = ChangeViewModel::class.java

    override fun factory() = ChangeFactory(ChangeRepository(ChangeApi.invoke(baseContext)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        init()
    }

    private fun init() {
        setToolbar(toolbar)

        //set widgets
        val user = Gson().fromJson(readPref(Commons.USER), User::class.java)
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
        viewModel.upload.observe(this, Observer {
            progress_bar.hide()
        })

        viewModel.changeProfile.observe(this, Observer { response ->
            val (success, message) = response
            progress_bar.hide()
            message?.let { toast(it) }
            if (success) {
                progress_bar.show()
                viewModel.fetchUser()
            }
        })

        viewModel.user.observe(this, Observer { response ->
            val (success, message, userInfo) = response
            progress_bar.hide()
            if (success) {
                writePref(Commons.USER, Gson().toJson(userInfo))
                finish()
            } else {
                message?.let { toast(it) }
            }
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

    private fun changeProfile() {
        when {
            et_full_name.isEmpty(getString(R.string.error_empty_name)) -> return
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
        viewModel.changeProfile(
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
