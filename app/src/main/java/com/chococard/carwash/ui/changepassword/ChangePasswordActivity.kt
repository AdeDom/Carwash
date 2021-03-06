package com.chococard.carwash.ui.changepassword

import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import com.chococard.carwash.R
import com.chococard.carwash.ui.base.BaseActivity
import com.chococard.carwash.ui.changeprofile.ChangeProfileActivity
import com.chococard.carwash.ui.splashscreen.SplashScreenActivity
import com.chococard.carwash.util.extension.*
import com.chococard.carwash.viewmodel.ChangePasswordViewModel
import kotlinx.android.synthetic.main.activity_change_password.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordActivity : BaseActivity() {

    val viewModel by viewModel<ChangePasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        init()
    }

    private fun init() {
        setToolbar(toolbar)

        // set widget button confirm
        validateChangePassword()

        // set event
        iv_arrow_back.setOnClickListener { onBackPressed() }

        root_layout.setOnClickListener { hideSoftKeyboard() }

        et_re_password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) callChangePassword()
            false
        }

        bt_cancel.setOnClickListener { finish() }

        bt_confirm.setOnClickListener { callChangePassword() }

        //toggle password
        et_old_password.addTextChangedListener {
            validateChangePassword()
            et_old_password setTogglePassword iv_toggle_old_password
        }

        iv_toggle_old_password.setOnClickListener {
            iv_toggle_old_password setTogglePassword et_old_password
        }

        et_new_password.addTextChangedListener {
            validateChangePassword()
            et_new_password setTogglePassword iv_toggle_new_password
        }

        iv_toggle_new_password.setOnClickListener {
            iv_toggle_new_password setTogglePassword et_new_password
        }

        et_re_password.addTextChangedListener {
            validateChangePassword()
            et_re_password setTogglePassword iv_toggle_re_password
        }

        iv_toggle_re_password.setOnClickListener {
            iv_toggle_re_password setTogglePassword et_re_password
        }

        //observe
        viewModel.state.observe { state ->
            if (state.loading) progress_bar.show() else progress_bar.hide()
        }

        viewModel.getChangePassword.observe { response ->
            val (success, message) = response
            root_layout.snackbar(message)
            if (success) {
                startActivity<SplashScreenActivity> {
                    finishAffinity()
                }
            }
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

        viewModel.errorMessage.observe {
            root_layout.snackbar(it)
        }

        viewModel.validateChangePassword.observe {
            if (it) bt_confirm.ready() else bt_confirm.unready()
        }

        viewModel.error.observeError()
    }

    private fun validateChangePassword() = viewModel.setValueValidateChangePassword(
        et_old_password.getContents(),
        et_new_password.getContents(),
        et_re_password.getContents()
    )

    private fun callChangePassword() {
        val oldPassword = et_old_password.getContents()
        val newPassword = et_new_password.getContents()
        val rePassword = et_re_password.getContents()
        viewModel.callChangePassword(oldPassword, newPassword, rePassword)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_change_profile -> startActivity<ChangeProfileActivity> { finish() }
            R.id.option_contact_admin -> dialogContactAdmin { startActivityActionDial() }
            R.id.option_logout -> dialogLogout { viewModel.callLogout() }
        }
        return super.onOptionsItemSelected(item)
    }

}
