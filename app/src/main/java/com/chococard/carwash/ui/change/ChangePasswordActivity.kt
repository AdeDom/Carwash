package com.chococard.carwash.ui.change

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chococard.carwash.R
import com.chococard.carwash.data.models.User
import com.chococard.carwash.data.networks.ChangeApi
import com.chococard.carwash.data.repositories.ChangeRepository
import com.chococard.carwash.util.base.BaseActivity
import com.chococard.carwash.util.extension.*
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : BaseActivity<ChangeViewModel>() {

    private var mUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        init()

    }

    private fun init() {
        mUser = intent.getParcelableExtra(getString(R.string.user))

        val factory =
            ChangeFactory(ChangeRepository(ChangeApi.invoke(networkConnectionInterceptor)))
        viewModel = ViewModelProvider(this, factory).get(ChangeViewModel::class.java)

        setToolbar(toolbar)

        iv_arrow_back.setOnClickListener { onBackPressed() }
        bt_cancel.setOnClickListener { finish() }
        bt_confirm.setOnClickListener { changePassword() }

        //observe
        viewModel.changePassword.observe(this, Observer { response ->
            progress_bar.hide()
            response.message?.let { toast(it) }
            if (response.success) goToSignIn()
        })

        //exception
        viewModel.exception = {
            progress_bar.hide()
            toast(it)
        }

    }

    private fun changePassword() {
        when {
            et_old_password.isEmpty(getString(R.string.error_empty_old_password)) -> return
            et_new_password.isEmpty(getString(R.string.error_empty_new_password)) -> return
            et_re_password.isEmpty(getString(R.string.error_empty_re_password)) -> return
            et_old_password.isMinLength(8, getString(R.string.error_least_length, 8)) -> return
            et_new_password.isMinLength(8, getString(R.string.error_least_length, 8)) -> return
            et_re_password.isMinLength(8, getString(R.string.error_least_length, 8)) -> return
            et_new_password.isMatching(et_re_password, getString(R.string.error_matching)) -> return
        }

        progress_bar.show()
        viewModel.changePassword(et_old_password.getContents(), et_new_password.getContents())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_change_profile -> {
                Intent(baseContext, ChangeProfileActivity::class.java).apply {
                    putExtra(getString(R.string.user), mUser)
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
