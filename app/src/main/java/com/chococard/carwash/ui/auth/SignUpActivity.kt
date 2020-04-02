package com.chococard.carwash.ui.auth

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.AuthApi
import com.chococard.carwash.data.repositories.AuthRepository
import com.chococard.carwash.util.extension.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthViewModel
    private val REQUEST_CODE = 1
    private var mImgBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        init()
    }

    private fun init() {
        val factory = AuthFactory(AuthRepository(AuthApi.invoke()))
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        //event
        iv_arrow_back.setOnClickListener {
            onBackPressed()
        }

        iv_photo.setOnClickListener { selectImage() }

        iv_camera.setOnClickListener { selectImage() }

        bt_sign_up.setOnClickListener { signUp() }

        tv_sign_in.setOnClickListener {
            Intent(baseContext, SignInActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }

        //observe
        viewModel.signUpResponse.observe(this, Observer {
            progress_bar.hide()
            toast("${it.success}, ${it.message}")
        })
    }

    private fun selectImage() = Intent().apply {
        type = "image/*"
        action = Intent.ACTION_GET_CONTENT
        startActivityForResult(this, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val path = data.data
            mImgBitmap = MediaStore.Images.Media.getBitmap(contentResolver, path)
            iv_photo.loadCircle(mImgBitmap)
            toast("$mImgBitmap")
        }
    }

    private fun signUp() {
        when {
            et_name.isEmpty(getString(R.string.error_empty_name)) -> return
            et_username.isEmpty(getString(R.string.error_empty_username)) -> return
            et_password.isEmpty(getString(R.string.error_empty_password)) -> return
            et_re_password.isEmpty(getString(R.string.error_empty_re_password)) -> return
            et_identity_card.isEmpty(getString(R.string.error_empty_identity_card)) -> return
            et_phone.isEmpty(getString(R.string.error_empty_phone)) -> return
            et_password.isLength(8, getString(R.string.error_length, 8)) -> return
            et_re_password.isLength(8, getString(R.string.error_length, 8)) -> return
            et_password.isMatching(et_re_password, getString(R.string.error_matching)) -> return
            et_identity_card.isLength(13, getString(R.string.error_length, 13)) -> return
            et_phone.isLength(10, getString(R.string.error_length, 10)) -> return
        }

        //TODO check identity card

        progress_bar.show()
        viewModel.signUp(
            et_name.getContents(),
            et_username.getContents(),
            et_password.getContents(),
            et_identity_card.getContents(),
            et_phone.getContents(),
            ""
        )
    }
}
