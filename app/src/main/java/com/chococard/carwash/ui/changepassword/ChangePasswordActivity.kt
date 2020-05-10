package com.chococard.carwash.ui.changepassword

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseHeaderActivity
import com.chococard.carwash.ui.changeprofile.ChangeProfileActivity
import com.chococard.carwash.ui.signin.SignInActivity
import com.chococard.carwash.util.CommonsConstant
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ChangePasswordViewModel
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : BaseHeaderActivity<ChangePasswordViewModel>() {

    override fun viewModel() = ChangePasswordViewModel::class.java

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
                viewModel.deleteUser()
                Intent(baseContext, SignInActivity::class.java).apply {
                    finishAffinity()
                    startActivity(this)
                }
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
                Intent(baseContext, ChangeProfileActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }
            R.id.option_contact_admin -> dialogContactAdmin()
            R.id.option_logout -> dialogLogout {
                viewModel.deleteUser()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
