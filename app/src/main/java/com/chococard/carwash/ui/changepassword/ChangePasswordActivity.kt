package com.chococard.carwash.ui.changepassword

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.changeprofile.ChangeProfileActivity
import com.chococard.carwash.ui.splashscreen.SplashScreenActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ChangePasswordViewModel
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
        bt_cancel.setOnClickListener { finish() }
        bt_confirm.setOnClickListener { changePassword() }

        //observe
        viewModel.getChangePassword.observe(this, Observer { response ->
            val (success, message) = response
            progress_bar.hide()
            message?.let { toast(it) }
            if (success) {
                writePref(CommonsConstant.TOKEN, "")
                writePref(CommonsConstant.REFRESH_TOKEN, "")
                viewModel.deleteUser()
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
                message?.let { toast(it, Toast.LENGTH_LONG) }
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
        viewModel.callChangePassword(et_old_password.getContents(), et_new_password.getContents())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_change_profile -> {
                startActivity<ChangeProfileActivity> {
                    finish()
                }
            }
            R.id.option_contact_admin -> dialogContactAdmin()
            R.id.option_logout -> dialogLogout {
                viewModel.deleteUser()
                viewModel.callLogout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
