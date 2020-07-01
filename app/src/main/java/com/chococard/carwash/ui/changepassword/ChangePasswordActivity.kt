package com.chococard.carwash.ui.changepassword

import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.data.networks.request.ChangePasswordRequest
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.changeprofile.ChangeProfileActivity
import com.chococard.carwash.ui.splashscreen.SplashScreenActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ChangePasswordViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_password.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordActivity : BaseActivity() {

    val viewModel: ChangePasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        init()
    }

    private fun init() {
        setToolbar(toolbar)

        iv_arrow_back.setOnClickListener { onBackPressed() }

        root_layout.setOnClickListener { hideSoftKeyboard() }

        et_re_password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) changePassword()
            false
        }

        bt_cancel.setOnClickListener { finish() }

        bt_confirm.setOnClickListener { changePassword() }

        //toggle password
        et_old_password.onTextChanged {
            et_old_password setTogglePassword iv_toggle_old_password
        }

        iv_toggle_old_password.setOnClickListener {
            iv_toggle_old_password setTogglePassword et_old_password
        }

        et_new_password.onTextChanged {
            et_new_password setTogglePassword iv_toggle_new_password
        }

        iv_toggle_new_password.setOnClickListener {
            iv_toggle_new_password setTogglePassword et_new_password
        }

        et_re_password.onTextChanged {
            et_re_password setTogglePassword iv_toggle_re_password
        }

        iv_toggle_re_password.setOnClickListener {
            iv_toggle_re_password setTogglePassword et_re_password
        }

        //observe
        viewModel.getChangePassword.observe(this, Observer { response ->
            val (success, message) = response
            progress_bar.hide()
            toast(message)
            if (success) {
                writePref(CommonsConstant.TOKEN, "")
                writePref(CommonsConstant.REFRESH_TOKEN, "")
                startActivity<SplashScreenActivity> {
                    finishAffinity()
                }
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
                toast(message, Toast.LENGTH_LONG)
            }
        })

        viewModel.getError.observe(this, Observer {
            progress_bar.hide()
            dialogError(it)
        })
    }

    private fun changePassword() {
        when {
            et_old_password.isEmpty(getString(R.string.error_empty_old_password)) -> return
            et_new_password.isEmpty(getString(R.string.error_empty_new_password)) -> return
            et_re_password.isEmpty(getString(R.string.error_empty_re_password)) -> return
            et_old_password.isMinLength(8, getString(R.string.error_least_length, 8)) -> return
            et_new_password.isMinLength(8, getString(R.string.error_least_length, 8)) -> return
            et_re_password.isMinLength(8, getString(R.string.error_least_length, 8)) -> return
            et_new_password.isMatched(et_re_password, getString(R.string.error_matched)) -> return
        }

        progress_bar.show()
        val changePassword = ChangePasswordRequest(
            et_old_password.getContents(),
            et_new_password.getContents()
        )
        viewModel.callChangePassword(changePassword)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_change_profile -> {
                startActivity<ChangeProfileActivity> {
                    finish()
                }
            }
            R.id.option_contact_admin -> startActivityActionDial()
            R.id.option_logout -> dialogLogout {
                FirebaseAuth.getInstance().signOut()
                viewModel.callLogout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
